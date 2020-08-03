package com.npdevs.healthcastle;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
	private Button about;
	private Button login;
	private EditText mobNumber;
	private EditText password;
	private Button signup;
	private TextInputLayout textInputLayout1;
	private TextInputLayout textInputLayout2;
	private ProgressDialog progressDialog;
	private String mobNo;
	private String pswd;
	private String loggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA},
					100);
		}

		loadPreferences();

		if (!loggedIn.equals("no") && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				== PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(),"Login Success!",Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MainActivity.this, FrontActivity.class);
			intent.putExtra("MOB_NUMBER", loggedIn);
			startActivity(intent);
			finish();
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		setContentView(R.layout.activity_main);

		FirebaseApp.initializeApp(this);

		about = findViewById(R.id.about);
		login = findViewById(R.id.login);
		mobNumber = findViewById(R.id.mobNumber);
		password = findViewById(R.id.password);
		signup = findViewById(R.id.button3);
		textInputLayout1 = findViewById(R.id.name_text_input1);
		textInputLayout1.getEditText().addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.length() == 0) {
					textInputLayout1.setError("Field can't be left empty!");
				} else if (charSequence.charAt(0) == 'D' && charSequence.length() != 11) {
					textInputLayout1.setError("Enter valid ID!");
				} else if (charSequence.charAt(0) != 'D' && charSequence.length() != 10) {
					textInputLayout1.setError("Enter 10 digit Mobile Number!");
				} else {
					textInputLayout1.setError(null);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		textInputLayout2 = findViewById(R.id.name_text_input2);
		Objects.requireNonNull(textInputLayout2.getEditText()).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.length() == 0) {
					textInputLayout2.setError("Enter valid password!");
				} else {
					textInputLayout2.setError(null);
				}
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openSignUpActivity();
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAboutActivity();
			}
		});
	}

	private void login() {
		mobNo = mobNumber.getText().toString();
		pswd = password.getText().toString();
		if (pswd.isEmpty()) {
			textInputLayout2.setError("Enter valid Password!");
			password.requestFocus();
		} else {
			progressDialog.setMessage("Logging In...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			openOptionsPage();
		}
	}

	private void openOptionsPage() {
		DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				String mob = mobNumber.getText().toString();
				String pwd = password.getText().toString();
				MessageDigest digest = null;
				try {
					digest = MessageDigest.getInstance("SHA-256");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				assert digest != null;
				byte[] pasd = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
				pwd = Arrays.toString(pasd);
				if (dataSnapshot.child(mob).exists()) {
					if (!mob.isEmpty()) {
						Users login = dataSnapshot.child(mob).getValue(Users.class);
						if (login.getPassword().equals(pwd)) {
							mobNo = mob;
							clearTable();
							saveTable(login.getAge(), login.getHeight(), login.getWeight(), login.getSex());
							Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(MainActivity.this, FrontActivity.class);
							intent.putExtra("MOB_NUMBER", mob);
							startActivity(intent);
							progressDialog.cancel();
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_LONG).show();
							progressDialog.cancel();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Enter 10 digit Mobile number!", Toast.LENGTH_LONG).show();
						progressDialog.cancel();
					}
				} else {
					Toast.makeText(getApplicationContext(), "User is not registered!", Toast.LENGTH_LONG).show();
					progressDialog.cancel();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(getApplicationContext(), "Process Cancelled!", Toast.LENGTH_LONG).show();
				progressDialog.cancel();
			}
		});
	}

	private void openSignUpActivity() {
		Intent intent = new Intent(this, SignUp.class);
		startActivity(intent);
	}

	private void openAboutActivity() {
		Intent intent = new Intent(this, About.class);
		startActivity(intent);
	}

	private void loadPreferences() {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		loggedIn = sharedPreferences.getString("User", "no");
		if (loggedIn.equals("") || loggedIn.isEmpty() || loggedIn.equals("no"))
			loggedIn = "no";
	}

	private void clearTable() {
		SharedPreferences preferences = getSharedPreferences("usersave", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	private void saveTable(int age, int height, int weight, int sex) {
		SharedPreferences sharedPreferences = getSharedPreferences("usersave", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("User", mobNo);
		editor.putString("Age", "" + age);
		editor.putString("Height", "" + height);
		editor.putString("Weight", "" + weight);
		editor.putString("Gender", "" + age);
		editor.apply();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 100: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay!
					if (!loggedIn.equals("no")) {
//            Toast.makeText(getApplicationContext(),"Login Success!",Toast.LENGTH_LONG).show();
						Intent intent = new Intent(MainActivity.this, FrontActivity.class);
						intent.putExtra("MOB_NUMBER", loggedIn);
						startActivity(intent);
						finish();
					}
				} else {
					Toast.makeText(this, "Please grant camera permission then run the app", Toast.LENGTH_LONG).show();
					System.exit(0);
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}

}
