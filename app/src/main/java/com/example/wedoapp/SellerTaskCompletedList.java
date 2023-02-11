package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerTaskCompletedList extends AppCompatActivity {

    private TextView tv_NoCTask;
    private ImageView iv_NoCTask;

    private ImageButton CompletedBackToMyTask;
    private ArrayList<Model_Order> SellerTaskCompletedList;

    private RecyclerView recyclerViewSellerCompletedList;
    private AdapterTaskCompleted adapterTaskCompleted;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_completed_list);

        iv_NoCTask = findViewById(R.id.iv_NoCTask);
        tv_NoCTask = findViewById(R.id.tv_NoCTask);

        CompletedBackToMyTask = findViewById(R.id.CompletedBackToMyTask);
        recyclerViewSellerCompletedList = findViewById(R.id.recyclerViewSellerCompletedList);

        loadCompletedTasks();

        CompletedBackToMyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent pendBack2taskList = new Intent(SellerTaskCompletedList.this, Tasks.class);
                //startActivity(pendBack2taskList);
            }
        });

    }

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}