package com.example.momskitchen;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PaymentMethodActivity extends AppCompatActivity {

    RadioButton online, offline;
    Button placeOrder;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;
    String ReceiverName, ReceiverAddress;
    ImageButton home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        online=findViewById(R.id.radioButtonOnline);
        offline=findViewById(R.id.radioButtonOffline);
        placeOrder=findViewById(R.id.placeOrderButton);
        home=findViewById(R.id.homeButton);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        orderHistoryCartDataArrayList = new ArrayList<>();

        online.setEnabled(false);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(offline.isChecked()){

                    DatabaseReference reference= database.getReference().child("user").child(auth.getUid()).child("orderHistory");
                    String id=reference.push().getKey();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    String Date= sdf.format(new Date());

                    DatabaseReference databaseReference=database.getReference().child("user").child(auth.getUid()).child("addresses");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ReceiverName=dataSnapshot.child("addressName").getValue().toString();
                            ReceiverAddress=dataSnapshot.child("address").getValue().toString();
                            OrderHistoryDisplayData orderHistoryDisplayData=new OrderHistoryDisplayData(id, Date, ReceiverName, ReceiverAddress);
                            Log.d("Receiver", ReceiverName+ReceiverAddress);
                            reference.child(id).setValue(orderHistoryDisplayData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        DatabaseReference reference= database.getReference().child("user").child(auth.getUid()).child("order");
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                                                        Log.d("surprise", "onDataChange: ");
                                                        String Name = snapshots.child("name").getValue(String.class);
                                                        String Photo=snapshots.child("image").getValue(String.class);
                                                        int Quantity = snapshots.child("quantity").getValue(Integer.class);
                                                        int Price = snapshots.child("price").getValue(Integer.class);
                                                        int TotalPrice=Quantity*Price;

                                                        DatabaseReference reference1= database.getReference().child("user").child(auth.getUid()).child("orderHistory").child(id).child("orderList");
                                                        String id1=reference.push().getKey();

                                                        OrderHistoryCartData orderHistoryCartData=new OrderHistoryCartData(id, Photo, Name, Quantity, Price, TotalPrice);
                                                        orderHistoryCartDataArrayList.add(orderHistoryCartData);
                                                        reference1.child(id1).setValue(orderHistoryCartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    DatabaseReference reference= database.getReference().child("user").child(auth.getUid()).child("order");
                                                                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                orderConformationMessage();
                                                                            }else{
                                                                                Toast.makeText(PaymentMethodActivity.this, "Order Unsuccessful", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }else{
                                                                    Toast.makeText(PaymentMethodActivity.this, "Order Unsuccessful", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        Log.d("SURPRISE", "onDataChange: "+Name+Quantity+Price+TotalPrice);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(PaymentMethodActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }else{
                                        Toast.makeText(PaymentMethodActivity.this, "Order Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(PaymentMethodActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(PaymentMethodActivity.this, "Please choose payment method.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentMethodActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    public void orderConformationMessage(){
        Dialog dialog=new Dialog(PaymentMethodActivity.this);
        dialog.setContentView(R.layout.confirmation_box);

        LottieAnimationView display=dialog.findViewById(R.id.displayImage);
        TextView displayText=dialog.findViewById(R.id.confirmationMessage);
        Button confirm=dialog.findViewById(R.id.continueButton);

        displayText.setText("Your order has been placed");
        display.setAnimation(R.raw.order_confirmed);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(PaymentMethodActivity.this, HomeActivity.class));
            }
        });
        dialog.show();
    }
}