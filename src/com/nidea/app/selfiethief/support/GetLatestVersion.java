package com.nidea.app.selfiethief.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;

public class GetLatestVersion extends AsyncTask<String, String, String> {

	String latestVersion;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		try {

			//It retrieves the latest version by scraping the content of current version from play store at runtime
			String urlOfAppFromPlayStore = Util.app_playStore_link;
			Document  doc = Jsoup.connect(urlOfAppFromPlayStore).get();
			latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();

		}catch (Exception e){
			e.printStackTrace();

		}

		return latestVersion;
	}
}

