package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udari.evoraadmin.adapter.OrderAdapter;
import com.udari.evoraadmin.databinding.FragmentOrderBinding;
import com.udari.evoraadmin.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private List<OrderModel> orderList;
    private OrderAdapter adapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        loadOrdersFromFirestore();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOrders.setAdapter(adapter);
    }

    private void loadOrdersFromFirestore() {
        // Collection නම "Orders" ද කියලා අනිවාර්යයෙන්ම බලන්න
        FirebaseFirestore.getInstance().collection("Orders")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (binding != null && value != null) {
                        orderList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            OrderModel order = doc.toObject(OrderModel.class);
                            if (order != null) {
                                orderList.add(order);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        // තාමත් පේන්නේ නැත්නම් Log එකක් දාලා බලමු ඇත්තටම කීයක් ආවද කියලා
                        android.util.Log.d("OrderCount", "Total Orders: " + orderList.size());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}