package com.npdevs.healthcastle;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PhoneSearch extends AppCompatActivity {
	List<SampleItem1> msampleItem = new ArrayList<>();
	private DatabaseReference databaseReference;
	private EditText mob;
	private String SEARCH;
	private RecyclerView recyclerView;
	private String MOB_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_search);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");
		recyclerView = findViewById(R.id.recyclerview);
		mob = findViewById(R.id.editText15);
		databaseReference = FirebaseDatabase.getInstance().getReference("users");
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					if (!postSnapshot.getKey().equalsIgnoreCase(MOB_NUMBER)) {
						msampleItem.add(new SampleItem1(postSnapshot.getKey()));
					}
				}
				recyclerView.setHasFixedSize(true);
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PhoneSearch.this);
				recyclerView.setLayoutManager(layoutManager);
				RecyclerView.Adapter adapter = new PhoneSearch.MainAdapter(msampleItem);
				recyclerView.setAdapter(adapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
		mob.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.equals(null))
					return;
				if (charSequence.toString().trim().equals(""))
					return;
				msampleItem.clear();
				databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
							if (postSnapshot.getKey().indexOf(charSequence.toString()) >= 0) {
								msampleItem.add(new SampleItem1(postSnapshot.getKey()));
							}
							recyclerView.setHasFixedSize(true);
							RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PhoneSearch.this);
							recyclerView.setLayoutManager(layoutManager);
							RecyclerView.Adapter adapter = new PhoneSearch.MainAdapter(msampleItem);
							recyclerView.setAdapter(adapter);
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		try {
			SEARCH = getIntent().getStringExtra("SEARCH");
			mob.setText(SEARCH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openDialog(final String mob) {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		TextView title = new TextView(this);

		title.setText("");
		title.setPadding(150, 10, 10, 10);   // Set Position
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);
		alertDialog.setCustomTitle(title);

		TextView msg = new TextView(this);

		msg.setText("    Add Person to family list?");
		msg.setTextColor(Color.BLACK);
		msg.setTextSize(20);
		alertDialog.setView(msg);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, " YES ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + mob);
				myRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						Users user = dataSnapshot.getValue(Users.class);
						ArrayList<String> family = user.getFamily();
						if (family.contains(MOB_NUMBER)) {
							Toast.makeText(PhoneSearch.this, "Person already in Family", Toast.LENGTH_LONG).show();
						} else {
							family.add(MOB_NUMBER);
							user.setFamily(family);
							myRef.setValue(user);
							Toast.makeText(PhoneSearch.this, "Person added to Family", Toast.LENGTH_LONG).show();
							finish();
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
			}
		});

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO   ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// NO need to write
			}
		});

		new Dialog(getApplicationContext());
		alertDialog.show();

		// Set Properties for OK Button
		final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
		LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
		neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
		okBT.setPadding(50, 10, 10, 10);   // Set Position
		okBT.setTextColor(Color.BLUE);
		okBT.setLayoutParams(neutralBtnLP);

		final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
		negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
		cancelBT.setTextColor(Color.RED);
		cancelBT.setLayoutParams(negBtnLP);
	}

	private class MainAdapter extends RecyclerView.Adapter<PhoneSearch.MainAdapter.ViewHolder> {

		private List<SampleItem1> samples;

		MainAdapter(List<SampleItem1> samples) {
			this.samples = samples;
			Log.e("nsp", samples.size() + "");
		}

		@NonNull
		@Override
		public PhoneSearch.MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater
					.from(parent.getContext())
					.inflate(R.layout.item_main_feature, parent, false);

			return new PhoneSearch.MainAdapter.ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull PhoneSearch.MainAdapter.ViewHolder holder, int position) {
			holder.textView.setText(samples.get(position).getMob());
			final String str = holder.textView.getText().toString();
			holder.textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openDialog(str);
				}
			});
		}

		@Override
		public int getItemCount() {
			return samples.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {

			private TextView textView;

			ViewHolder(View view) {
				super(view);
				textView = view.findViewById(R.id.textView11);
			}
		}
	}
}
