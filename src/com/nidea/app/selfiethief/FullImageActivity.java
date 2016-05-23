package com.nidea.app.selfiethief;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nidea.app.selfiethief.support.Util;

public class FullImageActivity extends AppCompatActivity {
	String imagepath = null;
	String date = null;
	String time = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);

		TextView txt = (TextView)findViewById(R.id.grid_item_label);
		Intent i = getIntent();

		imagepath = i.getExtras().getString("imagepath");
		date = i.getExtras().getString("date");
		time = i.getExtras().getString("time");

		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		imageView.setImageBitmap(BitmapFactory.decodeFile(imagepath));
		txt.setText(date+" "+time);
		toolBarStyle();
	}

	private void toolBarStyle(){
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.ic_launcher);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setTitle("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_fullimage, menu);
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
		else if (id == R.id.action_share) {
			shareImage();
			return true;
		}
		else if (id == R.id.action_delete) {
			showAlert();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showAlert(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage("Do you want to remove this thief?");
		builder1.setCancelable(true);

		builder1.setPositiveButton(
				"Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deleteFile();
					}
				});

		builder1.setNegativeButton(
				"No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	private void deleteFile(){
		File file = new File(imagepath);

		if(file.exists()){
			file.delete();
		}
		Intent intent=new Intent(); 
		setResult(2,intent);  
		finish();
	}

	private void shareImage() {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		File imageFileToShare = new File(imagepath);
		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(share, "Share thief!"));
	}
	
	@Override
    public void onDestroy() {
		 
        super.onDestroy();
    }
}
