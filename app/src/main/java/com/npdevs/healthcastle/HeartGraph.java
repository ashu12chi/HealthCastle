package com.npdevs.healthcastle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;

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

public class HeartGraph extends AppCompatActivity {

	private String MOB_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heart_graph);

		MOB_NUMBER=getIntent().getStringExtra("MOB_NUMBER");

		com.github.mikephil.charting.charts.LineChart chart = findViewById(R.id.chart);

		DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("users/"+MOB_NUMBER);
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users user=dataSnapshot.getValue(Users.class);
				assert user != null;
				ArrayList<Integer> data=user.getHeartbeat();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Toast.makeText(HeartGraph.this,"Sorry, attempt failed!", Toast.LENGTH_LONG).show();
			}
		});

		ArrayList<Entry> entries = new ArrayList<>();
		entries.add(new Entry(0, 4));
		entries.add(new Entry(1, 1));
		entries.add(new Entry(2, 2));
		entries.add(new Entry(3, 4));

		LineDataSet dataSet = new LineDataSet(entries, "Customized values");
		dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
		dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

		//****
		// Controlling X axis
		XAxis xAxis = chart.getXAxis();
		// Set the xAxis position to bottom. Default is top
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		//Customizing x axis value
		final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr"};

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
		LineData data = new LineData(dataSet);
		chart.setData(data);
		chart.animateX(2500);
		//refresh
		chart.invalidate();
	}
}
