package com.example.momskitchen;

import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;
import static java.sql.Types.DATE;
import static java.sql.Types.NULL;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryDisplayAdaptor extends RecyclerView.Adapter<OrderHistoryDisplayAdaptor.viewholder> {

    Context context;
    ArrayList<OrderHistoryDisplayData> orderHistoryDisplayDataArrayList;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;
    FirebaseAuth auth;
    FirebaseDatabase database;
    OrderHistoryCartAdaptor orderHistoryCartAdaptor;



    public OrderHistoryDisplayAdaptor(Context context, ArrayList<OrderHistoryDisplayData> orderHistoryDisplayDataArrayList, ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList) {
        this.context = context;
        this.orderHistoryDisplayDataArrayList = orderHistoryDisplayDataArrayList;
        this.orderHistoryCartDataArrayList = orderHistoryCartDataArrayList;
    }

    @NonNull
    @Override
    public OrderHistoryDisplayAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_history_display_box, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDisplayAdaptor.viewholder holder, int position) {
            OrderHistoryDisplayData orderHistoryDisplayData=orderHistoryDisplayDataArrayList.get(position);

            holder.Date.setText("Order Date: "+orderHistoryDisplayData.date);
            holder.ReceiverName.setText("Receiver Name: "+orderHistoryDisplayData.receiverName);
            holder.Id.setText("Order Id: "+orderHistoryDisplayData.id);
            holder.Status.setText("Delivery Status: Delivered");
            holder.ReceiverAddres.setText("Receiver Address: "+orderHistoryDisplayData.receiverAddress);
    }

    @Override
    public int getItemCount() {
        if (orderHistoryDisplayDataArrayList == null) {
            return 0; // or any other appropriate action
        } else {
            return orderHistoryDisplayDataArrayList.size();
        }
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView Date, ReceiverName, Id, Status, ReceiverAddres;
        ConstraintLayout layout;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            auth= FirebaseAuth.getInstance();
            database= FirebaseDatabase.getInstance();

            Date=itemView.findViewById(R.id.textDate);
            ReceiverAddres=itemView.findViewById(R.id.textReceiverAddress);
            Status=itemView.findViewById(R.id.textViewDeliveryStatus);
            Id=itemView.findViewById(R.id.textOrderId);
            layout=itemView.findViewById(R.id.layout);
            ReceiverName=itemView.findViewById(R.id.textReceiverName);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference1=database.getReference().child("user").child(auth.getUid()).child("orderHistory");
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        int position = getAdapterPosition();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (position != RecyclerView.NO_POSITION) {
                                    OrderHistoryDisplayData clickedItem = orderHistoryDisplayDataArrayList.get(position);
                                    String ID = clickedItem.getId();
                                    Log.d("ID for the intent", ID);
                                    Intent intent=new Intent(context, OrderHistoryDetailsActivity.class);
                                    intent.putExtra("ID", ID);
                                    context.startActivity(intent);
                                }
                            }else {
                                Log.d("TAG OrderHistoryDisplayAdaptor", "snapshots dne");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("OrderHistoryDetailsActivity", "Error fetching data: " + databaseError.getMessage());
                        }
                    });
                }
            });
        }
    }
}