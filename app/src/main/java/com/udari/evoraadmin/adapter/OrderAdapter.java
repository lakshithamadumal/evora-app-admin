package com.udari.evoraadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.udari.evoraadmin.R;
import com.udari.evoraadmin.model.OrderModel;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderModel> orderList;

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.name.setText(order.getCustomerName());
        holder.status.setText(order.getStatus());
        holder.total.setText("Total: Rs. " + order.getTotalAmount());

        // ItemsSummary එකක් හදමු
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> item : order.getItems()) {
                sb.append(item.get("productTitle")).append(", ");
            }
            holder.products.setText(sb.toString());
        } else {
            holder.products.setText("Date: " + order.getFormattedDate());
        }

        // Status Click -> Details Dialog
        holder.status.setOnClickListener(v -> showDetailsDialog(holder.itemView.getContext(), order));

        // Update Button
        holder.btnUpdate.setOnClickListener(v -> showStatusUpdateDialog(holder.itemView.getContext(), order, position));
    }

    private void showDetailsDialog(Context context, OrderModel order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_details, null);
        builder.setView(view);

        TextView date = view.findViewById(R.id.tvDetailDate);
        TextView phone = view.findViewById(R.id.tvDetailPhone);
        TextView city = view.findViewById(R.id.tvDetailCity);
        TextView address = view.findViewById(R.id.tvDetailAddress);
        Button close = view.findViewById(R.id.btnCloseDetail);

        // මෙතනත් getTimestamp() පාවිච්චි කරන්න
        date.setText("Date: " + order.getFormattedDate());
        phone.setText("Phone: " + order.getCustomerPhone());
        city.setText("City: " + order.getDeliveryCity());
        address.setText("Address: " + order.getDeliveryAddress());

        AlertDialog dialog = builder.create();
        close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showStatusUpdateDialog(Context context, OrderModel order, int position) {
        String[] options = {"Paid", "Processing", "Shipped", "Delivered", "Cancelled"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Order Status");
        builder.setItems(options, (dialog, which) -> {
            String selectedStatus = options[which];

            // Firestore Update Logic
            FirebaseFirestore.getInstance().collection("Orders")
                    .document(order.getOrderId()) // Document ID එක OrderId එකම විය යුතුයි
                    .update("status", selectedStatus)
                    .addOnSuccessListener(aVoid -> {
                        order.setStatus(selectedStatus);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Status Updated to " + selectedStatus, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Update Failed!", Toast.LENGTH_SHORT).show());
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, status, products, total;
        Button btnUpdate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvOrderUserName);
            status = itemView.findViewById(R.id.tvOrderStatus);
            products = itemView.findViewById(R.id.tvOrderProducts);
            total = itemView.findViewById(R.id.tvOrderTotal);
            btnUpdate = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}