package com.nidea.app.selfiethief;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.nidea.app.selfiethief.receiver.AdminReceiver;
import com.nidea.app.selfiethief.support.CustomAdapter;
import com.nidea.app.selfiethief.support.ItemObject;
import com.nidea.app.selfiethief.support.Util;


public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
	private static final int ACTIVITY_RESULT = 1;
	private GridView gridView;
	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDPM;
	private CustomAdapter customAdapter = null;
	private List<ItemObject> items = null;
	SwipeRefreshLayout swipeRefreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDeviceAdminSample = new ComponentName(getApplicationContext(), AdminReceiver.class);
		mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		startAdminIntent(this);
		Util.createFolder(getApplicationContext());
		//checkUpdate();

		toolBarStyle();

		initiateGridView();
		implementRefresh();

		//prepareAd();
	}

	private void implementRefresh(){
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);//Initialising
		//Setting Up OnRefresh Listener
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				//onRefresh method is used to perform all the action
				// when the swipe gesture is performed.
				try {
					addTime();//addTime() Method is called
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		//This are some optional methods for customizing
		// the colors and size of the loader.
		swipeRefreshLayout.setColorSchemeResources(
				R.color.blue,       //This method will rotate
				R.color.red,        //colors given to it when
				R.color.yellow,     //loader continues to
				R.color.green);     //refresh.

		//setSize() Method Sets The Size Of Loader
		swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
		//Below Method Will set background color of Loader
		swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
	}
	public void addTime() throws InterruptedException {
		//Here I am using a Handler to perform the refresh
		// action after some time to show some fake time
		// consuming task is being performed.
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				customAdapter.timings.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				//customAdapter.notifyItemInserted(customAdapter.timings.size()-1);
				refreshAdapter();
				if(swipeRefreshLayout.isRefreshing())
					swipeRefreshLayout.setRefreshing(false);
			}
		},4000);

	}
	private void checkUpdate(){
		Util util = new Util();
		util.checkUpdate(this);
	}
	private void startAdminIntent(Context context){
		if (!mDPM.isAdminActive(mDeviceAdminSample)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
		}
	}
	private ArrayList<String> timings;
	private void initiateGridView(){
		gridView = (GridView) findViewById(R.id.grid_view);
		List<ItemObject> allItems = getAllItems();
		timings = new ArrayList<String>();
		timings.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
		.format(new Date()));
		customAdapter = new CustomAdapter(MainActivity.this, allItems,timings);
		if(customAdapter != null){
			gridView.setAdapter(customAdapter);
			gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intentFull = new Intent(getApplicationContext(), FullImageActivity.class);
					CustomAdapter imageAd = (CustomAdapter) parent.getAdapter();
					ItemObject item = (ItemObject) imageAd.getItem(position);
					intentFull.putExtra("date", item.getDate());
					intentFull.putExtra("time", item.getTime());
					intentFull.putExtra("imagepath", item.getImagePath());
					startActivityForResult(intentFull, 1);
				}
			});
		}
	}

	private List<ItemObject> getAllItems(){

		String targetPath = Util.getAbsolutePath() + "/"+getApplicationContext().getResources().getString(R.string.folder_name)+"/";

		File targetDirector = new File(targetPath);

		File[] fileList = targetDirector.listFiles();

		if(fileList != null){
			Arrays.sort(fileList, new Comparator<File>(){
				@Override
				public int compare(File f1, File f2)
				{
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				} });

			for (File file : fileList){
				long modified = file.lastModified();
				Date date = new Date(modified);

				SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM, yyyy");
				SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss");
				if(items == null){
					items= new ArrayList<>();
				}
				items.add(new ItemObject(file.getAbsolutePath(),dateSDF.format(date), timeSDF.format(date)));
			}
		}
		return items;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_rateus) {
			Util util = new Util();
			util.rateUS(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void toolBarStyle(){
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.ic_launcher);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setTitle("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_RESULT) {
			refreshAdapter();
			prepareAd();//showInterstitial();
		}
	}

	private void refreshAdapter(){
		String targetPath = Util.getAbsolutePath() + "/"+getApplicationContext().getResources().getString(R.string.folder_name)+"/";
		File imageFiles = new File(targetPath); 

		if(imageFiles.isDirectory()){
			if(items != null){
				items.clear();
			}
			getAllItems();
			if(customAdapter != null){
				customAdapter.notifyDataSetChanged();	
			}
		}
	}

	private InterstitialAd mInterstitialAd;
	private void prepareAd(){
		MobileAds.initialize(this, getString(R.string.interstitial_full_screen_app_id));
		mInterstitialAd = new InterstitialAd(this);
		// Defined in res/values/strings.xml
		mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_ad_unit));

		AdRequest adRequest = new AdRequest.Builder()
		//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		// Check the LogCat to get your test device ID
		//.addTestDevice("783BCC63920923475AD81D6AB79DF629")
		.build();

		mInterstitialAd.loadAd(adRequest);

		mInterstitialAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				showInterstitial();
			}
			@Override
			public void onAdClosed() {
				//startGame();
			}
		});
	}

	private void showInterstitial() {
		// Show the ad if it's ready. Otherwise toast and restart the game.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		} else {
			//Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
			//startGame();
		}
	}
}
