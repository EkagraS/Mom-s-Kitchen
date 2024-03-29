package com.example.momskitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.momskitchen.databinding.ActivityMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity {


    FloatingActionButton order;
    ActivityMenuBinding binding;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new MenuFragment());

        bottomNavigationView=findViewById(R.id.bnv);
        order=findViewById(R.id.floatingActionButton);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, OrderPageActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId=item.getItemId();
            
            if(itemId==R.id.home){
                replaceFragment(new MenuFragment());
            } else if (itemId==R.id.history) {
                replaceFragment(new OrderHistoryFragment());
            } else if (itemId==R.id.favourites) {
                replaceFragment(new FavouritesFragment());
            } else if (itemId==R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

    }

    public  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}