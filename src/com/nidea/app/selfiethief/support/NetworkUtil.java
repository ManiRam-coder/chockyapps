package com.nidea.app.selfiethief.support;



import com.nidea.app.selfiethief.support.ApplicationEnums.Networks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return Networks.TYPE_WIFI.getValue();

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return Networks.TYPE_MOBILE.getValue();
		} 
		return Networks.TYPE_NOT_CONNECTED.getValue();
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == Networks.TYPE_WIFI.getValue()) {
			status = Networks.TYPE_WIFI.getStrvalue();
		} else if (conn == Networks.TYPE_MOBILE.getValue()) {
			status = Networks.TYPE_MOBILE.getStrvalue();
		} else if (conn == Networks.TYPE_NOT_CONNECTED.getValue()) {
			status = Networks.TYPE_NOT_CONNECTED.getStrvalue();
		}
		return status;
	}
}