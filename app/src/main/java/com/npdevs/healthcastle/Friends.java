package com.npdevs.healthcastle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {
	private String MOB;
	private TextView name, heartStats, stepStats, calorieStats, sugarStats, bloodPressureDisplay, emotions;
	private DatabaseReference databaseReference;
	private Button doctors, chat, pharmacy, diagnostics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		MOB = getIntent().getStringExtra("MOB");
		Log.e("NSP", "Number recieved: " + MOB);
		name = findViewById(R.id.name);
		doctors = findViewById(R.id.button9);
		chat = findViewById(R.id.button11);
		pharmacy = findViewById(R.id.button12);
		diagnostics = findViewById(R.id.button13);
		heartStats = findViewById(R.id.textView14);
		stepStats = findViewById(R.id.textView15);
		calorieStats = findViewById(R.id.textView16);
		sugarStats = findViewById(R.id.textView17);
		bloodPressureDisplay = findViewById(R.id.textView19);
		emotions = findViewById(R.id.textView20);
		databaseReference = FirebaseDatabase.getInstance().getReference("users/" + MOB);
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users users = dataSnapshot.getValue(Users.class);
				if (users != null) {
					String nameMob = users.getName();
					name.setText(nameMob);
					ArrayList<String> bp = users.getBloodpressure();
					if (bp.size() > 1) {
						bloodPressureDisplay.setText(bp.get(bp.size() - 1) + " (latest)");
					} else {
						bloodPressureDisplay.setText("No data");
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
		heartStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Friends.this, HeartGraph.class);
				intent.putExtra("MOB_NUMBER", MOB);
				startActivity(intent);
			}
		});
		stepStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Friends.this, StepsGraph.class);
				intent.putExtra("MOB_NUMBER", MOB);
				startActivity(intent);
			}
		});
		calorieStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Friends.this, CalorieGraph.class);
				intent.putExtra("MOB_NUMBER", MOB);
				startActivity(intent);
			}
		});
		sugarStats.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Friends.this, SugarLevelGraph.class);
				intent.putExtra("MOB_NUMBER", MOB);
				startActivity(intent);
			}
		});
		doctors.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.practo.com/doctors"));
				startActivity(intent);
			}
		});
		chat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.practo.com/consult"));
				startActivity(intent);
			}
		});
		diagnostics.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.practo.com/tests"));
				startActivity(intent);
			}
		});
		pharmacy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.practo.com/order?utm_source=practonavbar&utm_medium=referral&utm_campaign=practonavbarredirect"));
				startActivity(intent);
			}
		});
		emotions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Friends.this, Emotions.class);
				intent.putExtra("MOB_NUMBER", MOB);
				startActivity(intent);
			}
		});
	}
}
