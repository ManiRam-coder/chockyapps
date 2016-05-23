package com.nidea.app.selfiethief.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nidea.app.selfiethief.service.CameraService;

public class AdminReceiver extends DeviceAdminReceiver {

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);

		//Toast.makeText(context, "onEnabled, action", Toast.LENGTH_LONG).show();
	}

	/** Called when disabling device administrator power. */
	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		//Toast.makeText(context, "onEnabled, action", Toast.LENGTH_LONG).show();

		return super.onDisableRequested(context, intent);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		super.onReceive(context, intent);
		//Log.d("DeviceAdmin", "onReceive, action = " + intent.getAction());
	}

	@Override
	public void onPasswordChanged(Context ctxt, Intent intent) {
		/*DevicePolicyManager mgr=
				(DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);*/
	}

	@Override
	public void onPasswordFailed(Context ctxt, Intent intent) {
		//Toast.makeText(ctxt, "u will never break!", Toast.LENGTH_LONG).show();
		startService(ctxt);
		super.onPasswordFailed(ctxt, intent);
		
		//Log.d("DeviceAdmin", "onPasswordFailed");

	}

	private void startService(Context context){
		//Log.d("DeviceAdmin", "startService");
		Intent i= new Intent(context, CameraService.class);
		//i.putExtra("KEY1", "Value to be used by the service");
		context.startService(i); 
	}
	@Override
	public void onPasswordSucceeded(Context ctxt, Intent intent) {
		/*Toast.makeText(ctxt, "good u enterd", Toast.LENGTH_LONG)
		.show();*/
		//Log.d("DeviceAdmin", "onPasswordSucceeded");
		super.onPasswordSucceeded(ctxt, intent);

	}
}
