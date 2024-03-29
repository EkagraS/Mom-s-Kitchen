package com.example.momskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectLocationActivity extends AppCompatActivity {

    RecyclerView addressRecyclerview;
    FirebaseAuth auth;
    FirebaseDatabase database;
    AddressBoxAdaptor AddressBoxAdaptor;
    ArrayList<AddressData> AddressArrayList;
    ImageButton home;

    Button add, next;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        add=findViewById(R.id.addButton);
        next=findViewById(R.id.nextButton);
        home=findViewById(R.id.homeButton);

        addressRecyclerview=findViewById(R.id.addressRecyclerView);
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        String id=auth.getUid();
        DatabaseReference databaseReference= database.getReference().child("user").child(id).child("addresses");
        AddressArrayList=new ArrayList<>();
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(SelectLocationActivity.this));
        AddressBoxAdaptor=new AddressBoxAdaptor(SelectLocationActivity.this, AddressArrayList);
        addressRecyclerview.setAdapter(AddressBoxAdaptor);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddressData addressData = snapshot.getValue(AddressData.class);
                if (addressData != null) {
                AddressArrayList.add(addressData);
                } else {
                Log.e("Select Location Activity", "AddressData is null for snapshot: ");
                }
                AddressBoxAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Select Location Activity", "Error fetching data: " + error.getMessage());
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectLocationActivity.this, AddLocationDetailsActivity.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference1= database.getReference().child("user").child(id).child("addresses");
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            startActivity(new Intent(SelectLocationActivity.this, PaymentMethodActivity.class));
                        }else{
                            Toast.makeText(SelectLocationActivity.this, "Address already exists", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Select Location Activity", "Error fetching data: " + databaseError.getMessage());
                    }
                });
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectLocationActivity.this, MenuActivity.class));
                finish();
            }
        });
    }
}