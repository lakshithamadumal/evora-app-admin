package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.udari.evoraadmin.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;

    private ListenerRegistration usersListener;
    private ListenerRegistration ordersListener;
    private ListenerRegistration productsListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        usersListener = db.collection("Users").addSnapshotListener((value, error) -> {
            if (error != null || value == null || binding == null) return;
            binding.tvTotalUsers.setText(String.valueOf(value.size()));
        });

        ordersListener = db.collection("Orders").addSnapshotListener((value, error) -> {
            if (error != null || value == null || binding == null) return;

            double totalEarnings = 0;
            double todayEarnings = 0;
            int pendingCount = 0;
            int todayOrdersCount = 0;

            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            for (DocumentSnapshot doc : value.getDocuments()) {
                Double amount = doc.getDouble("totalAmount");
                if (amount == null) {
                    amount = doc.getDouble("totalPrice");
                }

                String status = doc.getString("status");

                if (amount != null) {
                    totalEarnings += amount;
                }

                com.google.firebase.Timestamp timestamp = doc.getTimestamp("timestamp");

                if (timestamp != null) {
                    Date dateObj = timestamp.toDate();
                    String formattedOrderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateObj);

                    if (formattedOrderDate.equals(todayDate)) {
                        todayOrdersCount++;
                        if (amount != null) {
                            todayEarnings += amount;
                        }
                    }
                }

                if (!"Delivered".equalsIgnoreCase(status) && !"Cancelled".equalsIgnoreCase(status)) {
                    pendingCount++;
                }
            }

            binding.tvTotalEarnings.setText(String.format(Locale.getDefault(), "Rs. %.2f", totalEarnings));
            binding.tvTodayEarnings.setText(String.format(Locale.getDefault(), "Rs. %.2f", todayEarnings));
            binding.tvTodayOrders.setText(String.valueOf(todayOrdersCount));
            binding.tvPendingOrders.setText(String.valueOf(pendingCount));
        });

        productsListener = db.collection("Products").addSnapshotListener((value, error) -> {
            if (error != null || value == null || binding == null) return;
            binding.tvTotalProducts.setText(String.valueOf(value.size()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (usersListener != null) usersListener.remove();
        if (ordersListener != null) ordersListener.remove();
        if (productsListener != null) productsListener.remove();

        binding = null;
    }
}