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

    private List<UserModel> userList;

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
        holder.name.setText(user.getName());
        holder.phoneNumber.setText("phoneNumber: " + user.getPhoneNumber());
        holder.city.setText("City: " + user.getCity());
        holder.orders.setText("Orders: " + user.getOrderCount());
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