package com.npdevs.healthcastle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PhoneSearch extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText mob;
    private RecyclerView recyclerView;
    List<SampleItem1> msampleItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_search);
        recyclerView = findViewById(R.id.recyclerview);
        mob = findViewById(R.id.editText15);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    msampleItem.add(new SampleItem1(postSnapshot.getKey()));
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
                if(charSequence.equals(null))
                    return;
                if(charSequence.toString().trim().equals(""))
                    return;
                msampleItem.clear();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            if(postSnapshot.getKey().indexOf(charSequence.toString())>=0){
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
    }
    private class MainAdapter extends RecyclerView.Adapter<PhoneSearch.MainAdapter.ViewHolder> {

        private List<SampleItem1> samples;

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.textView11);
            }
        }

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
                    Intent intent = new Intent(PhoneSearch.this, AddFood.class);
                    intent.putExtra("Food", str);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return samples.size();
        }
    }
}
