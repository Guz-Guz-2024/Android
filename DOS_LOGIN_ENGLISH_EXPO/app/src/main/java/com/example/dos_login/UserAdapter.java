package com.example.dos_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> userList;
    private final OnUserClickListener onUserClickListener;

    public UserAdapter(List<User> userList, OnUserClickListener onUserClickListener) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.tvRole.setText(user.getRole());
        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
