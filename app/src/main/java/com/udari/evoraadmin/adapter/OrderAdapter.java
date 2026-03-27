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
import com.udari.evoraadmin.R;
import com.udari.evoraadmin.model.OrderModel;
import java.util.List;

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
        holder.name.setText(order.userName);
        holder.status.setText(order.status);
        holder.products.setText(order.productsSummary);
        holder.total.setText(String.format("Total: Rs. %.2f", order.total));

        // Details පෙන්වීම
        holder.itemView.setOnClickListener(v -> showDetailsDialog(holder.itemView.getContext(), order));

        // Status Update කිරීම
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

        date.setText("Date: " + order.date);
        phone.setText("Phone: " + order.phone);
        city.setText("City: " + order.city);
        address.setText("Address: " + order.address);

        AlertDialog dialog = builder.create();
        close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showStatusUpdateDialog(Context context, OrderModel order, int position) {
        String[] options = {"Paid", "Processing", "Delivered", "Received"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Order Status");
        builder.setItems(options, (dialog, which) -> {
            order.status = options[which];
            notifyItemChanged(position);
            // TODO: Firestore update logic
            Toast.makeText(context, "Status Updated to " + options[which], Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    @Override
    public int getItemCount() { return orderList.size(); }

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