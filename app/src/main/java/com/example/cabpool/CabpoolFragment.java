package com.example.cabpool;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class CabpoolFragment extends Fragment {

    private View cabpoolView;
    private FloatingActionButton addCabpoolFloatingButton;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;


    List<Cabpools> cabpools = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();
    List<String> autoComplete= new ArrayList<>();

    ProgressDialog progress;

    CabpoolAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cabpoolView = inflater.inflate(R.layout.cabpool_fragment, container, false);
        addCabpoolFloatingButton = cabpoolView.findViewById(R.id.AddButton_Create);
        recyclerView = cabpoolView.findViewById(R.id.recyclerView_cabpool);

        addCabpoolFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CreateCabpool.class));
            }
        });

        return cabpoolView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.cabpoolView = view;
        init();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing");
        progress.setCancelable(false);
        progress.show();
        loadData();
    }

    private void loadData() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cabpools");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cabpools.clear();
                mDataKey.clear();
                autoComplete.clear();
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    Cabpools cabpool = new Cabpools(single.child("from").getValue().toString(),single.child("to").getValue().toString(),single.child("date").getValue().toString(),single.child("time").getValue().toString());
                    cabpools.add(cabpool);
                    // cabpools.add(single.getValue(Cabpools.class));
                    mDataKey.add(single.getKey().toString());
                    autoComplete.add(single.child("from").getValue().toString());
                    autoComplete.add(single.child("to").getValue().toString());
                    autoComplete.add(single.child("date").getValue().toString().substring(0,2));
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
        recyclerView = cabpoolView.findViewById(R.id.recyclerView_cabpool);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CabpoolAdapter(getActivity(),cabpools,mDataKey);
        recyclerView.setAdapter(adapter);
    }

}
