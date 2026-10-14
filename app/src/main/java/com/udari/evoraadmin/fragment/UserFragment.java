package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.udari.evoraadmin.adapter.UserAdapter;
import com.udari.evoraadmin.databinding.FragmentUserBinding;
import com.udari.evoraadmin.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserAdapter adapter;
    private List<UserModel> userList;
    private FirebaseFirestore db;
    private ListenerRegistration usersListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadUsersFromFirestore();
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsers.setAdapter(adapter);
    }

    private void loadUsersFromFirestore() {
        usersListener = db.collection("Users").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("FirestoreError", error.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (value != null && binding != null) {
                userList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    UserModel user = doc.toObject(UserModel.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (usersListener != null) {
            usersListener.remove();
        }
        binding = null;
    }
}