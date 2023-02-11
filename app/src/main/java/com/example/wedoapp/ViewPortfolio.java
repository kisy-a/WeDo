package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPortfolio extends AppCompatActivity {

    private ImageView iv_NoDesignNotice;
    private TextView tv_NoDesignNotice;
    private ImageButton PortfolioBackToList;
    private FloatingActionButton FButtonAdd_Portfolio;

    private ArrayList<Model_PortfolioDesign> ServicePortfolioSampleList;
    private RecyclerView recyclerViewPortfolioDesignListed;
    private AdapterPortfolioDesignListed adapterPortfolioDesignListed;


    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String serviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_portfolio);

        iv_NoDesignNotice = findViewById(R.id.iv_NoDesignNotice);
        tv_NoDesignNotice = findViewById(R.id.tv_NoDesignNotice);
        PortfolioBackToList = findViewById(R.id.PortfolioBackToList);
        recyclerViewPortfolioDesignListed = findViewById(R.id.recyclerViewPortfolioDesignListed);
        FButtonAdd_Portfolio = findViewById(R.id.FButtonAdd_Portfolio);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");

        loadDesign();
        setupAdapter();

        PortfolioBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serListBack2Acc = new Intent(ViewPortfolio.this, ManageServiceList.class);
                startActivity(serListBack2Acc);
                //onBackPressed();
            }
        });

        FButtonAdd_Portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToAddDesign = new Intent (ViewPortfolio.this , AddSamplePortfolio.class);
                intentToAddDesign.putExtra("SID", serviceID);
                startActivity(intentToAddDesign);
            }
        });


        /*adapterPortfolioDesignListed.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapterPortfolioDesignListed.getItemCount() !=0){
                    tv_NoDesignNotice.setVisibility(View.GONE);
                   FButtonAdd_Portfolio.setVisibility(View.GONE);
                }
                else{
                    tv_NoDesignNotice.setVisibility(View.VISIBLE);
                   FButtonAdd_Portfolio.setVisibility(View.VISIBLE);
                }
            }
        });*/

    }

    private void setupAdapter() {
        recyclerViewPortfolioDesignListed.setHasFixedSize(true);
        recyclerViewPortfolioDesignListed.setLayoutManager(new LinearLayoutManager(this));
        //setup adapter
        adapterPortfolioDesignListed = new AdapterPortfolioDesignListed(ViewPortfolio.this, ServicePortfolioSampleList);
        //set adapter
        recyclerViewPortfolioDesignListed.setAdapter(adapterPortfolioDesignListed);
    }

    private void loadDesign() {
        ServicePortfolioSampleList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Portfolio");
        databaseReference.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServicePortfolioSampleList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Model_PortfolioDesign model_portfolioDesign = ds.getValue(Model_PortfolioDesign.class);
                        ServicePortfolioSampleList.add(model_portfolioDesign);
                        tv_NoDesignNotice.setVisibility(View.GONE);
                        iv_NoDesignNotice.setVisibility(View.GONE);
                        //FButtonAdd_Portfolio.setVisibility(View.GONE);

                    }

                    adapterPortfolioDesignListed.notifyDataSetChanged();

                }else{
                    tv_NoDesignNotice.setVisibility(View.VISIBLE);
                    iv_NoDesignNotice.setVisibility(View.VISIBLE);
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