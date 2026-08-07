package com.udari.evoraadmin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();
        setupRecyclerView();
        loadUsersFromFirestore();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvUsers.setAdapter(adapter);
    }

    private void loadUsersFromFirestore() {
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                userList.clear();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    UserModel user = doc.toObject(UserModel.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown Error";
                Log.e("FirestoreError", error);
                Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}