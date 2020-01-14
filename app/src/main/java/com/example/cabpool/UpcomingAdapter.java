package com.example.cabpool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    Context context;
    List<Cabpools> cabpools;

    List<String> mData;

    public UpcomingAdapter(Context context, List<Cabpools> cabpools, List<String> mData) {
        this.context = context;
        this.cabpools = cabpools;
        this.mData = mData;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpcomingViewHolder(LayoutInflater.from(context).inflate(R.layout.cabpool_recycler_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        holder.from.setText(cabpools.get(position).getFrom());
        holder.to.setText(cabpools.get(position).getTo());
        holder.date.setText(cabpools.get(position).getDate());
        holder.time.setText(cabpools.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return cabpools.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder{

        TextView from, to, date, time;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);

            from = itemView.findViewById(R.id.from_field_cabpoolsRecyclerView);
            to = itemView.findViewById(R.id.to_field_cabpoolsRecyclerView);
            date = itemView.findViewById(R.id.date_field_cabpoolsRecyclerView);
            time = itemView.findViewById(R.id.time_field_cabpoolsRecyclerView);
        }
    }
}
