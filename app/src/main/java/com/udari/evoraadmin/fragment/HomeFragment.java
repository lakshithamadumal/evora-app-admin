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

        // Dashboard එකේ දත්ත Load කිරීම
        loadDashboardStats();

        return binding.getRoot();
    }

    private void loadDashboardStats() {
        // 1. Total Users ගණන බැලීම
        db.collection("Users").addSnapshotListener((value, error) -> {
            if (value != null) {
                binding.tvTotalUsers.setText(String.valueOf(value.size()));
            }
        });

        // 2. Orders, Earnings සහ Today's Stats බැලීම
        db.collection("Orders").addSnapshotListener((value, error) -> {
            if (value != null) {
                double totalEarnings = 0;
                double todayEarnings = 0;
                int pendingCount = 0;
                int todayOrdersCount = 0;

                // අද දවස ලබා ගැනීම (Format: 2024-05-20)
                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                for (DocumentSnapshot doc : value.getDocuments()) {
                    // Firestore එකේ තියෙන Field Names නිවැරදිද බලන්න
                    Double amount = doc.getDouble("totalAmount");
                    String status = doc.getString("status");
                    String date = doc.getString("orderDate");

                    if (amount != null) {
                        totalEarnings += amount;

                        // අද දවසේ දත්ත වෙන් කර ගැනීම
                        if (date != null && date.equals(todayDate)) {
                            todayEarnings += amount;
                            todayOrdersCount++;
                        }
                    }

                    // අලුත් (Paid) orders පමණක් ගණනය කිරීම
                    if ("Paid".equals(status)) {
                        pendingCount++;
                    }
                }

                // UI එක Update කිරීම
                binding.tvTotalEarnings.setText(String.format("Rs. %.2f", totalEarnings));
                binding.tvTodayEarnings.setText(String.format("Rs. %.2f", todayEarnings));
                binding.tvTodayOrders.setText(String.valueOf(todayOrdersCount));
                binding.tvPendingOrders.setText(String.valueOf(pendingCount));
            }
        });

        // 3. Total Products ගණන බැලීම
        db.collection("Products").addSnapshotListener((value, error) -> {
            if (value != null) {
                binding.tvTotalProducts.setText(String.valueOf(value.size()));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}