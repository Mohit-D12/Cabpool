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
    DatabaseReference cabpoolReference,usersReference;
    String cabpoolId, uid, userId;
    int flag=1;
    SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    List<Users> users = new ArrayList<>();
    List<String> mDataKey = new ArrayList<>();

    ProgressDialog progress;

    UsersAdapter adapter;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabpool_info);

        from_cabpoolInfo_java = findViewById(R.id.from_cabpoolInfo_xml);
        to_cabpoolInfo_java = findViewById(R.id.to_cabpoolInfo_xml);
        date_cabpoolInfo_java = findViewById(R.id.date_cabpoolInfo_xml);
        time_cabpoolInfo_java = findViewById(R.id.time_cabpoolInfo_xml);
        joinButton_cabpoolInfo_java = findViewById(R.id.joinButton_cabpoolInfo_xml);

        init();

        Intent intent = getIntent();
        cabpoolId = intent.getStringExtra("CabpoolId");

        sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        uid = sharedPreferences.getString("userId","defaultUser");


        cabpoolReference = FirebaseDatabase.getInstance().getReference().child("Cabpools").child(cabpoolId);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        cabpoolReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();;
                mDataKey.clear();

                    from_cabpoolInfo_java.setText(dataSnapshot.child("from").getValue().toString());
                    to_cabpoolInfo_java.setText(dataSnapshot.child("to").getValue().toString());
                    date_cabpoolInfo_java.setText(dataSnapshot.child("date").getValue().toString());
                    time_cabpoolInfo_java.setText(dataSnapshot.child("time").getValue().toString());

                    cabpoolReference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot single:dataSnapshot.getChildren()){

                                userId=single.getValue().toString();

                                usersReference.child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Users user = new Users(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("phone").getValue().toString());
                                        users.add(user);
                                        mDataKey.add(dataSnapshot.getKey());
                                        adapter.notifyDataSetChanged();
                                        System.out.println(dataSnapshot.child("name").getValue().toString()+dataSnapshot.child("phone").getValue().toString());

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                adapter.notifyDataSetChanged();


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         joinButton_cabpoolInfo_java.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 usersReference.child(uid).child("Cabpools").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         System.out.println("Inside OnDataChange");

                         if (!dataSnapshot.hasChildren())
                         {
                             cabpoolReference.child("Users").push().setValue(uid);
                             usersReference.child(uid).child("Cabpools").push().setValue(cabpoolId);
                             Toast.makeText(getApplicationContext(), "Cabpool joined successfully", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), mainApp_activity.class));
                         }

                         for(DataSnapshot single:dataSnapshot.getChildren()) {
                             System.out.println("Inside DataSnashot");
                             flag=0;
                             if (single.getValue().equals(cabpoolId)) {
                                 flag=1;
                                 Toast.makeText(getApplicationContext(), "You are already a part of this cabpool", Toast.LENGTH_SHORT).show();
                                 break;
                             }
                         }
                         if (flag==0) {
                             cabpoolReference.child("Users").push().setValue(uid);
                             usersReference.child("uid").child("Cabpools").push().setValue(cabpoolId);
                             Toast.makeText(getApplicationContext(), "Cabpool joined successfully", Toast.LENGTH_SHORT).show();
                             flag=1;
                             startActivity(new Intent(getApplicationContext(), mainApp_activity.class));
                         }


                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
                 }


         });
    }

    private void init() {
            recyclerView = findViewById(R.id.recyclerView_users_cabpoolInfo);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new UsersAdapter(getApplicationContext(),users,mDataKey);
            recyclerView.setAdapter(adapter);
        }

}
