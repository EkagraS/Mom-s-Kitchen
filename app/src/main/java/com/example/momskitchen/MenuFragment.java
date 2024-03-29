package com.example.momskitchen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    public MenuFragment(){}
    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5;
    FirebaseDatabase database1, database2, database3, database4, database5;
    FoodBoxAdaptor adaptor1, adaptor2, adaptor3, adaptor4, adaptor5;
    ArrayList<FoodBoxData> data1, data2, data3, data4, data5;
    FirebaseAuth auth;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        recyclerView1 = view.findViewById(R.id.RecyclerViewStarters);
        database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference().child("Data").child("starters");

        data1 = new ArrayList<>();
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        adaptor1=new FoodBoxAdaptor(getContext(), data1);
        recyclerView1.setAdapter(adaptor1);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data1.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    FoodBoxData foodBoxData1 = dataSnapshot.getValue(FoodBoxData.class);
                    data1.add(foodBoxData1);
                    Log.d("FoodData", "Name: " + foodBoxData1.getName() + ", Price: " + foodBoxData1.getPrice()+", Image: "+foodBoxData1.getImage());
                }
                adaptor1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching data: " + error.getMessage());
            }
        });

        database2 = FirebaseDatabase.getInstance();
        recyclerView2 = view.findViewById(R.id.RecyclerViewChinese);
        DatabaseReference reference2 = database2.getReference().child("Data").child("chinese");
        data2 = new ArrayList<>();
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        adaptor2=new FoodBoxAdaptor(getContext(), data2);
        recyclerView2.setAdapter(adaptor2);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data2.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    FoodBoxData foodBoxData2 = dataSnapshot.getValue(FoodBoxData.class);
                    data2.add(foodBoxData2);
                    Log.d("FoodData", "Name: " + foodBoxData2.getName() + ", Price: " + foodBoxData2.getPrice());

                }
                adaptor2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching data: " + error.getMessage());
            }
        });

        database3 = FirebaseDatabase.getInstance();
        recyclerView3 = view.findViewById(R.id.RecyclerViewItalian);
        DatabaseReference reference3 = database3.getReference().child("Data").child("italian");
        data3 = new ArrayList<>();
        recyclerView3.setLayoutManager(new LinearLayoutManager(context));
        adaptor3=new FoodBoxAdaptor(getContext(), data3);
        recyclerView3.setAdapter(adaptor3);

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data3.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    FoodBoxData foodBoxData3 = dataSnapshot.getValue(FoodBoxData.class);
                    data3.add(foodBoxData3);
                    Log.d("FoodData", "Name: " + foodBoxData3.getName() + ", Price: " + foodBoxData3.getPrice());
                }
                adaptor3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching data: " + error.getMessage());
            }
        });

        database4 = FirebaseDatabase.getInstance();
        recyclerView4 = view.findViewById(R.id.RecyclerViewSouthIndian);
        DatabaseReference reference4 = database4.getReference().child("Data").child("south indian");
        data4 = new ArrayList<>();
        recyclerView4.setLayoutManager(new LinearLayoutManager(context));
        adaptor4=new FoodBoxAdaptor(getContext(), data4);
        recyclerView4.setAdapter(adaptor4);

        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data4.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    FoodBoxData foodBoxData4 = dataSnapshot.getValue(FoodBoxData.class);
                    data4.add(foodBoxData4);
                    Log.d("FoodData", "Name: " + foodBoxData4.getName() + ", Price: " + foodBoxData4.getPrice());
                }
                adaptor4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching data: " + error.getMessage());
            }
        });

        database5 = FirebaseDatabase.getInstance();
        recyclerView5 = view.findViewById(R.id.RecyclerViewDessert);
        DatabaseReference reference5 = database5.getReference().child("Data").child("Dessert");
        data5 = new ArrayList<>();
        recyclerView5.setLayoutManager(new LinearLayoutManager(context));
        adaptor5=new FoodBoxAdaptor(getContext(), data5);
        recyclerView5.setAdapter(adaptor5);

        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data5.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    FoodBoxData foodBoxData5 = dataSnapshot.getValue(FoodBoxData.class);
                    data5.add(foodBoxData5);
                    Log.d("FoodData", "Name: " + foodBoxData5.getName() + ", Price: " + foodBoxData5.getPrice());
                }
                adaptor5.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error fetching data: " + error.getMessage());
            }
        });

        return view;
    }
}