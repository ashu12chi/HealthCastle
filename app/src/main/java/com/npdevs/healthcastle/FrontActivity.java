package com.npdevs.healthcastle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
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
	private String MOB_NUMBER;
	String[] activites=new String[]{"Weight Lifting: general","Weight Lifting: vigorous","Bicycling, Stationary: moderate","Rowing, Stationary: moderate","Bicycling, Stationary: vigorous","Dancing: slow, waltz, foxtrot","Volleyball: non-competitive, general play","Walking: 3.5 mph","Dancing: disco, ballroom, square","Soccer: general","Tennis: general","Swimming: backstroke","Running: 5.2 mph","Bicycling: 14-15.9 mph","Digging","Chopping & splitting wood","Sleeping","Cooking","Auto Repair","Paint house: outside","Computer Work","Welding","Coaching Sports","Sitting in Class"};
	int[] calories1=new int[]{112,223,260,260,391,112,112,149,205,260,260,298,335,372,186,223,23,93,112,186,51,112,149,65};

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
		MOB_NUMBER=getIntent().getStringExtra("MOB_NUMBER");

//		schedulealarm();

		TextView heart=findViewById(R.id.textView12);
		heart.setText(readHeartbeat());

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
					case R.id.heartbeatstats:
						intent = new Intent(FrontActivity.this,HeartGraph.class);
						intent.putExtra("MOB_NUMBER",MOB_NUMBER);
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
						intent = new Intent(FrontActivity.this,About.class);
						startActivity(intent);
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
		consumedCalorie.setText(loadPreferences("consumed"));
		burntCalorie.setText(loadPreferences("burnt"));
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
		databaseHelper2 = new DatabaseHelper2(this);
		Cursor res2 = databaseHelper2.getAllData();
		//Log.e("ch",res2.getCount()+"");
		if(res2.getCount()==0){
			int size = activites.length;
			for(int i=0;i<size;i++){
				databaseHelper2.insertData(activites[i],30,calories1[i]);
			}
			Cursor ashu = databaseHelper2.getAllData();
			/*while (ashu.moveToNext()){
				Log.e("ashu",ashu.getString(0)+" "+ashu.getString(1)+" "+ashu.getString(3));
			}*/
		}
		checkSafe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openCheckSafeActivity();
			}
		});
		addFood.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAddFoodActivity();
			}
		});
		addExercise.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAddExerciseActivity();
			}
		});
	}

	private void openAddExerciseActivity() {
		Intent intent = new Intent(FrontActivity.this,AddExerciseSearch.class);
		startActivity(intent);
	}

	private void openAddFoodActivity() {
		Intent intent = new Intent(this,AddFoodSearch.class);
		startActivity(intent);
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

	private void schedulealarm() {

		// Construct an intent that will execute the AlarmReceiver
		Intent intent = new Intent(this, AlarmReciever.class);
		// Create a PendingIntent to be triggered when the alarm goes off
		final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReciever.REQUEST_CODE,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Setup periodic alarm every every half hour from this point onwards
		AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		// First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
		// Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
				1000*40, pIntent);
	}

	private String readHeartbeat()
	{
		SharedPreferences sharedPreferences=getSharedPreferences("heartbeats",MODE_PRIVATE);
		String beats=sharedPreferences.getString("beats","no");
		if(beats.equals("") || beats.isEmpty() || beats.equals("no"))
			beats="NaN";
		return beats;
	}

	private void openCheckSafeActivity() {
		Intent intent = new Intent(this,CheckSafeSearch.class);
		startActivity(intent);
	}

	private String loadPreferences(String whom)
	{
		SharedPreferences sharedPreferences=getSharedPreferences("food",MODE_PRIVATE);
		return sharedPreferences.getString(whom,"0");
	}
}