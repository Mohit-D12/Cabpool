package com.example.cabpool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriversViewHolder> {

    Context context;
    List<Drivers> drivers;
    List<String> mData;
    String phone;

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
    public void onBindViewHolder(@NonNull final DriversViewHolder holder, final int position) {

        holder.name.setText(drivers.get(position).getName());
        holder.vehicle.setText(drivers.get(position).getVehicle());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = drivers.get(position).getPhoneNumber();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+phone));
                holder.itemView.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public class DriversViewHolder extends RecyclerView.ViewHolder{

        TextView name, vehicle;
        ImageButton callButton;

        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_field_driversRecyclerView);
            vehicle = itemView.findViewById(R.id.vehicle_field_driversRecyclerView);
            callButton = itemView.findViewById(R.id.call_driversRecyclerView);
        }
    }
}
