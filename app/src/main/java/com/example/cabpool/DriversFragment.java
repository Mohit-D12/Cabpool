package com.example.cabpool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriversFragment extends Fragment {

    private View driversView;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;

    List<Drivers> drivers = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();

    ProgressDialog progress;

    DriversAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        driversView = inflater.inflate(R.layout.drivers_fragment, container, false);
        recyclerView = driversView.findViewById(R.id.recyclerView_drivers);

        return driversView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.driversView = view;
        init();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing");
        progress.setCancelable(false);
        progress.show();
        loadData();
    }

    private void loadData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drivers.clear();;
                mDataKey.clear();
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    Drivers driver = new Drivers(single.child("name").getValue().toString(),single.child("phone").getValue().toString(),single.child("vehicle").getValue().toString());
                    drivers.add(driver);
                    mDataKey.add(single.getKey().toString());
                }
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        recyclerView = driversView.findViewById(R.id.recyclerView_drivers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DriversAdapter(getActivity(),drivers,mDataKey);
        recyclerView.setAdapter(adapter);
    }
}
