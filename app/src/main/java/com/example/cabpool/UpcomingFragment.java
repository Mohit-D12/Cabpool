package com.example.cabpool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    TextView defaultDisplayView;
    DatabaseReference databaseReference, cabpoolReference;
    SharedPreferences sharedPreferences;

    List<Cabpools> cabpools = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();

    ProgressDialog progress;
    CabpoolAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        upcomingView = inflater.inflate(R.layout.upcoming_fragment, container, false);
        recyclerView = upcomingView.findViewById(R.id.recyclerView_upcoming);
        swipeRefreshLayout = upcomingView.findViewById(R.id.refresh_upcoming);
        defaultDisplayView = upcomingView.findViewById(R.id.defaultTextView_upcoming);
        defaultDisplayView.setVisibility(View.VISIBLE);
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

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progress.show();
                loadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                    final String cabpoolId = single.getKey();
                    defaultDisplayView.setVisibility(View.GONE);

                    cabpoolReference = FirebaseDatabase.getInstance().getReference().child("Cabpools");
                    cabpoolReference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.hasChild(cabpoolId))
                                databaseReference.child(cabpoolId).setValue(null);
                            else {
                                String from = dataSnapshot.child(cabpoolId).child("from").getValue().toString();
                                String to = dataSnapshot.child(cabpoolId).child("to").getValue().toString();
                                String date = dataSnapshot.child(cabpoolId).child("date").getValue().toString();
                                String time = dataSnapshot.child(cabpoolId).child("time").getValue().toString();


                                Cabpools cabpool = new Cabpools(from, to, date, time);
                                cabpools.add(cabpool);
                                mDataKey.add(cabpoolId);

                            }
                            cabpools = SortCabpool.sortCabpool(cabpools,mDataKey);
                            mDataKey = SortCabpool.sortKey(cabpools,mDataKey);
                            adapter.notifyDataSetChanged();
                            progress.dismiss();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(cabpools.isEmpty())
            progress.dismiss();
    }

    private void init() {
        recyclerView = upcomingView.findViewById(R.id.recyclerView_upcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CabpoolAdapter(getActivity(),cabpools,mDataKey);
        recyclerView.setAdapter(adapter);
    }

}

