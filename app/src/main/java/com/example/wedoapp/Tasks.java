package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tasks extends AppCompatActivity {

    private Button btn_taskPending, btn_taskProgress, btn_taskCompleted, btn_taskCancel;
    private ImageButton btn_tSeller_message;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        btn_taskCancel = findViewById(R.id.btn_taskCancel);
        btn_taskPending = findViewById(R.id.btn_taskPending);
        btn_taskProgress = findViewById(R.id.btn_taskProgress);
        btn_taskCompleted = findViewById(R.id.btn_taskCompleted);

        Integer total = 0;




        BottomNavigationView sellerNV = findViewById(R.id.sellerNavigation);

        //set Tasks Selected
        sellerNV.setSelectedItemId(R.id.sellerTask);
        sellerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerHome:
                        startActivity(new Intent(getApplicationContext(), BrowseJobList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerTask:
                        return true;

                    case R.id.sellerMessage:
                        startActivity(new Intent(getApplicationContext(), SellerChatMessage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerAccount:
                        startActivity(new Intent(getApplicationContext(), SellerAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        btn_taskPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskPendingLs = new Intent(Tasks.this, SellerTaskPendingList.class);
                startActivity(intent2taskPendingLs);
            }
        });

        btn_taskProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskProgressLs = new Intent(Tasks.this, SellerTaskAcceptedList.class);
                startActivity(intent2taskProgressLs);
            }
        });

        btn_taskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskCompleteLs = new Intent(Tasks.this, SellerTaskCompletedList.class);
                startActivity(intent2taskCompleteLs);
            }
        });

        btn_taskCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskCancelLs = new Intent(Tasks.this, SellerTaskCancelList.class);
                startActivity(intent2taskCancelLs);
            }
        });
    }
    /*
    private void loadCompletedTasks() {
        SellerTaskCompletedList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("SellerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);

                    //show all progressing orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Completed")) {
                        SellerTaskCompletedList.add(0, model_order);
                    }
                    else if(oStatus.equals("Rated")) {
                        SellerTaskCompletedList.add(0, model_order);
                    }
                }
                //setup adapter
                adapterTaskCompleted = new AdapterTaskCompleted(SellerTaskCompletedList.this, SellerTaskCompletedList);
                //set adapter
                recyclerViewSellerCompletedList.setAdapter(adapterTaskCompleted);

                if(SellerTaskCompletedList.isEmpty()){
                    tv_NoCTask.setVisibility(View.VISIBLE);
                    iv_NoCTask.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
     */
}