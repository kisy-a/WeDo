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

public class SellerTaskAcceptedList extends AppCompatActivity {

    private TextView tv_NoATask;
    private ImageView iv_NoATask;

    private ImageButton InProgressBackToMyTask;
    private ArrayList<Model_Order> SellerTaskProgressingList;

    private RecyclerView recyclerViewSellerInProgressList;
    private AdapterTaskProgressing adapterTaskProgressing;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_accepted_list);

        iv_NoATask = findViewById(R.id.iv_NoATask);
        tv_NoATask = findViewById(R.id.tv_NoATask);

        InProgressBackToMyTask = findViewById(R.id.InProgressBackToMyTask);
        recyclerViewSellerInProgressList = findViewById(R.id.recyclerViewSellerInProgressList);

        loadAcceptedTasks();

        InProgressBackToMyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent acceptBack2taskList = new Intent(SellerTaskAcceptedList.this, Tasks.class);
                //startActivity(acceptBack2taskList);
            }
        });
    }

    private void loadAcceptedTasks() {
        SellerTaskProgressingList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("SellerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);

                    //show all progressing orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Progressing")) {
                        SellerTaskProgressingList.add(0, model_order);
                    }
                }
                //setup adapter
                adapterTaskProgressing = new AdapterTaskProgressing(SellerTaskAcceptedList.this, SellerTaskProgressingList);
                //set adapter
                recyclerViewSellerInProgressList.setAdapter(adapterTaskProgressing);

                if(SellerTaskProgressingList.isEmpty()){
                    tv_NoATask.setVisibility(View.VISIBLE);
                    iv_NoATask.setVisibility(View.VISIBLE);
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