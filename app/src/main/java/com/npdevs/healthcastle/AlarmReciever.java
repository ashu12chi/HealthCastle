package com.npdevs.healthcastle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {
	public static final int REQUEST_CODE = 12345;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, push_notification.class);
		context.startService(i);
	}
}
