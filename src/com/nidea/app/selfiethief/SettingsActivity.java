package com.nidea.app.selfiethief;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends Activity  {
	private Switch settingSwitch;
	private EditText settingName;
	private EditText settingMailId;
	private EditText settingMobileNo;
	private Button settingSave;
	private EditText settingNoOfAttempt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		settingSwitch = (Switch) findViewById(R.id.sw_activate);
		settingName = (EditText) findViewById(R.id.txt_Name);
		settingMailId = (EditText) findViewById(R.id.txt_EmailAddress);
		settingMobileNo = (EditText) findViewById(R.id.txt_MobileNo);
		settingSave = (Button) findViewById(R.id.btn_save);
		setSwitchListener();
		setsaveListener();
	}

	private void setSwitchListener(){
		settingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton cb, boolean on){
				if(on)
				{

				}
				else
				{

				}
			}
		});
	}
	private void setsaveListener(){
		settingSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				insertSettings();
			}
		});
	}
	private void insertSettings(){

	}
	/*	private void getSettings(){
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ChockyeeSettings settings = dbHandler.getContact(1);
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_rateus) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
