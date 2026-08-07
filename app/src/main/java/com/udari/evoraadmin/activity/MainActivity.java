package com.udari.evoraadmin.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.udari.evoraadmin.R;
import com.udari.evoraadmin.databinding.ActivityMainBinding;
import com.udari.evoraadmin.fragment.HomeFragment;
import com.udari.evoraadmin.fragment.OrderFragment;
import com.udari.evoraadmin.fragment.ProductFragment;
import com.udari.evoraadmin.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding Setup
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Default Fragment එක විදිහට Home එක පෙන්වනවා
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        initNavigation();
    }

    private void initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bottom_home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.bottom_cart) { // මේක User ලිස්ට් එකට (Menu එකේ ID එක අනුව)
                replaceFragment(new UserFragment());
            } else if (id == R.id.bottom_profile) { // මේක Product ලිස්ට් එකට
                replaceFragment(new ProductFragment());
            } else if (id == R.id.bottom_orders) {
                replaceFragment(new OrderFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}