package com.nidea.app.selfiethief;

import com.nidea.app.selfiethief.support.NetworkUtil;
import com.nidea.app.selfiethief.support.Util;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;



public class SplashActivity extends AppCompatActivity {
	private static final String TAG = SplashActivity.class.getSimpleName();


	private long ms = 0;
	private long splashDuration = 2000;
	private boolean splashActive = true;
	private boolean paused = false;

	private ProgressBar mProgress; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_splash);
		mProgress = (ProgressBar)findViewById(R.id.progressBar);

		//checkUpdate();
		//mProgress.setVisibility(View.VISIBLE);
		//mProgress.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

		int secondsDelayed = 1;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				checkConnection();
			}
		}, secondsDelayed * 600);
	}

	private int connectionValue;
	private void checkConnection(){
		connectionValue = NetworkUtil.getConnectivityStatus(getApplicationContext());
		if(connectionValue > 0){
			checkUpdate();
		}
		else{
			callMainActivity();
		}
	}
	private void checkUpdate(){
		Util util = new Util();
		if(util.checkUpdate(this)){
			//mProgress.setVisibility(View.GONE);
			showAlert();
		}
		else{
			callMainActivity();
		}
	}
	private void callMainActivity(){
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		finish();
	}
	private void showAlert(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("An Update is Available");
		builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String urlOfAppFromPlayStore = Util.app_playStore_link;

				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlOfAppFromPlayStore)));
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Cancel button action
				callMainActivity();
			}
		});

		builder.setCancelable(false);
		builder.show();
	}


}