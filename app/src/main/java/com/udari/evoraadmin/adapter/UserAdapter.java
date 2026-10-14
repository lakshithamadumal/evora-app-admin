package com.udari.evoraadmin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udari.evoraadmin.R;
import com.udari.evoraadmin.model.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserModel> userList;

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);

        if (user != null) {
            holder.name.setText(user.getName() != null ? user.getName() : "N/A");

            String phone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A";
            holder.phoneNumber.setText("Phone: " + phone);

            String city = user.getCity() != null ? user.getCity() : "N/A";
            holder.city.setText("City: " + city);

            holder.orders.setText("Orders: " + user.getOrderCount());
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, phoneNumber, city, orders;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvUserName);
            phoneNumber = itemView.findViewById(R.id.tvUserMobile);
            city = itemView.findViewById(R.id.tvUserCity);
            orders = itemView.findViewById(R.id.tvOrderCount);
        }
    }
}