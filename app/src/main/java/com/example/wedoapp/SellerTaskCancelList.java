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

public class SellerTaskCancelList extends AppCompatActivity {

    private TextView tv_NoCancelTask;
    private ImageView iv_NoCancelTask;
    private ImageButton CancelBackToTaskList;
    private ArrayList<Model_Order> TaskCancelledList;

    private RecyclerView recyclerViewSellerCancelList;
    private AdapterTaskCancelled adapterTaskCancelled;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_cancel_list);
        tv_NoCancelTask = findViewById(R.id.tv_NoCancelTask);
        iv_NoCancelTask = findViewById(R.id.iv_NoCancelTask);
        CancelBackToTaskList = findViewById(R.id.CancelBackToTaskList);
        recyclerViewSellerCancelList = findViewById(R.id.recyclerViewSellerCancelList);

        loadRatedOrders();

        CancelBackToTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent ratedBack2ordList = new Intent(BuyerOrderRatedList.this, Orders.class);
                //startActivity(ratedBack2ordList);
            }
        });
    }

    private void loadRatedOrders() {
        TaskCancelledList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("SellerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TaskCancelledList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all pending orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Cancelled")) {
                        TaskCancelledList.add(0, model_order);
                    }
                }
                //setup adapter
                adapterTaskCancelled = new AdapterTaskCancelled(SellerTaskCancelList.this, TaskCancelledList);
                //set adapter
                recyclerViewSellerCancelList.setAdapter(adapterTaskCancelled);

                if(TaskCancelledList.isEmpty()){
                    tv_NoCancelTask.setVisibility(View.VISIBLE);
                    iv_NoCancelTask.setVisibility(View.VISIBLE);
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