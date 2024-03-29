package com.example.momskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.momskitchen.Adapter.AdapterOrderBox;
//import com.example.momskitchen.database_handler.data_base_model.data_order_page;
//import com.example.momskitchen.database_handler.dbhandler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderPageActivity extends AppCompatActivity {

     RecyclerView recyclerViewOrderData;
     OrderBoxAdaptor orderBoxAdaptor;
     FirebaseDatabase database;
     FirebaseAuth auth;
     ArrayList<OrderData> orderData;
     Button confirmOrder;
    TextView detail;
    int sum=0;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        confirmOrder=findViewById(R.id.confirmOrderButton);
        detail=findViewById(R.id.EmptyDetail);
        recyclerViewOrderData = findViewById(R.id.RecyclerViewOrder);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String ID = auth.getUid();
        orderData = new ArrayList<>();
        recyclerViewOrderData.setLayoutManager(new LinearLayoutManager(OrderPageActivity.this));
        orderBoxAdaptor = new OrderBoxAdaptor(OrderPageActivity.this, OrderPageActivity.this, orderData);
        recyclerViewOrderData.setAdapter(orderBoxAdaptor);


        DatabaseReference databaseReference = database.getReference().child("user").child(ID).child("order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderData.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                        detail.setVisibility(View.GONE);
                        OrderData orderData1 = dataSnapshot.getValue(OrderData.class);
                        orderData.add(orderData1);
                        Log.d("FoodData", "Name: " + orderData1.getName() + ", Price: " + orderData1.getPrice());
                    }else{
                        detail.setVisibility(View.VISIBLE);
                    }
                }
                orderBoxAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderPageActivity", "Error fetching data: " + error.getMessage());
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderData.clear();
                        for(DataSnapshot datasnapshot:dataSnapshot.getChildren()) {
                            if(dataSnapshot.exists()&&datasnapshot.getChildrenCount()>0) {
                                detail.setVisibility(View.GONE);
                                startActivity(new Intent(OrderPageActivity.this, SelectLocationActivity.class));
                            }else{
                                Toast.makeText(OrderPageActivity.this, "Cart is Empty", Toast.LENGTH_LONG).show();
                                detail.setVisibility(View.VISIBLE);
                            }
                        }
                        orderBoxAdaptor.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("OrderPageActivity", "Error fetching data: " + databaseError.getMessage());
                    }
                });
            }
        });

    }

}