package com.example.momskitchen;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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


public class FoodBoxAdaptor extends RecyclerView.Adapter<FoodBoxAdaptor.viewholder> {
    HomeActivity MenuActivity;
    ArrayList<FoodBoxData> foodArrayList;
    ArrayList<FavouritesData> favouritesArrayList;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String name, photo, item;
    FoodBoxAdaptor adaptor;
    Button Cart, favourites, next;
    Spinner quantity;
    ImageView image, close;
    LottieAnimationView display;
    TextView Price, Name, message;
    int price, value;
    RadioGroup rgb;

    ImageView plus, minus;
    TextView text;


    public FoodBoxAdaptor(Context context, ArrayList<FoodBoxData> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }


    @NonNull
    @Override
    public FoodBoxAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_box, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBoxAdaptor.viewholder holder, int position) {
        FoodBoxData foodBoxData = foodArrayList.get(position);
        holder.FoodName.setText(foodBoxData.Name);
        holder.FoodPrice.setText("Price: "+String.valueOf(foodBoxData.Price));
        Picasso.get().load(foodBoxData.Image).into(holder.FoodPhoto);
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView FoodPhoto, cart, heart;
        TextView FoodName, FoodPrice;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();

            FoodPhoto = itemView.findViewById(R.id.foodImage);
            FoodName = itemView.findViewById(R.id.foodName);
            FoodPrice = itemView.findViewById(R.id.foodPrice);
            heart = itemView.findViewById(R.id.favouritesButton);
            cart = itemView.findViewById(R.id.cartButton);

                    cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cart.setTranslationY(-20f);
                            cart.animate().translationYBy(20f).setDuration(200);
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.quantity_box);

                            final int[] Quantity = {0};
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
                                    plus.setTranslationX(20f);
                                    plus.animate().translationXBy(-20f).setDuration(200);
                                    if(Quantity[0] >=0 && Quantity[0] <=9){
                                        Quantity[0]++;
                                        text.setText(String.valueOf(Quantity[0]));
                                    }else{
                                        text.setText(String.valueOf(Quantity[0]));
                                    }
                                }
                            });

                            minus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    minus.setTranslationX(-20f);
                                    minus.animate().translationXBy(20f).setDuration(200);
                                    if(Quantity[0] >=1 && Quantity[0] <=10){
                                        Quantity[0]--;
                                        text.setText(String.valueOf(Quantity[0]));
                                    }else{
                                        Quantity[0] =0;
                                        text.setText(String.valueOf(Quantity[0]));
                                    }
                                }
                            });

                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                FoodBoxData foodBoxData = foodArrayList.get(position);
                                photo = foodBoxData.Image;
                                name = FoodName.getText().toString();
                                price = Integer.parseInt(String.valueOf(foodBoxData.Price));
                                Name.setText(name);
                                Price.setText(FoodPrice.getText().toString());
                                Picasso.get().load(photo).into(image);

                                Cart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(Quantity[0] ==0){
                                            Toast.makeText(context, "Quantity should be at least 1", LENGTH_SHORT).show();
                                        }else {
                                            String ID = auth.getUid();
                                            DatabaseReference reference = database.getReference().child("user").child(ID).child("order");

                                            String id = reference.push().getKey();
                                            OrderData data = new OrderData(id, name, price, photo, Quantity[0]);

                                            reference.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Dialog dialog1=new Dialog(context);
                                                        dialog1.setContentView(R.layout.confirmation_box);

                                                        next=dialog1.findViewById(R.id.continueButton);
                                                        message=dialog1.findViewById(R.id.confirmationMessage);
                                                        display=dialog1.findViewById(R.id.displayImage);

                                                        message.setText("Successfully added to cart");
                                                        display.setAnimation(R.raw.success_cart);

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

                                                        message.setText("Failed to add to cart");
                                                        display.setAnimation(R.raw.failed);

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

                    heart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            heart.setTranslationY(-20f);
                            heart.animate().translationYBy(20f).setDuration(200);
                            int position=getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION){
                                FoodBoxData foodBoxData = foodArrayList.get(position);
                                photo = foodBoxData.Image;
                                name = foodBoxData.Name;
                                price = foodBoxData.Price;

                                String ID= auth.getUid();
                                DatabaseReference reference= database.getReference().child("user").child(ID).child("favourites");

                                Log.d("TAG", "onClick: "+name+photo+price);
                                String id=reference.push().getKey();
                                FavouritesData data=new FavouritesData(id, name, photo, price);

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
