package com.npdevs.healthcastle;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class push_notification extends IntentService {
	String saved;
	public push_notification() {
		super("push_notification");
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onHandleIntent(Intent intent) {
        Log.e("NSP","Reached Here by NSP");
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
		final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//		ref.addListenerForSingleValueEvent(new ValueEventListener() {
//			@Override
//			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//				loadPreferences();
//				String no=dataSnapshot.child("no").getValue(String.class);
//				if(!saved.equals(no)) {
//					createNotificationChannel();
//					savePreferences(no);
//					notification();
//				}
//			}
//			@Override
//			public void onCancelled(@NonNull DatabaseError databaseError) {
//			}
//		});
		createNotificationChannel();
		notification(); // remove this
	}
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//CharSequence name = getString(R.string.channel_name);
			//String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel("No sync notify", "No sync notify", importance);
			//channel.setDescription(description);
			channel.enableVibration(true);
			channel.enableLights(true);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void notification() {
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, 0);
		//PendingIntent pendingintent = stackBuilder.getPendingIntent(0 , PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "No sync notify")
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setAutoCancel(true)
				.setContentTitle("Sync failed")
				.setContentText("Data not synced in last 24 hours")
				.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("Please sync manually after opening app..."))
				.setContentIntent(pendingintent);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
		notificationManager.notify(12, builder.build());
	}
	private void loadPreferences()
	{
		SharedPreferences sharedPreferences=getSharedPreferences("CheckNPAlarm",MODE_PRIVATE);
		saved=sharedPreferences.getString("No","");
	}
	private void savePreferences(String value)
	{
		SharedPreferences sharedPreferences=getSharedPreferences("CheckNPAlarm",MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString("No", value);
		editor.apply();
	}
}
