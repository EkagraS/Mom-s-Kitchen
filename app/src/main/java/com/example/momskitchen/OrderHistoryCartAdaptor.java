package com.example.momskitchen;

import static java.sql.Types.NULL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHistoryCartAdaptor extends RecyclerView.Adapter<OrderHistoryCartAdaptor.viewholder> {

    Context context;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;

    public OrderHistoryCartAdaptor(Context context, ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList) {
        this.context = context;
        this.orderHistoryCartDataArrayList = orderHistoryCartDataArrayList;
    }

    @NonNull
    @Override
    public OrderHistoryCartAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_history_cart_box, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryCartAdaptor.viewholder holder, int position) {
        OrderHistoryCartData orderHistoryCartData=orderHistoryCartDataArrayList.get(position);

        holder.Name.setText(orderHistoryCartData.name);
        holder.Price.setText("Price: Rs "+String.valueOf(orderHistoryCartData.price));
        holder.Quantity.setText("Quantity: "+String.valueOf(orderHistoryCartData.quantity));
        holder.TotalPrice.setText("Total Price: Rs "+String.valueOf(orderHistoryCartData.totprice));
        Picasso.get().load(orderHistoryCartData.img).into(holder.Photo);
    }

    @Override
    public int getItemCount() {
        if (orderHistoryCartDataArrayList == null) {
            return 0;
        } else {
            return orderHistoryCartDataArrayList.size();
        }
    }
    public class viewholder extends RecyclerView.ViewHolder {

        TextView Name, Price, Quantity, TotalPrice;
        ImageView Photo;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.orderHistoryFoodName);
            Price=itemView.findViewById(R.id.orderHistoryFoodPrice);
            Quantity=itemView.findViewById(R.id.orderHistoryFoodQuantity);
            TotalPrice=itemView.findViewById(R.id.orderHistoryFoodTotalPrice);
            Photo=itemView.findViewById(R.id.orderHistoryFoodImage);

//            orderHistoryCartDataArrayList=new ArrayList<>();
        }
    }
}
