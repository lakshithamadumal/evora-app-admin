package com.udari.evoraadmin.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udari.evoraadmin.adapter.ProductAdapter;
import com.udari.evoraadmin.databinding.FragmentProductBinding;
import com.udari.evoraadmin.model.ProductModel;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private List<ProductModel> productList;
    private ProductAdapter adapter;
    private FirebaseFirestore db;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private final String IMGBB_API_KEY = "5cf3fbf76d4e5552e63ff0b168bcca79";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving Product...");
        progressDialog.setCancelable(false);

        setupRecyclerView();
        loadProductsFromFirestore();

        binding.btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        });

        binding.btnAddProduct.setOnClickListener(v -> {
            if (validateFields()) {
                if (imageUri != null) {
                    if (!progressDialog.isShowing()) progressDialog.show();
                    uploadImageToImgBB(imageUri);
                } else {
                    Toast.makeText(getContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    private boolean validateFields() {
        if (binding.etProductName.getText().toString().trim().isEmpty() ||
                binding.etProductPrice.getText().toString().trim().isEmpty() ||
                binding.etProductStock.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Fill required fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private final ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.btnSelectImage.setText("Image Selected ✅");
                }
            }
    );

    private void uploadImageToImgBB(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", base64Image)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload?key=" + IMGBB_API_KEY)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    closeDialog();
                    showToast("Upload Failed!");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String url = jsonObject.getJSONObject("data").getString("url");
                            if (isAdded()) requireActivity().runOnUiThread(() -> saveProductToFirestore(url));
                        } catch (Exception e) { closeDialog(); }
                    } else { closeDialog(); }
                }
            });
        } catch (Exception e) { closeDialog(); }
    }

    private void saveProductToFirestore(String uploadedImageUrl) {
        try {
            String title = binding.etProductName.getText().toString().trim();
            String description = binding.etProductDescription.getText().toString().trim();
            double price = Double.parseDouble(binding.etProductPrice.getText().toString().trim());
            int stockCount = Integer.parseInt(binding.etProductStock.getText().toString().trim());

            db.collection("Products").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && isAdded()) {
                    String customId = "P" + (task.getResult().size() + 1);

                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", customId);
                    product.put("title", title);
                    product.put("description", description);
                    product.put("price", price);
                    product.put("stockCount", stockCount);
                    product.put("status", true);
                    product.put("imageUrl", uploadedImageUrl);

                    db.collection("Products").document(customId).set(product)
                            .addOnSuccessListener(aVoid -> {
                                closeDialog();
                                showToast("Added: " + customId);
                                clearFields();
                                loadProductsFromFirestore(); // මෙතනදී තමයි crash වෙන්න ඉඩ තියෙන්නේ
                            })
                            .addOnFailureListener(e -> closeDialog());
                } else { closeDialog(); }
            });
        } catch (Exception e) { closeDialog(); }
    }

    private void loadProductsFromFirestore() {
        db.collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (isAdded() && binding != null) {
                productList.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    try {
                        // Crash එක වළක්වන්න ආරක්ෂිතව දත්ත ගමු
                        ProductModel product = doc.toObject(ProductModel.class);
                        if (product != null) productList.add(product);
                    } catch (Exception e) {
                        Log.e("FirestoreError", "Data mismatch in doc: " + doc.getId());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Load failed"));
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProducts.setAdapter(adapter);
    }

    private void clearFields() {
        if (binding == null) return;
        binding.etProductName.setText("");
        binding.etProductDescription.setText("");
        binding.etProductPrice.setText("");
        binding.etProductStock.setText("");
        binding.btnSelectImage.setText("Select Image");
        imageUri = null;
    }

    private void closeDialog() {
        if (isAdded()) requireActivity().runOnUiThread(() -> {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        });
    }

    private void showToast(String msg) {
        if (isAdded()) requireActivity().runOnUiThread(() ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}