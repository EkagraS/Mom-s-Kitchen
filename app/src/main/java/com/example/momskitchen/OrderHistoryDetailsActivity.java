package com.example.momskitchen;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    String id;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;
    OrderHistoryCartAdaptor orderHistoryCartAdaptor;
    RecyclerView DataRecyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        DataRecyclerview=findViewById(R.id.recyclerView);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        orderHistoryCartDataArrayList=new ArrayList<>();
        orderHistoryCartAdaptor=new OrderHistoryCartAdaptor(OrderHistoryDetailsActivity.this, orderHistoryCartDataArrayList);
        DataRecyclerview.setLayoutManager(new LinearLayoutManager(OrderHistoryDetailsActivity.this));
        DataRecyclerview.setAdapter(orderHistoryCartAdaptor);
//        orderHistoryDisplayAdaptor=new OrderHistoryDisplayAdaptor(getContext(), orderHistoryDisplayDataArrayList, orderHistoryCartDataArrayList);

        id = getIntent().getStringExtra("ID");
        if (id != null) {
            DatabaseReference databaseReference=database.getReference().child("user").child(auth.getUid()).child("orderHistory").child(id).child("orderList");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("Favourites Data", "onDataChange: ");
                    orderHistoryCartDataArrayList.clear();
                    if(dataSnapshot.exists()){
                        Log.d("Favourites Data", "snap shot exists");
                        for(DataSnapshot snapshots : dataSnapshot.getChildren()){
                            Log.d("Favourites Data", "snap shot exists and children as well");
                            OrderHistoryCartData orderHistoryCartData=snapshots.getValue(OrderHistoryCartData.class);
                            orderHistoryCartDataArrayList.add(orderHistoryCartData);
                            Log.d("Favourites Data", "Name: " + orderHistoryCartData.name+" "+orderHistoryCartData.price+" ");
                        }orderHistoryCartAdaptor.notifyDataSetChanged();
                    }else{
                        Log.d("Favourites Data", "Snap shot does not exist");
                        Toast.makeText(OrderHistoryDetailsActivity.this, "Snap shot does not exist", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Order History Display Adaptor", "Error fetching data: " + databaseError.getMessage());
                }
            });
        }else {
            Toast.makeText(OrderHistoryDetailsActivity.this, "Please Login", Toast.LENGTH_LONG).show();
        }

    }
}