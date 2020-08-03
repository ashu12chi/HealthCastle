package com.npdevs.healthcastle;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.npdevs.healthcastle.predictivemodels.Classification;
import com.npdevs.healthcastle.predictivemodels.TensorFlowClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class FrontActivity extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener, SurfaceHolder.Callback {
	static final int PIXEL_WIDTH = 48;
	private static boolean FIRST_TIME = true;
	boolean running = false;
	String[] activites = new String[]{"Weight Lifting: general", "Weight Lifting: vigorous", "Bicycling, Stationary: moderate", "Rowing, Stationary: moderate", "Bicycling, Stationary: vigorous", "Dancing: slow, waltz, foxtrot", "Volleyball: non-competitive, general play", "Walking: 3.5 mph", "Dancing: disco, ballroom, square", "Soccer: general", "Tennis: general", "Swimming: backstroke", "Running: 5.2 mph", "Bicycling: 14-15.9 mph", "Digging", "Chopping & splitting wood", "Sleeping", "Cooking", "Auto Repair", "Paint house: outside", "Computer Work", "Welding", "Coaching Sports", "Sitting in Class"};
	int[] calories1 = new int[]{112, 223, 260, 260, 391, 112, 112, 149, 205, 260, 260, 298, 335, 372, 186, 223, 23, 93, 112, 186, 51, 112, 149, 65};
	ImageView iv_image;
	SurfaceView sv;
	SurfaceHolder sHolder;
	Camera mCamera;
	Camera.Parameters parameters;
	Bitmap bmp;
	TensorFlowClassifier classifier;
	private TextView maxCalorie, consumedCalorie, burntCalorie, allowedCalorie, steps;
	private Button checkSafe, addFood, addExercise;
	private DatabaseHelper databaseHelper;
	private DatabaseHelper2 databaseHelper2;
	private TextToSpeech textToSpeech;
	private String MOB_NUMBER;
	private String[] categorties = new String[]{"Whole Milk", "Paneer (Whole Milk)", "Butter", "Ghee", "Apple", "Banana", "Grapes", "Mango", "Musambi", "Orange", "Cooked Cereal", "Rice Cooked", "Chapatti", "Potato", "Dal", "Mixed Vegetables", "Fish", "Mutton", "Egg", "Biscuit (Sweet)", "Cake (Plain)", "Cake (Rich Chocolate)", "Dosa (Plain)", "Dosa (Masala)", "Pakoras", "Puri", "Samosa", "Vada (Medu)", "Biryani (Mutton)", "Biryani (Veg.)", "Curry (Chicken)", "Curry (Veg.)", "Fried Fish", "Pulav (Veg.)", "Carrot Halwa", "Jalebi", "Kheer", "Rasgulla"};
	private int[] measure = new int[]{230, 60, 14, 15, 150, 60, 75, 100, 130, 130, 100, 25, 60, 150, 100, 150, 50, 30, 40, 15, 50, 50, 100, 100, 50, 40, 35, 40, 200, 200, 100, 100, 85, 100, 45, 20, 100, 50};
	private int[] calories = new int[]{150, 150, 45, 45, 55, 55, 55, 55, 55, 55, 80, 80, 80, 80, 80, 80, 55, 75, 75, 70, 135, 225, 135, 250, 175, 85, 140, 70, 225, 200, 225, 130, 140, 130, 165, 100, 180, 140};
	private DrawerLayout dl;
	private ActionBarDrawerToggle t;
	private NavigationView nv;
	private int age, weight, height, sex;
	private SensorManager sensorManager;
	private Sensor sensor;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (t.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");

		checkFamilyHealth();
		FIRST_TIME = true;

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		loadUserData();
		schedulealarm();
		loadModel();

		int index = getFrontCameraId();
		if (index == -1) {
			Toast.makeText(getApplicationContext(), "No front camera", Toast.LENGTH_LONG).show();
		} else {
			iv_image = (ImageView) findViewById(R.id.imageView);
			sv = (SurfaceView) findViewById(R.id.surfaceView);
			sHolder = sv.getHolder();
			sHolder.addCallback(this);
			sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		TextView heart = findViewById(R.id.textView12);
		heart.setText(readHeartbeat());

		dl = findViewById(R.id.activity_front);
		t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

		dl.addDrawerListener(t);
		t.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		nv = findViewById(R.id.nv);
		View headerView = nv.getHeaderView(0);
		Button editProfile = headerView.findViewById(R.id.button8);
		editProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FrontActivity.this, EditProfile.class);
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
			}
		});
		nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int id = item.getItemId();
				switch (id) {
					case R.id.heartbeat:
						//Toast.makeText(FrontActivity.this, "Click finish when satisfied!",Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(FrontActivity.this, HeartMeter.class);
						startActivity(intent);
						return true;
					case R.id.addfood:
						//Toast.makeText(FrontActivity.this, "Settings... who got time for that?",Toast.LENGTH_SHORT).show();
						Intent intent1 = new Intent(FrontActivity.this, AddNewFood.class);
						startActivity(intent1);
						return true;
					case R.id.addexercise:
						//Toast.makeText(FrontActivity.this, "I won't give help!",Toast.LENGTH_SHORT).show();
						Intent intent2 = new Intent(FrontActivity.this, AddNewExercise.class);
						startActivity(intent2);
						return true;
					case R.id.heartbeatstats:
						intent = new Intent(FrontActivity.this, HeartGraph.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.caloriestats:
						intent = new Intent(FrontActivity.this, CalorieGraph.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.sugarstats:
						intent = new Intent(FrontActivity.this, SugarLevelGraph.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.stepsstats:
						intent = new Intent(FrontActivity.this, StepsGraph.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.mobSearch:
						intent = new Intent(FrontActivity.this, PhoneSearch.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.connection:
						intent = new Intent(FrontActivity.this, Connections.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.doctors:
						intent = new Intent(FrontActivity.this, Friends.class);
						intent.putExtra("MOB", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.logout:
						Toast.makeText(FrontActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
						clearTable();
						saveTable();
						intent = new Intent(FrontActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						return true;
					case R.id.contact:
						intent = new Intent(FrontActivity.this, About.class);
						startActivity(intent);
						return true;
					case R.id.feedback:
						intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ashu12chi/HealthCastle"));
						startActivity(intent);
						return true;
					case R.id.sugar:
						intent = new Intent(FrontActivity.this, SugarMeasure.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					case R.id.bloodpressure:
						intent = new Intent(FrontActivity.this, BloodpressureMeasure.class);
						intent.putExtra("MOB_NUMBER", MOB_NUMBER);
						startActivity(intent);
						return true;
					default:
						return true;
				}
			}

		});

		textToSpeech = new TextToSpeech(this, this);

		Button button = findViewById(R.id.button10);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
				startActivityForResult(intent, 10);
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
		if (sex == 1) {
			double bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
			bmr = bmr * 1.2;
			int bmr1 = (int) bmr;
			maxCalorie.setText(bmr1 + "");
			int x = Integer.parseInt(burntCalorie.getText().toString());
			int y = Integer.parseInt(consumedCalorie.getText().toString());
			allowedCalorie.setText(bmr1 + x - y + "");
			saveTable2(bmr1 + x - y + "");
		} else {
			double bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
			bmr = bmr * 1.2;
			int bmr1 = (int) bmr;
			maxCalorie.setText(bmr1 + "");
			int x = Integer.parseInt(burntCalorie.getText().toString());
			int y = Integer.parseInt(consumedCalorie.getText().toString());
			allowedCalorie.setText(bmr1 + x - y + "");
			saveTable2(bmr1 + x - y + "");
		}
		if (res.getCount() == 0) {
			int size = categorties.length;
			for (int i = 0; i < size; i++) {
				databaseHelper.insertData(categorties[i], measure[i], calories[i]);
			}
		}
		checkSafe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openCheckSafeActivity();
			}
		});
		databaseHelper2 = new DatabaseHelper2(this);
		Cursor res2 = databaseHelper2.getAllData();
		//Log.e("ch",res2.getCount()+"");
		if (res2.getCount() == 0) {
			int size = activites.length;
			for (int i = 0; i < size; i++) {
				databaseHelper2.insertData(activites[i], 30, calories1[i]);
			}
			Cursor ashu = databaseHelper2.getAllData();
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
		Intent intent = new Intent(FrontActivity.this, AddExerciseSearch.class);
		startActivity(intent);
	}

	private void openAddFoodActivity() {
		Intent intent = new Intent(this, AddFoodSearch.class);
		startActivity(intent);
	}

	private void clearTable() {
		SharedPreferences preferences = getSharedPreferences("usersave", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTable() {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("User", "no");
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
				1000 * 29 * 60, pIntent);
	}

	private String readHeartbeat() {
		SharedPreferences sharedPreferences = getSharedPreferences("heartbeats", MODE_PRIVATE);
		String beats = sharedPreferences.getString("beats", "no");
		if (beats.equals("") || beats.isEmpty() || beats.equals("no"))
			beats = "NaN";
		return beats;
	}

	private void openCheckSafeActivity() {
		Intent intent = new Intent(this, CheckSafeSearch.class);
		startActivity(intent);
	}

	private String loadPreferences(String whom) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		return sharedPreferences.getString(whom, "0");
	}

	private void loadUserData() {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		String temp = sharedPreferences.getString("Age", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		age = Integer.parseInt(temp);
		temp = sharedPreferences.getString("Height", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		height = Integer.parseInt(temp);
		temp = sharedPreferences.getString("Weight", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		weight = Integer.parseInt(temp);
		temp = sharedPreferences.getString("Sex", "0");
		if (temp.equals("") || temp.isEmpty() || temp.equals("0"))
			temp = "0";
		sex = Integer.parseInt(temp);
	}

	@Override
	public void onResume() {
		super.onResume();
		running = true;
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		//   if(sensorManager!=null)
		//      Log.e("ashu","ashu");
		if (sensor != null) {
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		} else {
			Toast.makeText(this, "Sensor not found!!!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		running = false;
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (running) {
			steps.setText(String.valueOf(sensorEvent.values[0]));
			saveTable1(String.valueOf(sensorEvent.values[0]));
			String burnt1 = loadPreferences("burnt");

			int x = Integer.parseInt(burnt1);
			int z = (int) (0.05 * Double.parseDouble(steps.getText().toString()));
			saveTable(x + z + "");
		}
	}

	private void saveTable(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("burnt", ans);
		editor.apply();
	}

	private void saveTable1(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("steps", ans);
		editor.apply();
	}

	private void saveTable2(String ans) {
		SharedPreferences sharedPreferences = getSharedPreferences("food", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("allowed", ans);
		editor.apply();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void onInit(int i) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && data != null) {
			getWorkDoneFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
		} else {
			Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
		}
	}

	private void getWorkDoneFromResult(ArrayList<String> stringArrayListExtra) {
		for (String str : stringArrayListExtra) {
			if (str.toLowerCase().contains("add food")) {
				Intent intent = new Intent(FrontActivity.this, AddFoodSearch.class);
				try {
					intent.putExtra("SEARCH", str.substring(str.lastIndexOf("add food") + 9).trim());
				} catch (Exception e) {
					intent.putExtra("SEARCH", "");
				}
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("add exercise")) {
				Intent intent = new Intent(FrontActivity.this, AddExerciseSearch.class);
				try {
					intent.putExtra("SEARCH", str.substring(str.lastIndexOf("add exercise") + 13).trim());
				} catch (Exception e) {
					intent.putExtra("SEARCH", "");
				}
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("search person")) {
				Intent intent = new Intent(FrontActivity.this, PhoneSearch.class);
				try {
					intent.putExtra("SEARCH", str.substring(str.lastIndexOf("search person") + 14).trim().replaceAll(" ", ""));
				} catch (Exception e) {
					intent.putExtra("SEARCH", "");
				}
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("steps graph")) {
				Intent intent = new Intent(FrontActivity.this, StepsGraph.class);
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("heart graph") || str.toLowerCase().contains("heartbeat graph") || str.toLowerCase().contains("heart rate graph")) {
				Intent intent = new Intent(FrontActivity.this, HeartGraph.class);
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("measure heart")) {
				Intent intent = new Intent(FrontActivity.this, HeartMeter.class);
				startActivity(intent);
				break;
			}
			if (str.toLowerCase().contains("food graph") || str.toLowerCase().contains("calorie graph")) {
				Intent intent = new Intent(FrontActivity.this, CalorieGraph.class);
				intent.putExtra("MOB_NUMBER", MOB_NUMBER);
				startActivity(intent);
				break;
			}
		}
	}

	private void speak(String string) {
		textToSpeech.speak(String.valueOf(string), TextToSpeech.QUEUE_ADD, null);
	}

	private void checkFamilyHealth() {
		final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users user = dataSnapshot.child(MOB_NUMBER).getValue(Users.class);
				assert user != null;
				ArrayList<String> family = user.getFamily();
				for (int i = 1; i < family.size(); i++) {
					Users member = dataSnapshot.child(family.get(i)).getValue(Users.class);
					assert member != null;
					int sugar = 100, systole = 100;
					if (member.getSugar().size() > 1)
						sugar = member.getSugar().get(member.getSugar().size() - 1);
					if (member.getBloodpressure().size() > 1) {
						String heart = member.getBloodpressure().get(member.getBloodpressure().size() - 1);
						systole = Integer.parseInt(heart.substring(heart.indexOf('-') + 1));
					}
					int depcount = 0;
					ArrayList<String> emo = member.getEmotions();
					int length = emo.size() - 1;
					if (length >= 10) {
						emo.remove(0);
						for (int j = length - 10; j < length; j++) {
							String s = emo.get(j);
							if (s.equalsIgnoreCase("fear") || s.equalsIgnoreCase("angry") || s.equalsIgnoreCase("sad")) {
								depcount++;
							}
						}
					}
					if (sugar > 120 || sugar < 55 || systole < 90 || systole > 180 || depcount >= 8) {
						createNotificationChannel();
						notification(family.get(i), member);
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.e("NSP", "Some error occurred while checking person connections");
			}
		});
	}

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//CharSequence name = getString(R.string.channel_name);
			//String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel("Unhealthy person notify", "Unhealthy person notify", importance);
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
	private void notification(String mob, Users user) {
		Intent intent = new Intent(this, Friends.class);
		intent.putExtra("MOB", mob);
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//PendingIntent pendingintent = stackBuilder.getPendingIntent(0 , PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Unhealthy person notify")
				.setSmallIcon(R.mipmap.ic_launcher_round)
				.setAutoCancel(true)
				.setContentTitle(user.getName() + " is unhealthy")
				.setContentText("Large vary from standard data")
				.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("Tap to see stats..."))
				.setContentIntent(pendingintent);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
		notificationManager.notify(12, builder.build());
	}

	private void loadModel() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					classifier = TensorFlowClassifier.create(getAssets(), "CNN",
							"opt_em_convnet_5000.pb", "labels.txt", PIXEL_WIDTH,
							"input", "output_50", true, 7);

				} catch (final Exception e) {
					//if they aren't found, throw an error!
					throw new RuntimeException("Error initializing classifiers!", e);
				}
			}
		}).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (FIRST_TIME) {
			parameters = mCamera.getParameters();
			mCamera.setParameters(parameters);
			mCamera.startPreview();

			Camera.PictureCallback mCall = new Camera.PictureCallback() {
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
					Matrix matrix = new Matrix();
					matrix.postRotate(-90);
					Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
					iv_image.setImageBitmap(rotatedBitmap);
					detectEmotion();
				}
			};

			mCamera.takePicture(null, null, mCall);
		}
	}

	int getFrontCameraId() {
		Camera.CameraInfo ci = new Camera.CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, ci);
			if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
		}
		return -1; // No front-facing camera found
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		int index = getFrontCameraId();
		if (index == -1) {
//			Toast.makeText(getApplicationContext(), "No front camera", Toast.LENGTH_LONG).show();
		} else {
			mCamera = Camera.open(index);
//			Toast.makeText(getApplicationContext(), "With front camera", Toast.LENGTH_LONG).show();
		}
		mCamera = Camera.open(index);
		try {
			mCamera.setPreviewDisplay(holder);

		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}

	private void detectEmotion() {
		if (FIRST_TIME) {
			FIRST_TIME = false;

			Bitmap image = ((BitmapDrawable) iv_image.getDrawable()).getBitmap();
			Bitmap grayImage = toGrayscale(image);
			Bitmap resizedImage = getResizedBitmap(grayImage, 48, 48);
			int[] pixelarray;

			//Initialize the intArray with the same size as the number of pixels on the image
			pixelarray = new int[resizedImage.getWidth() * resizedImage.getHeight()];

			//copy pixel data from the Bitmap into the 'intArray' array
			resizedImage.getPixels(pixelarray, 0, resizedImage.getWidth(), 0, 0, resizedImage.getWidth(), resizedImage.getHeight());


			float[] normalized_pixels = new float[pixelarray.length];
			for (int i = 0; i < pixelarray.length; i++) {
				// 0 for white and 255 for black
				int pix = pixelarray[i];
				int b = pix & 0xff;
				//  normalized_pixels[i] = (float)((0xff - b)/255.0);
				// normalized_pixels[i] = (float)(b/255.0);
				normalized_pixels[i] = (float) (b);

			}
			System.out.println(normalized_pixels);
			Log.d("pixel_values", String.valueOf(normalized_pixels));
			String text = null;

			try {
				final Classification res = classifier.recognize(normalized_pixels);
				//if it can't classify, output a question mark
				if (res.getLabel() == null) {
					text = "Status: " + ": ?\n";
				} else {
					//else output its name
					text = String.format("%s: %s, %f\n", "Status: ", res.getLabel(),
							res.getConf());
					final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + MOB_NUMBER);
					myRef.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							Users user = dataSnapshot.getValue(Users.class);
							assert user != null;
							ArrayList<String> emo = user.getEmotions();
							emo.add(res.getLabel());
							user.setEmotions(emo);
							myRef.setValue(user);
						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {

						}
					});
				}
			} catch (Exception e) {
				System.out.print("Exception:" + e.toString());

			} finally {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}

			Toast.makeText(FrontActivity.this, text, Toast.LENGTH_LONG).show();
		}
	}

	private Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
		return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	}
}