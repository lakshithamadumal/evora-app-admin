package com.udari.evoraadmin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.udari.evoraadmin.databinding.ActivityLogInBinding;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        // Local Data Store කරන්න SharedPreferences සෙට් කිරීම
        sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);

        // පලවෙනි පියවර: දැනටමත් ලොග් වෙලාද කියලා බලනවා
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            goToDashboard();
        }

        binding.btnLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        // Firestore වල 'Admin' collection එකේ දත්ත චෙක් කිරීම
        db.collection("Admin")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // ලොගින් එක සාර්ථකයි!
                        saveAdminSession(email);
                        goToDashboard();
                    } else {
                        // වැරදි විස්තර
                        Toast.makeText(this, "Invalid Admin Credentials!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveAdminSession(String email) {
        // ඇඩ්මින්ව මතක තබා ගැනීමට දත්ත සේව් කිරීම
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("adminEmail", email);
        editor.apply();
    }

    private void goToDashboard() {
        startActivity(new Intent(LogInActivity.this,MainActivity.class));
        finish();
    }
}