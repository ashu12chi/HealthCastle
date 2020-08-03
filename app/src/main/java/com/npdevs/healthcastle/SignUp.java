package com.npdevs.healthcastle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

	private Button signup, getOTP;
	private EditText name, mobNumber, password, age, weight, height, sex, city, otp;
	private FirebaseAuth mAuth;
	private ProgressDialog progressDialog;
	private String codeSent;
	PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
		@Override
		public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
			progressDialog.cancel();
		}

		@Override
		public void onVerificationFailed(FirebaseException e) {
			Toast.makeText(SignUp.this, "Something fishy happened!", Toast.LENGTH_SHORT).show();
			Log.e("NSP", "Something fishy happened");
			progressDialog.cancel();
		}

		@Override
		public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
			super.onCodeSent(s, forceResendingToken);
			codeSent = s;
			Log.e("NSP", "Reached Here, Code Found");
			mAuth.signOut();
			progressDialog.cancel();
		}
	};
	private TextInputLayout nameLay, mobLay, passLay, ageLay, weightLay, heightLay, sexLay, cityLay, otpLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		signup = findViewById(R.id.button);
		getOTP = findViewById(R.id.button5);

		name = findViewById(R.id.editText);
		mobNumber = findViewById(R.id.editText2);
		password = findViewById(R.id.editText3);
		age = findViewById(R.id.editText4);
		weight = findViewById(R.id.editText5);
		height = findViewById(R.id.editText6);
		sex = findViewById(R.id.editText7);
		otp = findViewById(R.id.editText8);
		city = findViewById(R.id.editText9);

		nameLay = findViewById(R.id.name_text_input1);
		mobLay = findViewById(R.id.name_text_input2);
		passLay = findViewById(R.id.name_text_input3);
		ageLay = findViewById(R.id.name_text_input4);
		weightLay = findViewById(R.id.name_text_input5);
		heightLay = findViewById(R.id.name_text_input6);
		sexLay = findViewById(R.id.name_text_input7);
		cityLay = findViewById(R.id.name_text_input9);
		otpLay = findViewById(R.id.name_text_input8);

		FirebaseApp.initializeApp(this);
		mAuth = FirebaseAuth.getInstance();

		signup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String n, m, p, a, w, h, s, o;
				n = name.getText().toString();
				m = mobNumber.getText().toString();
				p = password.getText().toString();
				a = age.getText().toString();
				w = weight.getText().toString();
				h = height.getText().toString();
				s = sex.getText().toString();
				o = otp.getText().toString();
				if (n.isEmpty()) {
					nameLay.setError("Enter valid name!");
					name.requestFocus();
					return;
				}
				if (m.length() != 10) {
					mobLay.setError("Enter valid Mobile Number!");
					mobNumber.requestFocus();
					return;
				}
				if (p.isEmpty() || p.length() < 6) {
					passLay.setError("Enter at least 6 length password!");
					password.requestFocus();
					return;
				}
				if (a.isEmpty()) {
					passLay.setError("Enter valid age!");
					password.requestFocus();
					return;
				}
				if (h.isEmpty()) {
					passLay.setError("Enter valid height!");
					password.requestFocus();
					return;
				}
				if (w.isEmpty()) {
					passLay.setError("Enter valid weight!");
					password.requestFocus();
					return;
				}
				if (s.isEmpty() || s.length() != 1) {
					passLay.setError("Enter valid sex character!");
					password.requestFocus();
					return;
				}
				if (o.isEmpty()) {
					passLay.setError("Enter valid OTP!");
					password.requestFocus();
					return;
				} else {
					progressDialog.setMessage("Registering...");
					progressDialog.show();
					verifySignInCode();
				}
			}
		});

		getOTP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String m = mobNumber.getText().toString();
				if (m.isEmpty()) {
					mobLay.setError("Enter valid Mobile Number");
					mobNumber.requestFocus();
				} else {
					progressDialog.setMessage("Sending OTP...");
					progressDialog.show();
					sendVerificationCode();
				}
			}
		});
	}

	private void openFrontActivity(PhoneAuthCredential phoneAuthCredential) {

		mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
							.getReference("users");
					String name1 = name.getText().toString();
					String mobNumber1 = mobNumber.getText().toString();
					String password1 = password.getText().toString();
					int age1 = Integer.parseInt(age.getText().toString());
					int weight1 = Integer.parseInt(weight.getText().toString());
					int height1 = Integer.parseInt(height.getText().toString());
					char sex1 = sex.getText().toString().charAt(0);
					int sex2 = sex1 == 'M' ? 1 : 0;
					String city1 = city.getText().toString();
					MessageDigest digest = null;
					try {
						digest = MessageDigest.getInstance("SHA-256");
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					assert digest != null;
					byte[] pp = digest.digest(password1.getBytes(StandardCharsets.UTF_8));
					password1 = Arrays.toString(pp);
					ArrayList<Integer> first = new ArrayList<>(1);
					first.add(0);
					ArrayList<String> second = new ArrayList<>(1);
					second.add("no");
					final Users users = new Users(name1, mobNumber1, password1, age1, weight1, height1, sex2, city1, first, first, first, first, second, second, second);
					databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							if (dataSnapshot.child(users.getMob()).exists()) {
								Toast.makeText(getApplicationContext(), "User already exists!!!", Toast.LENGTH_SHORT)
										.show();
								progressDialog.cancel();
								mAuth.signOut();    // added this
								finish();
							} else {
								databaseReference.child(users.getMob()).setValue(users);
								Toast.makeText(getApplicationContext(), "SignUp successful!!!", Toast.LENGTH_SHORT).show();
								progressDialog.cancel();
								mAuth.signOut();    // added this
								finish();
							}

						}

						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
							Toast.makeText(getApplicationContext(), "Process Cancelled!", Toast.LENGTH_LONG).show();
							progressDialog.cancel();
						}
					});
				}
			}
		});

		mAuth.signOut();
		mAuth.signOut();
	}

	private void verifySignInCode() {
		String code = otp.getText().toString();
		PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(codeSent, code);
		openFrontActivity(phoneAuthCredential);
	}

	private void sendVerificationCode() {
		String mobNumber1 = mobNumber.getText().toString();
		PhoneAuthProvider.getInstance()
				.verifyPhoneNumber("+91" + mobNumber1, 60, TimeUnit.SECONDS, this, mCallbacks);
		Log.e("NSP", "Reached Here");
		progressDialog.cancel();
	}
}