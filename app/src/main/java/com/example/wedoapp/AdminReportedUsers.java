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

public class AdminReportedUsers extends AppCompatActivity {

    private TextView tv_NoPOrder;
    private ImageView iv_NoPOrder;

    private ImageButton PendingBackToOrderList;
    private RecyclerView recyclerViewReportedUsersList;
    private ArrayList<Model_ReportedUsers> ReportedUsersList;
    private AdapterReportedUsers adapterReportedUsers;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reported_users);

        tv_NoPOrder = findViewById(R.id.tv_NoPOrder);
        iv_NoPOrder = findViewById(R.id.iv_NoPOrder);
        PendingBackToOrderList = findViewById(R.id.PendingBackToOrderList);
        recyclerViewReportedUsersList = findViewById(R.id.recyclerViewReportedUsersList);

        auth = FirebaseAuth.getInstance();

        loadReportedUsers();

        PendingBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadReportedUsers() {
        ReportedUsersList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReportedUsers");
        databaseReference.orderByChild("ReportID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReportedUsersList.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_ReportedUsers model_reportedUsers = ds.getValue(Model_ReportedUsers.class);

                    ReportedUsersList.add(model_reportedUsers);
                }
                //setup adapter
                adapterReportedUsers = new AdapterReportedUsers(AdminReportedUsers.this, ReportedUsersList);
                //set adapter
                recyclerViewReportedUsersList.setAdapter(adapterReportedUsers);
                if(ReportedUsersList.isEmpty()){
                    tv_NoPOrder.setVisibility(View.VISIBLE);
                    iv_NoPOrder.setVisibility(View.VISIBLE);
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