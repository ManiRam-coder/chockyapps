package com.nidea.app.selfiethief.support;

public class ItemObject {

	private String date;
	private String time;
	private String imagePath;

	public ItemObject(String imagePath,String date, String time) {
		this.date = date;
		this.time = time;
		this.imagePath = imagePath;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}




}
