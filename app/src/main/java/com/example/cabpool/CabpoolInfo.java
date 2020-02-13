package com.example.cabpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CabpoolInfo extends AppCompatActivity {

    TextView from_cabpoolInfo_java, to_cabpoolInfo_java, date_cabpoolInfo_java, time_cabpoolInfo_java;
    Button joinButton_cabpoolInfo_java;

    String cabpoolId, uid, userId;
    SharedPreferences sharedPreferences;
    List<Users> users = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();

    RecyclerView recyclerView;
    UsersAdapter adapter;
    DatabaseReference cabpoolReference,usersReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabpool_info);

        init();
        loadData();

        joinButton_cabpoolInfo_java.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 usersReference.child(uid).child("Cabpools").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         if (joinButton_cabpoolInfo_java.getText().toString().equalsIgnoreCase("Join"))
                             joinCabpool();
                         else
                             leavecabpool();
                     }
                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) { }
                 });
                 }
         });
    }

    private void init() {
        from_cabpoolInfo_java = findViewById(R.id.from_cabpoolInfo_xml);
        to_cabpoolInfo_java = findViewById(R.id.to_cabpoolInfo_xml);
        date_cabpoolInfo_java = findViewById(R.id.date_cabpoolInfo_xml);
        time_cabpoolInfo_java = findViewById(R.id.time_cabpoolInfo_xml);
        joinButton_cabpoolInfo_java = findViewById(R.id.joinButton_cabpoolInfo_xml);

        recyclerView = findViewById(R.id.recyclerView_users_cabpoolInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new UsersAdapter(getApplicationContext(),users,mDataKey);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        cabpoolId = intent.getStringExtra("CabpoolId");

        sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        uid = sharedPreferences.getString("userId","defaultUser");

        cabpoolReference = FirebaseDatabase.getInstance().getReference().child("Cabpools").child(cabpoolId);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    void joinCabpool(){
        cabpoolReference.child("Users").child(uid).setValue(uid);
        usersReference.child(uid).child("Cabpools").child(cabpoolId).setValue(cabpoolId);
        Toast.makeText(getApplicationContext(), "Cabpool joined successfully", Toast.LENGTH_SHORT).show();
        joinButton_cabpoolInfo_java.setText("LEAVE");
        startActivity(new Intent(getApplicationContext(), mainApp_activity.class));
        loadData();
    }

    private void leavecabpool() {
        cabpoolReference.child("Users").child(uid).setValue(null);
        usersReference.child(uid).child("Cabpools").child(cabpoolId).setValue(null);
        Toast.makeText(getApplicationContext(), "Left Cabpool successfully", Toast.LENGTH_SHORT).show();
        if (cabpoolReference.child("Users") != null)
            joinButton_cabpoolInfo_java.setText("JOIN");
        startActivity(new Intent(getApplicationContext(), mainApp_activity.class));
        //loadData();

    }

    private void loadData() {

        cabpoolReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();;
                mDataKey.clear();
                if(!dataSnapshot.hasChildren()) return;
                from_cabpoolInfo_java.setText(dataSnapshot.child("from").getValue().toString());
                to_cabpoolInfo_java.setText(dataSnapshot.child("to").getValue().toString());
                date_cabpoolInfo_java.setText(dataSnapshot.child("date").getValue().toString());
                time_cabpoolInfo_java.setText(dataSnapshot.child("time").getValue().toString());

                cabpoolReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot single:dataSnapshot.getChildren()){
                            userId=single.getKey();
                            usersReference.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Users user = new Users(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("phone").getValue().toString());
                                    users.add(user);
                                    mDataKey.add(dataSnapshot.getKey());
                                    if(dataSnapshot.getKey().equalsIgnoreCase(uid))
                                        joinButton_cabpoolInfo_java.setText("Leave");
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
