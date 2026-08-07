package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udari.evoraadmin.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        loadDashboardStats();

        return binding.getRoot();
    }

    private void loadDashboardStats() {
        // 1. Total Users
        db.collection("Users").addSnapshotListener((value, error) -> {
            // binding != null කියන එක අනිවාර්යයි
            if (binding != null && value != null) {
                binding.tvTotalUsers.setText(String.valueOf(value.size()));
            }
        });

        // 2. Orders, Earnings සහ Stats
        db.collection("Orders").addSnapshotListener((value, error) -> {
            if (binding != null && value != null) {
                double totalEarnings = 0;
                double todayEarnings = 0;
                int pendingCount = 0;
                int todayOrdersCount = 0;

                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                for (DocumentSnapshot doc : value.getDocuments()) {
                    Double amount = doc.getDouble("totalAmount");
                    String status = doc.getString("status");
                    String date = doc.getString("orderDate");

                    if (amount != null) {
                        totalEarnings += amount;
                        if (date != null && date.equals(todayDate)) {
                            todayEarnings += amount;
                            todayOrdersCount++;
                        }
                    }

                    if ("Paid".equals(status)) {
                        pendingCount++;
                    }
                }

                // UI Update කරනකොටත් binding check එක ඇතුළේම කරන්න
                binding.tvTotalEarnings.setText(String.format(Locale.getDefault(), "Rs. %.2f", totalEarnings));
                binding.tvTodayEarnings.setText(String.format(Locale.getDefault(), "Rs. %.2f", todayEarnings));
                binding.tvTodayOrders.setText(String.valueOf(todayOrdersCount));
                binding.tvPendingOrders.setText(String.valueOf(pendingCount));
            }
        });

        // 3. Total Products
        db.collection("Products").addSnapshotListener((value, error) -> {
            if (binding != null && value != null) {
                binding.tvTotalProducts.setText(String.valueOf(value.size()));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment එක screen එකෙන් අයින් වෙනකොට binding එක null වෙනවා
        binding = null;
    }
}