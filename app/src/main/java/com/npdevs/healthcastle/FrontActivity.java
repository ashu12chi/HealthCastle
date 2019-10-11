package com.npdevs.healthcastle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class FrontActivity extends AppCompatActivity {
	private TextView maxCalorie,consumedCalorie,burntCalorie,allowedCalorie,steps;
	private Button checkSafe,addFood,addExercise;
	private DatabaseHelper databaseHelper;
	private DatabaseHelper2 databaseHelper2;
	private String[] categorties=new String[]{"Whole Milk","Paneer (Whole Milk)","Butter","Ghee","Apple","Banana","Grapes","Mango","Musambi","Orange","Cooked Cereal","Rice Cooked","Chapatti","Potato","Dal","Mixed Vegetables","Fish","Mutton","Egg","Biscuit (Sweet)","Cake (Plain)","Cake (Rich Chocolate)","Dosa (Plain)","Dosa (Masala)","Pakoras","Puri","Samosa","Vada (Medu)","Biryani (Mutton)","Biryani (Veg.)","Curry (Chicken)","Curry (Veg.)","Fried Fish","Pulav (Veg.)","Carrot Halwa","Jalebi","Kheer","Rasgulla"};
	private int[] measure=new int[]{230,60,14,15,150,60,75,100,130,130,100,25,60,150,100,150,50,30,40,15,50,50,100,100,50,40,35,40,200,200,100,100,85,100,45,20,100,50};
	private int[] calories=new int[]{150,150,45,45,55,55,55,55,55,55,80,80,80,80,80,80,55,75,75,70,135,225,135,250,175,85,140,70,225,200,225,130,140,130,165,100,180,140};
	private DrawerLayout dl;
	private ActionBarDrawerToggle t;
	private NavigationView nv;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(t.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);

		dl = findViewById(R.id.activity_front);
		t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

		dl.addDrawerListener(t);
		t.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		nv = findViewById(R.id.nv);
		nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int id = item.getItemId();
				switch(id)
				{
					case R.id.heartbeat:
						Toast.makeText(FrontActivity.this, "Click finish when satisfied!",Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(FrontActivity.this,HeartMeter.class);
						startActivity(intent);
						finish();
						return true;
					case R.id.addfood:
						Toast.makeText(FrontActivity.this, "Settings... who got time for that?",Toast.LENGTH_SHORT).show();
						return true;
					case R.id.addexercise:
						Toast.makeText(FrontActivity.this, "I won't give help!",Toast.LENGTH_SHORT).show();
						return true;
					case R.id.viewstats:
						Toast.makeText(FrontActivity.this,"Oh ho,stats!",Toast.LENGTH_SHORT).show();
						return true;
					case R.id.logout:
						Toast.makeText(FrontActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
						clearTable();
						saveTable();
						intent=new Intent(FrontActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
						return true;
					case R.id.contact:
						Toast.makeText(FrontActivity.this,"You can't contact me!",Toast.LENGTH_LONG).show();
						return true;
					case R.id.feedback:
						Toast.makeText(FrontActivity.this,"I don't need feedback!",Toast.LENGTH_LONG).show();
					default:
						return true;
				}
			}

		});


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
			//databaseHelper.insertData("Ashu","20","10");
			int size = categorties.length;
			for(int i=0;i<size;i++){
				databaseHelper.insertData(categorties[i],measure[i],calories[i]);
			}
			//Toast.makeText(this,"Hi",Toast.LENGTH_SHORT).show();
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
				openCheckSafeActivity();
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


	private void clearTable()
	{
		SharedPreferences preferences = getSharedPreferences("usersave", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTable()
	{
		SharedPreferences sharedPreferences=getSharedPreferences("usersave",MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString("User","no");
		editor.apply();
	}
	
	private void openCheckSafeActivity() {
		Intent intent = new Intent(this,CheckSafe.class);
		startActivity(intent);
	}
}