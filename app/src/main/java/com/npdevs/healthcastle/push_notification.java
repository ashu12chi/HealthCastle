package com.npdevs.healthcastle;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class push_notification extends IntentService {
	String saved, loggedIn;
	int calcon, steps, beats;

	public push_notification() {
		super("push_notification");
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.e("NSP", "Reached Here by NSP");
		WakefulBroadcastReceiver.completeWakefulIntent(intent);
		loadPreferencesMob();
		loadPreferences();
		if (!loggedIn.equals("no")) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date1 = new Date();
			final String date = dateFormat.format(date1);
			Log.e("NSP", "Today's date is: " + date);
			String timeStamp = new SimpleDateFormat("HH").format(new Date());
			final int ty = Integer.parseInt(timeStamp);
			final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + loggedIn);

			ref.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (ty <= 1) {
						resetData();
					}
					if (!date.equals(saved) && ty >= 22) {

						Users user = dataSnapshot.getValue(Users.class);
						loadData();
						assert user != null;

						ArrayList<Integer> temp = user.getCalorie();
						if (calcon != 0)
							temp.add(calcon);
						user.setCalorie(temp);

						temp = user.getSteps();
						if (steps != 0)
							temp.add(steps);
						user.setSteps(temp);

						temp = user.getHeartbeat();
						if (beats != 0)
							temp.add(beats);
						user.setHeartbeat(temp);

					} else if (ty >= 23) {
						createNotificationChannel();
						notification();
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {

				}
			});
		}
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

	private void loadPreferences() {
		SharedPreferences sharedPreferences = getSharedPreferences("SaveDate", MODE_PRIVATE);
		saved = sharedPreferences.getString("Date", "");
	}

	private void savePreferences(String value) {
		SharedPreferences sharedPreferences = getSharedPreferences("SaveDate", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("Date", value);
		editor.apply();
	}

	private void loadPreferencesMob() {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		loggedIn = sharedPreferences.getString("User", "no");
		if (loggedIn.equals("") || loggedIn.isEmpty() || loggedIn.equals("no"))
			loggedIn = "no";
	}

	private void clearTableMob() {
		SharedPreferences preferences = getSharedPreferences("usersave", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTableMob(String mobNo) {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("User", mobNo);
		editor.apply();
	}

	private void loadData() {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		String temp = sharedPreferences.getString("Consumed", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		calcon = Integer.parseInt(temp);

		sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		temp = sharedPreferences.getString("steps", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		steps = (int) Double.parseDouble(temp);

		sharedPreferences = getSharedPreferences("heartbeats", MODE_PRIVATE);
		temp = sharedPreferences.getString("beats", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		beats = Integer.parseInt(temp);
	}

	private void resetData() {
		SharedPreferences preferences = getSharedPreferences("food", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		preferences = getSharedPreferences("heartbeats", Context.MODE_PRIVATE);
		editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}
