package com.example.cabpool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UpcomingFragment extends Fragment {

    private View upcomingView;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference, cabpoolReference;
    SharedPreferences sharedPreferences;

    List<Cabpools> cabpools = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();

    ProgressDialog progress;
    CabpoolAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        upcomingView = inflater.inflate(R.layout.upcoming_fragment, container, false);
        recyclerView = upcomingView.findViewById(R.id.recyclerView_upcoming);
        return upcomingView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.upcomingView = view;
        init();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing");
        progress.setCancelable(false);
        progress.show();
        loadData();
    }
    private void loadData() {
        sharedPreferences = getActivity().getSharedPreferences("Users",MODE_PRIVATE);;
        String uid = sharedPreferences.getString("userId","defaultUser");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Cabpools");



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cabpools.clear();
                mDataKey.clear();
                for (DataSnapshot single : dataSnapshot.getChildren()) {
                    final String cabpoolId = single.getValue().toString();

                    cabpoolReference = FirebaseDatabase.getInstance().getReference().child("Cabpools").child(cabpoolId);

                    cabpoolReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String from=dataSnapshot.child("from").getValue().toString();
                            String to=dataSnapshot.child("to").getValue().toString();
                            String date=dataSnapshot.child("date").getValue().toString();
                            String time=dataSnapshot.child("time").getValue().toString();

                            Cabpools cabpool = new Cabpools(from,to,date,time);
                            cabpools.add(cabpool);
                            mDataKey.add(cabpoolId);

                            adapter.notifyDataSetChanged();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        recyclerView = upcomingView.findViewById(R.id.recyclerView_upcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CabpoolAdapter(getActivity(),cabpools,mDataKey);
        recyclerView.setAdapter(adapter);
    }

}

