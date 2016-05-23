package com.nidea.app.selfiethief.support;

public class ApplicationEnums {
	public enum Networks {
		TYPE_WIFI("WIFI",1), TYPE_MOBILE("MOBILE",2), TYPE_NOT_CONNECTED("NO_CONNECTION",0);
		private int value;
		private String strvalue;
		
		public String getStrvalue() {
			return strvalue;
		}

		public int getValue() {
			return value;
		}

		private Networks(String strvalue,int value) {
			this.value = value;
			this.strvalue = strvalue;
		}
	}
}
