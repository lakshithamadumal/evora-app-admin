package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.udari.evoraadmin.adapter.UserAdapter;
import com.udari.evoraadmin.databinding.FragmentUserBinding;
import com.udari.evoraadmin.model.UserModel;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserAdapter adapter;
    private List<UserModel> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        setupRecyclerView();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();

        // Sample Data ටිකක් ඇතුළත් කිරීම (පස්සේ මේවා Firebase එකෙන් ගමු)
        userList.add(new UserModel("Laky", "0712654117", "Gampaha", 5));
        userList.add(new UserModel("Kasun Perera", "0771234567", "Colombo", 2));
        userList.add(new UserModel("Dilini Silva", "0759876543", "Kandy", 10));
        userList.add(new UserModel("Amara", "0701112233", "Galle", 0));

        // Adapter එක සෙට් කිරීම
        adapter = new UserAdapter(userList);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvUsers.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}