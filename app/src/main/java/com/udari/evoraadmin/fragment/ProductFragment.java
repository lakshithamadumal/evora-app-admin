package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udari.evoraadmin.adapter.ProductAdapter;
import com.udari.evoraadmin.databinding.FragmentProductBinding;
import com.udari.evoraadmin.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private List<ProductModel> productList;
    private ProductAdapter adapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);

        setupRecyclerView();

        // Add Product Button එකේ වැඩ
        binding.btnAddProduct.setOnClickListener(v -> {
            addNewProductLocally();
        });

        // Image Select Button (දැනට Toast එකක් පමණි)
        binding.btnSelectImage.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Image Selection Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();

        // Sample Data
        productList.add(new ProductModel("Premium Cement", 2450.00, 100, "https://example.com/image1.jpg"));
        productList.add(new ProductModel("Iron Rod 10mm", 1200.00, 50, "https://example.com/image2.jpg"));

        adapter = new ProductAdapter(productList);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProducts.setAdapter(adapter);
    }

    private void addNewProductLocally() {
        String name = binding.etProductName.getText().toString();
        String priceStr = binding.etProductPrice.getText().toString();
        String stockStr = binding.etProductStock.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        // ලිස්ට් එකට එකතු කිරීම (දැනට පින්තූරයක් නැති නිසා default එකක් දාමු)
        ProductModel newProduct = new ProductModel(name, price, stock, "");
        productList.add(0, newProduct); // උඩටම ඇඩ් කරන්න
        adapter.notifyItemInserted(0);
        binding.rvProducts.scrollToPosition(0);

        // Form එක Clear කිරීම
        binding.etProductName.setText("");
        binding.etProductPrice.setText("");
        binding.etProductStock.setText("");

        Toast.makeText(getContext(), "Product Added Locally!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}