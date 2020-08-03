package com.npdevs.healthcastle;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExercise extends AppCompatActivity {
	private EditText amount;
	private Button add;
	private TextView food;
	private DatabaseHelper2 databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_exercise);
		amount = findViewById(R.id.editText10);
		add = findViewById(R.id.button6);
		food = findViewById(R.id.editText9);
		final String string = getIntent().getStringExtra("Food");
		food.setText(string);
		databaseHelper = new DatabaseHelper2(this);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Cursor res = databaseHelper.getAllData();
				while (res.moveToNext()) {
					if (res.getString(1).equals(string)) {
						int x = Integer.parseInt(amount.getText().toString());
						int y = res.getInt(2);
						int z = res.getInt(3);
						int ans = (x * z) / y;
						String test = loadPreferences("burnt");
						int prev = 0;
						prev = Integer.parseInt(test);
						ans = ans + prev;
						//clearTable();
						saveTable(ans + "");
						Toast.makeText(getApplicationContext(), ans + "", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
		});
	}

	private String loadPreferences(String whom) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		return sharedPreferences.getString(whom, "0");
	}

	private void clearTable() {
		SharedPreferences preferences = getSharedPreferences("food", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTable(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("burnt", ans);
		editor.apply();
	}
}