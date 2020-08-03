package com.npdevs.healthcastle;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
	private String MOB_NUMBER;
	private Users user;
	private EditText name, mob, age, weight, height, sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");

		name = findViewById(R.id.editText);
		mob = findViewById(R.id.editText2);
		age = findViewById(R.id.editText3);
		weight = findViewById(R.id.editText4);
		height = findViewById(R.id.editText5);
		sex = findViewById(R.id.editText6);

		final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + MOB_NUMBER);
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				user = dataSnapshot.getValue(Users.class);
				assert user != null;
				name.setText(user.getName());
				mob.setText(user.getMob());
				age.setText(user.getAge() + "");
				weight.setText(user.getWeight() + "");
				height.setText(user.getHeight() + "");
				sex.setText(user.getSex() == 1 ? "M" : "F");
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});


		findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				user.setAge(Integer.parseInt(age.getText().toString()));
				user.setHeight((int) Double.parseDouble(height.getText().toString()));
				user.setWeight(Integer.parseInt(weight.getText().toString()));
				myRef.setValue(user);
				Toast.makeText(EditProfile.this, "Data Updated", Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}
}
