package com.npdevs.healthcastle;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FrontActivity extends AppCompatActivity {
	private TextView maxCalorie,consumedCalorie,burntCalorie,allowedCalorie,steps;
	private Button checkSafe,addFood,addExercise;
	private DatabaseHelper databaseHelper;
	private DatabaseHelper2 databaseHelper2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
		maxCalorie = findViewById(R.id.textView2);
		consumedCalorie = findViewById(R.id.textView4);
		burntCalorie = findViewById(R.id.textView6);
		allowedCalorie = findViewById(R.id.textView8);
		steps = findViewById(R.id.textView10);
		checkSafe = findViewById(R.id.button);
		addFood = findViewById(R.id.button2);
		addExercise = findViewById(R.id.button4);
		databaseHelper = new DatabaseHelper(this);
		Cursor res = databaseHelper.getAllData();
		if(res.getCount()==0)
		{
			databaseHelper.insertData("Ashu","20","10");
			Toast.makeText(this,"Hi",Toast.LENGTH_SHORT).show();
		}
       /* databaseHelper2 = new DatabaseHelper2(this);
        Cursor res2 = databaseHelper2.getAllData();
        if(res2.getCount()==0){
            databaseHelper.insertData("Ashu","20","10");
            Toast.makeText(this,"Hi",Toast.LENGTH_SHORT).show();
        }*/
		checkSafe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		addFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		addExercise.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}
}