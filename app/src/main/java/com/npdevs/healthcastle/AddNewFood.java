package com.npdevs.healthcastle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewFood extends AppCompatActivity {
	private EditText food, amount, calories;
	private Button add;
	private DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_food);
		food = findViewById(R.id.editText12);
		amount = findViewById(R.id.editText13);
		calories = findViewById(R.id.editText14);
		add = findViewById(R.id.button7);
		databaseHelper = new DatabaseHelper(this);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str1 = food.getText().toString();
				String str2 = amount.getText().toString();
				String str3 = calories.getText().toString();
				if (str1.equals(null) || str2.equals(null) || str3.equals(null)) {
					Toast.makeText(getApplicationContext(), "Fill all fields!!!", Toast.LENGTH_SHORT).show();
				} else {
					databaseHelper.insertData(str1, Integer.parseInt(str2), Integer.parseInt(str3));
					finish();
				}
			}
		});
	}
}
