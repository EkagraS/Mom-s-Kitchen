package com.example.momskitchen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ListView listView;
    String name, email;
    Context context;
    ArrayList<String> profilePage=new ArrayList<>();
    Button yes, no;
    TextView main, subMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth=FirebaseAuth.getInstance();
        Context context = getContext();
        database=FirebaseDatabase.getInstance();
        listView=view.findViewById(R.id.listView);
        DatabaseReference reference= database.getReference().child("user").child(auth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name=snapshot.child("userName").getValue(String.class);
                    email=snapshot.child("mail").getValue(String.class);

                    profilePage.add(name);
                    profilePage.add(email);
                    profilePage.add("Saved Address");
                    profilePage.add("Location Permission");
                    profilePage.add("Log Out");
                    profilePage.add("Terms and Conditions");
                    profilePage.add("Rate App");
                    profilePage.add("Privacy policy");

                    ArrayAdapter<String> adapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, profilePage);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error","Database Error: " + error.getMessage());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==2){
                    startActivity(new Intent(context, SelectLocationActivity.class));
                } else if (i==3) {

                } else if (i==4) {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.permission_box);

                    yes=dialog.findViewById(R.id.yesButton);
                    no=dialog.findViewById(R.id.noButton);
                    main=dialog.findViewById(R.id.mainText);

                    main.setText("Do you want to log out");

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            auth.signOut();
                            startActivity(new Intent(context, MainActivity.class));
                            getActivity().finish();
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        return view;
    }
}