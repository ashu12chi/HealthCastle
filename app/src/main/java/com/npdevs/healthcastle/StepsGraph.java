package com.npdevs.healthcastle;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StepsGraph extends AppCompatActivity {

	private String MOB_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steps_graph);

		MOB_NUMBER = getIntent().getStringExtra("MOB_NUMBER");

		DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + MOB_NUMBER);
		myRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users user = dataSnapshot.getValue(Users.class);
				ArrayList<Entry> entries = new ArrayList<>();
				assert user != null;
				ArrayList<Integer> data = user.getSteps();
				for (int i = 1; i < data.size(); i++) {
					entries.add(new Entry(i - 1, data.get(i)));
				}
				if (entries.size() >= 2) {
					LineDataSet dataSet = new LineDataSet(entries, "Steps taken values");
					dataSet.setColor(ContextCompat.getColor(StepsGraph.this, R.color.colorPrimary));
					dataSet.setValueTextColor(ContextCompat.getColor(StepsGraph.this, R.color.colorPrimaryDark));

					com.github.mikephil.charting.charts.LineChart chart = findViewById(R.id.chart);

					//****
					// Controlling X axis
					XAxis xAxis = chart.getXAxis();
					// Set the xAxis position to bottom. Default is top
					xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
					//Customizing x axis value
					final String[] months = new String[data.size() - 1];
					for (int i = 0; i < months.length; i++) {
						months[i] = "Day " + i;
					}
//
					IAxisValueFormatter formatter = new IAxisValueFormatter() {
						@Override
						public String getFormattedValue(float value, AxisBase axis) {
							return months[(int) value];
						}
					};
					xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
					xAxis.setValueFormatter(formatter);

					//***
					// Controlling right side of y axis
					YAxis yAxisRight = chart.getAxisRight();
					yAxisRight.setEnabled(false);

					//***
					// Controlling left side of y axis
					YAxis yAxisLeft = chart.getAxisLeft();
					yAxisLeft.setGranularity(1f);

					// Setting Data
					LineData data1 = new LineData(dataSet);
					chart.setData(data1);
					chart.animateX(700);
					//refresh
					chart.invalidate();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(StepsGraph.this, "Sorry, attempt failed!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
