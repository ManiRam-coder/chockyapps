package com.nidea.app.selfiethief.support;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionControl {

	public String getCurrentVersion(Context context){
		PackageManager pm = context.getPackageManager();
		PackageInfo pInfo = null;

		try {
			pInfo =  pm.getPackageInfo(context.getPackageName(),0);

		} catch (PackageManager.NameNotFoundException e1) {
			e1.printStackTrace();
		}
		String currentVersion = pInfo.versionName;

		return currentVersion;
	}
}
