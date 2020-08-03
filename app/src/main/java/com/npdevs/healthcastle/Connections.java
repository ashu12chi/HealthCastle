package com.npdevs.healthcastle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class Connections extends AppCompatActivity {
	List<SampleItem2> msampleItem = new ArrayList<>();
	private RecyclerView recyclerView;
	private DatabaseReference databaseReference;
	private String MOB_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connections);
		databaseReference = FirebaseDatabase.getInstance().getReference("users");
		recyclerView = findViewById(R.id.recyclerview);
		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users users = dataSnapshot.child(MOB_NUMBER).getValue(Users.class);
				ArrayList<String> family = users.getFamily();
				family.remove(0);
				for (String x : family) {
					String name = dataSnapshot.child(x).getValue(Users.class).getName();
					msampleItem.add(new SampleItem2(x, name));
				}
				recyclerView.setHasFixedSize(true);
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Connections.this);
				recyclerView.setLayoutManager(layoutManager);
				RecyclerView.Adapter adapter = new Connections.MainAdapter(msampleItem);
				recyclerView.setAdapter(adapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	private class MainAdapter extends RecyclerView.Adapter<Connections.MainAdapter.ViewHolder> {
		private List<SampleItem2> samples;

		MainAdapter(List<SampleItem2> samples) {
			this.samples = samples;
			Log.e("nsp", samples.size() + "");
		}

		@NonNull
		@Override
		public Connections.MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater
					.from(parent.getContext())
					.inflate(R.layout.item_main_feature, parent, false);

			return new Connections.MainAdapter.ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull Connections.MainAdapter.ViewHolder holder, int position) {
			holder.textView.setText(samples.get(position).getName());
			final String str = holder.textView.getText().toString();
			final String str1 = samples.get(position).getMob();
			holder.textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(Connections.this, Friends.class);
					intent.putExtra("MOB", str1);
					startActivity(intent);
					finish();
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
