package com.npdevs.healthcastle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SugarMeasure extends AppCompatActivity {
	private String MOB_NUMBER;
	private EditText level;
	private Button ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sugar_measure);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");
		ok = findViewById(R.id.button14);
		level = findViewById(R.id.editText16);

		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveSugar();
			}
		});
	}

	private void saveSugar() {
		final int sugar = (int) Double.parseDouble(level.getText().toString());
		final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + MOB_NUMBER);
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users user = dataSnapshot.getValue(Users.class);
				ArrayList<Integer> sugarList = user.getSugar();
				sugarList.add(sugar);
				user.setSugar(sugarList);
				myRef.setValue(user);
				finish();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(SugarMeasure.this, "Cancelled", Toast.LENGTH_LONG).show();

			}
		});
	}
}
