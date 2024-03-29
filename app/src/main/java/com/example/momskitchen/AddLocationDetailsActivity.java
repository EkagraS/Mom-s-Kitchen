package com.example.momskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocationDetailsActivity extends AppCompatActivity {

    EditText name, phoneNumber, address, city, pincode, state, country, email;
    String Name, PhoneNumber;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button next;
    FirebaseAuth auth;
    FirebaseDatabase database;
    private final static int REQUEST_CODE=100;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_details);

        askPermission();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        name=findViewById(R.id.editTextName);
        phoneNumber=findViewById(R.id.editTextPhoneNumber);
        address=findViewById(R.id.editTextAddress);
        city=findViewById(R.id.editTextCity);
        pincode=findViewById(R.id.editTextPincode);
        state=findViewById(R.id.editTextState);
        country=findViewById(R.id.editTextCountry);
        email=findViewById(R.id.editTextEmailAddress);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        next=findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name=name.getText().toString();
                PhoneNumber=phoneNumber.getText().toString();
                String ADDRESS=address.getText().toString()+", "+city.getText().toString()+", "+state.getText().toString()+", "+country.getText().toString();
                String PINCODE=pincode.getText().toString();
                String EMAIL=email.getText().toString();

                if(TextUtils.isEmpty(Name)){
                    name.setError("Required");
                }else if (TextUtils.isEmpty(PhoneNumber)) {
                    phoneNumber.setError("Required");
                } else if (ContextCompat.checkSelfPermission(AddLocationDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED) {
                    askPermissionbyDialog();
                } else{
                    getLastLocation();
                    DatabaseReference reference= database.getReference().child("user").child(auth.getUid()).child("addresses");

                    AddressData addressData=new AddressData(Name, PhoneNumber, ADDRESS, PINCODE, EMAIL);

                    reference.setValue(addressData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddLocationDetailsActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddLocationDetailsActivity.this, SelectLocationActivity.class));
                                finish();
                            }else{
                                Toast.makeText(AddLocationDetailsActivity.this, "Unable to save address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Geocoder geocoder=new Geocoder(AddLocationDetailsActivity.this, Locale.getDefault());
                        List<Address> addresses= null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            address.setText(addresses.get(0).getLocality()+", "+addresses.get(0).getSubLocality());
                            city.setText(addresses.get(0).getSubAdminArea());
                            pincode.setText(addresses.get(0).getPostalCode());
                            state.setText(addresses.get(0).getAdminArea());
                            country.setText(addresses.get(0).getCountryName());
                        } catch (IOException e) {
                            Toast.makeText(AddLocationDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                          throw new RuntimeException(e);
                        }
                    }
                }
            });
        }else{
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(AddLocationDetailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                askPermissionbyDialog();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void askPermissionbyDialog(){
        Dialog dialog=new Dialog(AddLocationDetailsActivity.this);
        dialog.setContentView(R.layout.permission_box);
        Button yes, no;
        yes=dialog.findViewById(R.id.yesButton);
        no=dialog.findViewById(R.id.noButton);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address.setEnabled(false);
                city.setEnabled(false);
                pincode.setEnabled(false);
                state.setEnabled(false);
                country.setEnabled(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}