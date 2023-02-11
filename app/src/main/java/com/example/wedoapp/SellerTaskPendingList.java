package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SellerTaskPendingList extends AppCompatActivity {

    private TextView tv_NoPTask;
    private ImageView iv_NoPTask;

    private ImageButton PendingBackToMyTask;
    private Button btn_viewPayment, btn_accepted;
    private ArrayList<Model_Order> SellerTaskPendingList;

    private RecyclerView recyclerViewSellerPendingList;
    private AdapterTaskPending adapterTaskPending;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_pending_list);

        iv_NoPTask = findViewById(R.id.iv_NoPTask);
        tv_NoPTask = findViewById(R.id.tv_NoPTask);

        PendingBackToMyTask = findViewById(R.id.PendingBackToMyTask);
        btn_viewPayment = findViewById(R.id.btn_viewPayment);
        btn_accepted = findViewById(R.id.btn_accepted);
        recyclerViewSellerPendingList = findViewById(R.id.recyclerViewSellerPendingList);

        loadPendingTasks();
        loadAdapter();

        //ArrayList<Model_Order> SellerTaskPendingList = new ArrayList<>();


        PendingBackToMyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent penBack2taskList = new Intent(SellerTaskPendingList.this, Tasks.class);
                //startActivity(penBack2taskList);
            }
        });


    }

    private void loadAdapter() {

        recyclerViewSellerPendingList.setHasFixedSize(true);
        recyclerViewSellerPendingList.setLayoutManager(new LinearLayoutManager(this));
        //setup adapter
        adapterTaskPending = new AdapterTaskPending(SellerTaskPendingList.this, SellerTaskPendingList);
        //set adapter
        recyclerViewSellerPendingList.setAdapter(adapterTaskPending);

    }

    private void loadPendingTasks() {
        SellerTaskPendingList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("SellerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SellerTaskPendingList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all pending orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Pending")) {
                        SellerTaskPendingList.add(0, model_order);
                    }
                }

                adapterTaskPending.notifyDataSetChanged();

                if(SellerTaskPendingList.isEmpty()){
                    tv_NoPTask.setVisibility(View.VISIBLE);
                    iv_NoPTask.setVisibility(View.VISIBLE);
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