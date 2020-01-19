package com.example.cabpool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Context context;
    List<Users> users;

    List<String> mData;

    public UsersAdapter(Context context, List<Users> users, List<String> mData) {
        this.context = context;
        this.users = users;
        this.mData = mData;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersAdapter.UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.users_cabpool_info_recycler_layout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.phoneNumber.setText(users.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView name , phoneNumber;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_field_usersRecyclerView);
            phoneNumber = itemView.findViewById(R.id.phone_field_usersRecyclerView);

        }
    }
}
