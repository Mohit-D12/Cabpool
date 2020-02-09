package com.example.cabpool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CabpoolFragment extends Fragment {

    private View cabpoolView;
    private FloatingActionButton addCabpoolFloatingButton;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;

    AutoCompleteTextView searchBar;

    List<Cabpools> cabpools = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();
    List<Cabpools> tempCabpools = new ArrayList<>();
    List<String> mTempDataKey = new ArrayList<>();
    List<String> autoComplete= new ArrayList<>();

    ProgressDialog progress;
    SwipeRefreshLayout swipeRefreshLayout;
    CabpoolAdapter adapter, tempAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        cabpoolView = inflater.inflate(R.layout.cabpool_fragment, container, false);

        addCabpoolFloatingButton = cabpoolView.findViewById(R.id.AddButton_Create);
        recyclerView = cabpoolView.findViewById(R.id.recyclerView_cabpool);
        swipeRefreshLayout = cabpoolView.findViewById(R.id.refresh_cabpools);

        searchBar = cabpoolView.findViewById(R.id.autoCompleteTextView_searchBar);

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

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progress.show();
                loadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ArrayAdapter<String> autocompleteArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,autoComplete);
        searchBar.setAdapter(autocompleteArrayAdapter);

            searchBar.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String term = searchBar.getText().toString().trim();
                    if (term.isEmpty()) {
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        tempCabpools.clear();
                        mTempDataKey.clear();
                        Iterator<Cabpools> itr = cabpools.iterator();
                        Iterator<String> itr2 = mDataKey.iterator();
                        while (itr.hasNext()) {
                            Cabpools cabpool = itr.next();
                            String datakey = itr2.next();

                            if (cabpool.getDate().substring(0, 2).contains(term)
                                    || cabpool.getFrom().toLowerCase().contains(term.toLowerCase())
                                    || cabpool.getTo().toLowerCase().contains(term.toLowerCase())){
                                tempCabpools.add(cabpool);
                                mTempDataKey.add(datakey);
                            }

                            /*
                            switch (field) {
                                case "Date":
                                    if (cabpool.getDate().substring(0, 2).contains(term)) {
                                        tempCabpools.add(cabpool);
                                        mTempDataKey.add(datakey);
                                    }
                                    break;
                                case "From":
                                    if (cabpool.getFrom().toLowerCase().contains(term.toLowerCase())){
                                        tempCabpools.add(cabpool);
                                        mTempDataKey.add(datakey);
                                    }
                                    break;
                                case "To":
                                    if (cabpool.getTo().toLowerCase().contains(term.toLowerCase())){
                                        tempCabpools.add(cabpool);
                                        mTempDataKey.add(datakey);
                                    }
                                    break;
                                default:
                                    if (cabpool.getDate().substring(0, 2).contains(term)
                                            || cabpool.getFrom().toLowerCase().contains(term.toLowerCase())
                                            || cabpool.getTo().toLowerCase().contains(term.toLowerCase())){
                                        tempCabpools.add(cabpool);
                                        mTempDataKey.add(datakey);
                                    }
                            }
                            */
                        }
                        recyclerView.setAdapter(tempAdapter);
                    }
                }
            });
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
                    mDataKey.add(single.getKey());
                    if(!autoComplete.contains(single.child("from").getValue().toString()))
                        autoComplete.add(single.child("from").getValue().toString());
                    if(!autoComplete.contains(single.child("to").getValue().toString()))
                        autoComplete.add(single.child("to").getValue().toString());
                    if(!autoComplete.contains(single.child("date").getValue().toString().substring(0,2)))
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
        tempAdapter = new CabpoolAdapter(getActivity(),tempCabpools,mTempDataKey);
        recyclerView.setAdapter(adapter);
    }
}
