package com.npdevs.healthcastle;

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

public class Emotions extends AppCompatActivity {
	String MOB;
	List<SampleItem3> msampleItem = new ArrayList<>();
	private DatabaseReference databaseReference;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emotions);
		MOB = getIntent().getStringExtra("MOB_NUMBER");
		databaseReference = FirebaseDatabase.getInstance().getReference("users/" + MOB);
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users users = dataSnapshot.getValue(Users.class);
				ArrayList<String> emotion = users.getEmotions();
				int sad = 0, angry = 0, fear = 0, happy = 0, neutral = 0, disgust = 0, surprise = 0;
				for (String x : emotion) {
					if (x.equals("Sad"))
						sad++;
					else if (x.equals("Angry"))
						angry++;
					else if (x.equals("Fear"))
						fear++;
					else if (x.equals("Happy"))
						happy++;
					else if (x.equals("Neutral"))
						neutral++;
					else if (x.equals("Disgust"))
						disgust++;
					else if (x.equals("Surprise"))
						surprise++;
				}
				msampleItem.add(new SampleItem3(sad + "", angry + "", fear + "", happy + "", neutral + "",
						disgust + "", surprise + ""));
				recyclerView = findViewById(R.id.recyclerview);
				recyclerView = findViewById(R.id.recyclerview);
				recyclerView.setHasFixedSize(true);
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Emotions.this);
				recyclerView.setLayoutManager(layoutManager);
				RecyclerView.Adapter adapter = new MainAdapter(msampleItem);
				recyclerView.setAdapter(adapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	private class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

		private List<SampleItem3> samples;

		MainAdapter(List<SampleItem3> samples) {
			this.samples = samples;
			Log.e("nsp", samples.size() + "");
		}

		@NonNull
		@Override
		public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater
					.from(parent.getContext())
					.inflate(R.layout.item_main_feature1, parent, false);

			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
			holder.sad.setText("Sad: " + samples.get(position).getSad());
			holder.angry.setText("Angry: " + samples.get(position).getAngry());
			holder.fear.setText("Fear: " + samples.get(position).getFear());
			holder.happy.setText("Happy: " + samples.get(position).getHappy());
			holder.neutral.setText("Neutral: " + samples.get(position).getNeutral());
			holder.disgust.setText("Disgust: " + samples.get(position).getDisgust());
			holder.surprise.setText("Surprise: " + samples.get(position).getSurprise());
		}

		@Override
		public int getItemCount() {
			return samples.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {

			private TextView sad, angry, fear, happy, neutral, disgust, surprise;

			ViewHolder(View view) {
				super(view);
				sad = view.findViewById(R.id.textView21);
				angry = view.findViewById(R.id.textView22);
				fear = view.findViewById(R.id.textView23);
				happy = view.findViewById(R.id.textView24);
				neutral = view.findViewById(R.id.textView25);
				disgust = view.findViewById(R.id.textView26);
				surprise = view.findViewById(R.id.textView27);
			}
		}
	}
}
