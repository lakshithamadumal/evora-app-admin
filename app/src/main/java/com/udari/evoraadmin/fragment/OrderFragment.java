package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udari.evoraadmin.adapter.OrderAdapter;
import com.udari.evoraadmin.databinding.FragmentOrderBinding;
import com.udari.evoraadmin.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private List<OrderModel> orderList;
    private OrderAdapter adapter;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View Binding හරහා Layout එක inflate කිරීම
        binding = FragmentOrderBinding.inflate(inflater, container, false);

        setupOrders();

        return binding.getRoot();
    }

    private void setupOrders() {
        orderList = new ArrayList<>();

        // Sample Data (පසුව Firestore හරහා ලබා ගනී)
        orderList.add(new OrderModel(
                "Laky",
                "Paid",
                "2024-05-25",
                "0712654117",
                "Gampaha",
                "No 45, Main St, Gampaha",
                "Cement Bag x5, Iron Rod x10",
                25000.00
        ));

        orderList.add(new OrderModel(
                "Kasun Perera",
                "Processing",
                "2024-05-26",
                "0771234567",
                "Colombo",
                "Apt 4B, Lotus Tower Rd, Colombo",
                "Paint Bucket x2, Brush x4",
                18500.00
        ));

        // Adapter එක සහ RecyclerView එක සම්බන්ධ කිරීම
        adapter = new OrderAdapter(orderList);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOrders.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Memory leak වැලැක්වීමට
    }
}