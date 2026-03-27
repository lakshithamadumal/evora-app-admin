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
        holder.tvPrice.setText(String.format("Rs. %.2f", product.getPrice()));
        holder.tvStock.setText("Stock: " + product.getStock());

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.add)
                .into(holder.ivProduct);

        // Stock Update Dialog එක පෙන්වීම
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

        tvCurrent.setText("Current Stock: " + product.getStock());
        etNew.setText(String.valueOf(product.getStock()));

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            String newStockStr = etNew.getText().toString();
            if (!newStockStr.isEmpty()) {
                int newStock = Integer.parseInt(newStockStr);

                // Local update
                product.setStock(newStock);
                notifyItemChanged(position);

                // TODO: Firestore Update Logic here

                dialog.dismiss();
                Toast.makeText(context, "Stock Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
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