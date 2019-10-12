package com.npdevs.healthcastle;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class BootWakeUp extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		Intent startServiceIntent = new Intent(context, push_notification.class);
		startWakefulService(context, startServiceIntent);
	}
}
