package com.nidea.app.selfiethief.support;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nidea.app.selfiethief.R;

public class Util {

	private VersionControl vc = new VersionControl();
	public static String app_playStore_link = "https://play.google.com/store/apps/details?id=com.nidea.app.selfiethief&hl=en";
	public static String createFolder(Context context) {

		String folderName = getAbsolutePath()+"/"+context.getResources().getString(R.string.folder_name);
		File wallpaperDirectory = new File(folderName);
		if(!wallpaperDirectory.exists()){
			wallpaperDirectory.mkdirs();
		}
		return folderName;
	}

	public static String createFileName(){
		String extension = ".jpg";
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		return "image"+currentDateTimeString+extension;
	}

	public static String getAbsolutePath(){
		String externalStorageDirectoryPath = Environment
				.getExternalStorageDirectory()
				.getAbsolutePath();

		return externalStorageDirectoryPath;
	}

	public static String getFilePath(String fileName){
		return getAbsolutePath()+"/"+fileName;
	}

	public boolean checkUpdate(final Context context) {
		String latestVersion = "";
		String currentVersion = vc.getCurrentVersion(context);
		try {
			latestVersion = new GetLatestVersion().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		//If the versions are not the same
		if(!currentVersion.equals(latestVersion)){
			return true;
			/*final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("An Update is Available");
			builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String urlOfAppFromPlayStore = Util.app_playStore_link;

					((AppCompatActivity)context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlOfAppFromPlayStore)));
					dialog.dismiss();
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Cancel button action
				}
			});

			builder.setCancelable(false);
			builder.show();*/
		}
		else{
			return false;
		}
	}
	public void rateUS(Context context) {
		//Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
		try {
			String urlOfAppFromPlayStore = Util.app_playStore_link;
			((AppCompatActivity)context).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlOfAppFromPlayStore)));
		} catch (ActivityNotFoundException e) {
			Log.e("Error", "couldn't launch the market");
		}
	}
}
