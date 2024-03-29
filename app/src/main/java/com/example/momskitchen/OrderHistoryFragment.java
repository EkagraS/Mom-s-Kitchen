package com.example.momskitchen;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {

    RecyclerView OrderHistoryRecyclerview;
    FirebaseAuth auth;
    FirebaseDatabase database;
    OrderHistoryDisplayAdaptor orderHistoryDisplayAdaptor;
    ArrayList<OrderHistoryDisplayData> orderHistoryDisplayDataArrayList;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;
    Context context;
    String ID, ID1;
    TextView detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View view= inflater.inflate(R.layout.fragment_order_history, container, false);

        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        String id=auth.getUid();
        detail=view.findViewById(R.id.EmptyDetail);


        OrderHistoryRecyclerview=view.findViewById(R.id.RecyclerViewOrderHistory);
        orderHistoryDisplayDataArrayList=new ArrayList<>();
        orderHistoryCartDataArrayList=new ArrayList<>();
        OrderHistoryRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryDisplayAdaptor=new OrderHistoryDisplayAdaptor(getContext(), orderHistoryDisplayDataArrayList, orderHistoryCartDataArrayList);
        OrderHistoryRecyclerview.setAdapter(orderHistoryDisplayAdaptor);

        DatabaseReference databaseReference= database.getReference().child("user").child(id).child("orderHistory");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryDisplayDataArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren() ){
                    if(snapshot.exists()&&snapshot.getChildrenCount()>0) {
                        detail.setVisibility(View.GONE);
                        OrderHistoryDisplayData orderHistoryDisplayData = dataSnapshot.getValue(OrderHistoryDisplayData.class);
                        orderHistoryDisplayDataArrayList.add(orderHistoryDisplayData);
                        Log.d("Order History Fragment", "Name: " + orderHistoryDisplayData.date + " " + orderHistoryDisplayData.receiverName + " ");
                    }else{
                        detail.setVisibility(View.VISIBLE);
                    }
                }
                orderHistoryDisplayAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderPageActivity", "Error fetching data: " + error.getMessage());
            }
        });

        return view;
    }
}