package com.example.momskitchen;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderBoxAdaptor extends RecyclerView.Adapter<OrderBoxAdaptor.viewholder> {

    OrderPageActivity orderPageActivity;
    Context context;
    ArrayList<OrderData> orderBoxArrayList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String Name, Photo;
    int Price;
    Button next;
    LottieAnimationView display;
    TextView message;



    public OrderBoxAdaptor(Context context, OrderPageActivity orderPageActivity, ArrayList<OrderData> orderBoxArrayList) {
        this.context = context;
        this.orderPageActivity = orderPageActivity;
        this.orderBoxArrayList = orderBoxArrayList;
    }

    @NonNull
    @Override
    public OrderBoxAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(orderPageActivity).inflate(R.layout.order_box, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBoxAdaptor.viewholder holder, int position) {
        OrderData orderData = orderBoxArrayList.get(position);
        int totalPrice=orderData.quantity*orderData.price;
        String total=String.valueOf(totalPrice);
        holder.name.setText(orderData.name);
        holder.price.setText("Price: " +orderData.price);
        holder.quantity.setText(String.valueOf("Quantity: "+orderData.quantity));
        holder.totalPrice.setText(String.valueOf("Total: "+total));
        Picasso.get().load(orderData.image).into(holder.image);
    }

    @Override
    public int getItemCount() {return orderBoxArrayList.size();}

    public class viewholder extends RecyclerView.ViewHolder {

        TextView name, price, quantity, remove, favourites, totalPrice;
        ImageView image;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.orderFoodName);
            price = itemView.findViewById(R.id.orderFoodPrice);
            quantity = itemView.findViewById(R.id.orderFoodQuantity);
            image = itemView.findViewById(R.id.orderFoodImage);
            remove = itemView.findViewById(R.id.removeItemButton);
            favourites=itemView.findViewById(R.id.addToFavouritesButton);
            totalPrice=itemView.findViewById(R.id.orderFoodTotalPrice);

            database = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("order");
                    int position=getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String orderId = orderBoxArrayList.get(position).getId(); // Assuming there's an getId() method in OrderData class

                        // Remove the item from the database
                        reference.child(orderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Item removed successfully
                                    notifyDataSetChanged(); // Notify the adapter about the data change
                                } else {
                                    Log.e("TAG", "onComplete: "+task.getException());
                                    // Handle the error
                                }
                            }
                        });
                    }
                }
            });

            favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        OrderData OrderData = orderBoxArrayList.get(position);
                        Photo = OrderData.image;
                        Name = OrderData.name;
                        Price = OrderData.price;

                        String ID= auth.getUid();
                        DatabaseReference reference= database.getReference().child("user").child(ID).child("favourites");

                        Log.d("TAG", "onClick: "+name+Photo+price);
                        String id=reference.push().getKey();
                        FavouritesData data=new FavouritesData(id, Name, Photo, Price);

                        reference.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Dialog dialog1=new Dialog(context);
                                    dialog1.setContentView(R.layout.confirmation_box);

                                    next=dialog1.findViewById(R.id.continueButton);
                                    message=dialog1.findViewById(R.id.confirmationMessage);
                                    display=dialog1.findViewById(R.id.displayImage);

                                    message.setText("Successfully added to favourites");
                                    display.setAnimation(R.raw.success_favourites);

                                    next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog1.dismiss();
                                        }
                                    });
                                    dialog1.show();
                                }else{
                                    Dialog dialog1=new Dialog(context);
                                    dialog1.setContentView(R.layout.confirmation_box);

                                    next=dialog1.findViewById(R.id.continueButton);
                                    message=dialog1.findViewById(R.id.confirmationMessage);
                                    display=dialog1.findViewById(R.id.displayImage);

                                    message.setText("Failed to add to favourites");
                                    display.setAnimation(R.raw.failed);

                                    next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog1.dismiss();
                                        }
                                    });
                                    dialog1.show();
                                }
                            }
                        });
                    }
                }

            });
        }
    }
}
