package com.example.cabpool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriversViewHolder> {
    Context context;
    List<Drivers> drivers;

    List<String> mData;

    public DriversAdapter(Context context, List<Drivers> drivers, List<String> mData) {
        this.context = context;
        this.drivers = drivers;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DriversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DriversViewHolder(LayoutInflater.from(context).inflate(R.layout.drivers_recycler_layout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewHolder holder, int position) {
        holder.name.setText(drivers.get(position).getName());
        holder.phoneNumber.setText(drivers.get(position).getPhoneNumber());
        holder.vehicle.setText(drivers.get(position).getVehicle());
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public class DriversViewHolder extends RecyclerView.ViewHolder{

        TextView name , phoneNumber, vehicle;

        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_field_driversRecyclerView);
            phoneNumber = itemView.findViewById(R.id.phone_field_driversRecyclerView);
            vehicle = itemView.findViewById(R.id.vehicle_field_driversRecyclerView);
        }
    }
}
