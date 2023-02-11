package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BrowseJobList extends AppCompatActivity {

    private RecyclerView recyclerViewBrowseJob;
    private AdapterBrowseJob adapterBrowseJob;
    private ArrayList<Model_JobPosting> BrowseJobList;
    private ImageButton btn_seller_message;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_job_list);

        btn_seller_message = findViewById(R.id.btn_seller_message);
        recyclerViewBrowseJob = findViewById(R.id.recyclerViewBrowseJob);
        BottomNavigationView sellerNV = findViewById(R.id.sellerNavigation);

        btn_seller_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SellerHomeIntentToChat = new Intent(BrowseJobList.this, SellerChatMessage.class);
                startActivity(SellerHomeIntentToChat);
            }
        });

        //set Seller Home Selected
        sellerNV.setSelectedItemId(R.id.sellerHome);
        sellerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerHome:
                        return true;

                    case R.id.sellerTask:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
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

        browseAllProject();
    }

    private void browseAllProject() {

        BrowseJobList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        //final String timestamp = "" + System.currentTimeMillis();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.orderByChild("PStatus").equalTo("Open").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_JobPosting model_jobPosting = ds.getValue(Model_JobPosting.class);

                    //show all job list except current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        BrowseJobList.add(model_jobPosting);
                    }
                }
                //setup adapter
                adapterBrowseJob = new AdapterBrowseJob(BrowseJobList.this, BrowseJobList);
                //set adapter
                recyclerViewBrowseJob.setAdapter(adapterBrowseJob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}