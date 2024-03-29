package com.example.momskitchen;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class FavouritesBoxAdaptor extends RecyclerView.Adapter<FavouritesBoxAdaptor.viewHolder> {


    FavouritesFragment favouritesFragment;
    ArrayList<FavouritesData> favouritesArrayList;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button Cart, favourites, next;
    EditText quantity;
    ImageView image, close;
    LottieAnimationView display;
    TextView Price, Name, message;
    int Quantity=0, price;
    String name, photo;

    ImageView plus, minus;
    TextView text;




    public FavouritesBoxAdaptor(Context context, ArrayList<FavouritesData> favouritesArrayList) {
        this.context = context;
        this.favouritesArrayList = favouritesArrayList;
    }
    @NonNull
    @Override
    public FavouritesBoxAdaptor.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.favourites_box, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesBoxAdaptor.viewHolder holder, int position) {
        FavouritesData favouritesData=favouritesArrayList.get(position);
        holder.foodName.setText(favouritesData.name);
        holder.foodPrice.setText("Price: "+String.valueOf(favouritesData.price));
        Picasso.get().load(favouritesData.image).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return favouritesArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodPrice;
        ImageView foodImage;
        Button remove, cart;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            database = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();

            foodName=itemView.findViewById(R.id.orderFoodName);
            foodPrice=itemView.findViewById(R.id.orderFoodPrice);
            foodImage=itemView.findViewById(R.id.orderFoodImage);
            remove=itemView.findViewById(R.id.removeItemButton);
            cart=itemView.findViewById(R.id.addToCartButton);



            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "reached here");
                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("favourites");
                    int position=getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String orderId = favouritesArrayList.get(position).getId(); // Assuming there's an getId() method in OrderData class
                        Log.d("TAG1", orderId);

                        // Remove the item from the database
                        reference.child(orderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG12", orderId);
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

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("running", "running");
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.quantity_box);

                    Cart = dialog.findViewById(R.id.Cart);
                    Name = dialog.findViewById(R.id.name);
                    Price = dialog.findViewById(R.id.price);
                    image = dialog.findViewById(R.id.photo);
                    plus=dialog.findViewById(R.id.imagePlus);
                    minus=dialog.findViewById(R.id.imageMinus);
                    text=dialog.findViewById(R.id.text);

                    plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Quantity>=0 && Quantity<=9){
                                Quantity++;
                                text.setText(String.valueOf(Quantity));
                            }else{
                                text.setText(String.valueOf(Quantity));
                            }
                        }
                    });

                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Quantity>=1 && Quantity<=10){
                                Quantity--;
                                text.setText(String.valueOf(Quantity));
                            }else{
                                Quantity=0;
                                text.setText(String.valueOf(Quantity));
                            }
                        }
                    });

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        FavouritesData favouritesData = favouritesArrayList.get(position);
                        photo = favouritesData.image;
                        name = foodName.getText().toString();
                        price = Integer.parseInt(String.valueOf(favouritesData.price));
                        Name.setText(name);
                        Price.setText(foodPrice.getText().toString());
                        Picasso.get().load(photo).into(image);

                        Cart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(Quantity==0){
                                    Toast.makeText(context, "Quantity should be at least 1", LENGTH_SHORT).show();
                                }else {
                                    Log.d("FavouritesBoxAdaptor TAG", "onClick: "+name+price+photo);

                                    String ID = auth.getUid();
                                    DatabaseReference reference = database.getReference().child("user").child(ID).child("order");

                                    String id = reference.push().getKey();

                                    Log.d("FavouritesBoxAdaptor", "onClick: "+id+name+price+photo);

                                    OrderData data = new OrderData(id, name, price, photo, Quantity);

                                    reference.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Dialog dialog1=new Dialog(context);
                                                dialog1.setContentView(R.layout.confirmation_box);

                                                next=dialog1.findViewById(R.id.continueButton);
                                                message=dialog1.findViewById(R.id.confirmationMessage);
                                                display=dialog1.findViewById(R.id.displayImage);

                                                message.setText("Successfully added to Cart");
                                                display.setAnimation(R.raw.success_cart);
//                                                display.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        dialog1.dismiss();
//                                                    }
//                                                });

                                                next.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog1.dismiss();
                                                    }
                                                });
                                                dialog.dismiss();
                                                dialog1.show();
                                            } else {
                                                Dialog dialog1=new Dialog(context);
                                                dialog1.setContentView(R.layout.confirmation_box);

                                                next=dialog1.findViewById(R.id.continueButton);
                                                message=dialog1.findViewById(R.id.confirmationMessage);
                                                display=dialog1.findViewById(R.id.displayImage);

                                                message.setText("Failed to add to favourites");
                                                display.setAnimation(R.raw.failed);

//                                                display.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        dialog1.dismiss();
//                                                    }
//                                                });

                                                next.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog1.dismiss();
                                                    }
                                                });
                                                dialog.dismiss();
                                                dialog1.show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                dialog.show();
                }
            });
        }
    }
}
