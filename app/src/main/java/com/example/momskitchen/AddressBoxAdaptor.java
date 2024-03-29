package com.example.momskitchen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AddressBoxAdaptor extends RecyclerView.Adapter<AddressBoxAdaptor.viewholder> {

    Context context;
    ArrayList<AddressData> addressArrayList;

    public AddressBoxAdaptor(Context context, ArrayList<AddressData> addressArrayList) {
        this.context = context;
        this.addressArrayList = addressArrayList;
    }

    @NonNull
    @Override
    public AddressBoxAdaptor.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.address_box, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressBoxAdaptor.viewholder holder, int position) {
        AddressData addressData=addressArrayList.get(position);
        holder.name.setText(addressData.addressName);
        holder.number.setText(addressData.addressPhone);
        holder.address.setText(addressData.address);
        holder.pincode.setText(addressData.addressPincode);
        holder.email.setText(addressData.Email);
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView name, number, address, pincode, email;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.addressName);
            number=itemView.findViewById(R.id.addressPhoneNumber);
            address=itemView.findViewById(R.id.address);
            pincode=itemView.findViewById(R.id.addressPincode);
            email=itemView.findViewById(R.id.addressEmail);

        }
    }
}
