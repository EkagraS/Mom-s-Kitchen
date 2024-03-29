package com.example.momskitchen;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    RecyclerView favouritesRecyclerview;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FavouritesBoxAdaptor favouritesBoxAdaptor;
    ArrayList<FavouritesData> favouritesArrayList;
    Context context;
    TextView detail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_favourites, container, false);

        favouritesRecyclerview=view.findViewById(R.id.RecyclerViewFavourites);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        String id=auth.getUid();
        detail=view.findViewById(R.id.EmptyDetail);
        DatabaseReference databaseReference= database.getReference().child("user").child(id).child("favourites");
        favouritesArrayList=new ArrayList<>();
        favouritesRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        favouritesBoxAdaptor=new FavouritesBoxAdaptor(getContext(), favouritesArrayList);
        favouritesRecyclerview.setAdapter(favouritesBoxAdaptor);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favouritesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren() ){
                    if (snapshot.exists() && snapshot.getChildrenCount()>0){
                        detail.setVisibility(View.GONE);
                        FavouritesData favouritesData=dataSnapshot.getValue(FavouritesData.class);
                        favouritesArrayList.add(favouritesData);
                        Log.d("Favourites Data5", "Name: " + favouritesData.getName() + ", Price: " + favouritesData.getPrice());
                    }else{
                        detail.setVisibility(View.VISIBLE);
                    }
                }
                favouritesBoxAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderPageActivity", "Error fetching data: " + error.getMessage());
            }
        });

        return view;
    }
}