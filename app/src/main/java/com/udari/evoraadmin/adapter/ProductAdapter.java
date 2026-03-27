package com.udari.evoraadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore; // මේක ඇඩ් කළා
import com.udari.evoraadmin.R;
import com.udari.evoraadmin.model.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> productList;

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.tvTitle.setText(product.getTitle());

        // Crash වෙන්නේ නැති වෙන්න සරලව Price එක පෙන්වමු
        holder.tvPrice.setText("Rs. " + product.getPrice());

        // Model එකේ අලුත් නම stockCount නිසා ඒක පාවිච්චි කළා
        holder.tvStock.setText("Stock: " + product.getStockCount());

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.add)
                .error(R.drawable.add)
                .into(holder.ivProduct);

        holder.btnEdit.setOnClickListener(v -> {
            showUpdateStockDialog(holder.itemView.getContext(), product, position);
        });
    }

    private void showUpdateStockDialog(Context context, ProductModel product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_stock, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView tvCurrent = view.findViewById(R.id.tvCurrentStock);
        EditText etNew = view.findViewById(R.id.etNewStock);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        tvCurrent.setText("Current Stock: " + product.getStockCount());
        etNew.setText(String.valueOf(product.getStockCount()));

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            String newStockStr = etNew.getText().toString().trim();
            if (!newStockStr.isEmpty()) {
                int newStock = Integer.parseInt(newStockStr);

                // 1. Firestore එක Update කිරීම (Real-time)
                FirebaseFirestore.getInstance().collection("Products")
                        .document(product.getProductId()) // Model එකේ අලුත් නම productId
                        .update("stockCount", newStock) // Database එකේ Key එක stockCount
                        .addOnSuccessListener(aVoid -> {
                            // 2. Local Update (Screen එකේ පෙන්වන්න)
                            product.setStockCount(newStock);
                            notifyItemChanged(position);
                            Toast.makeText(context, "Stock Updated in Cloud! ✅", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Update Failed! ❌", Toast.LENGTH_SHORT).show();
                        });

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvStock;
        ImageView ivProduct;
        ImageButton btnEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvProductTitle);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
        }
    }
}