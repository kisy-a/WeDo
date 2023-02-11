package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class ManageServiceList extends AppCompatActivity {

    private TextView tv_NoServicePosted;
    private ImageView iv_NoServicePosted;

    private ImageButton ListBackToSellerAcc;
    private ArrayList<Model_Service> ManageServiceList;
    private RecyclerView recyclerViewManageService;
    private AdapterManageService adapterManageService;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service_list);

        tv_NoServicePosted = findViewById(R.id.tv_NoServicePosted);
        iv_NoServicePosted = findViewById(R.id.iv_NoServicePosted);

        ListBackToSellerAcc = findViewById(R.id.ListBackToSellerAcc);
        recyclerViewManageService = findViewById(R.id.recyclerViewManageService);
        
        auth = FirebaseAuth.getInstance();

        loadAllServices();

        ListBackToSellerAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent serListBack2Acc = new Intent(ManageServiceList.this, SellerAccount.class);
                startActivity(serListBack2Acc);
            }
        });
    }

    private void loadAllServices() {
        ManageServiceList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("UID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ManageServiceList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);
                    ManageServiceList.add(model_service);
                }

                //setup adapter
                adapterManageService = new AdapterManageService(ManageServiceList.this, ManageServiceList);
                //set adapter
                recyclerViewManageService.setAdapter(adapterManageService);
                adapterManageService.notifyDataSetChanged();

                if(ManageServiceList.isEmpty()){
                    tv_NoServicePosted.setVisibility(View.VISIBLE);
                    iv_NoServicePosted.setVisibility(View.VISIBLE);
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