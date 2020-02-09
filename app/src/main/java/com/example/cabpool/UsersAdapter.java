package com.example.cabpool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Context context;
    List<Users> users;
    List<String> mData;
    String phoneNumber;

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
    public void onBindViewHolder(@NonNull final UsersViewHolder holder, final int position) {

        holder.name.setText(users.get(position).getName());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = users.get(position).getPhoneNumber();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                holder.itemView.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView name ;
        ImageButton callButton;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_field_usersRecyclerView);
            callButton = itemView.findViewById(R.id.call_usersRecyclerView);

        }
    }
}
