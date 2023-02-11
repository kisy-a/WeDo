package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Orders extends AppCompatActivity {

    private TextView pendingCount, progressCount, completeCount, RatedCount, CancelCount;
    private Button btn_ordPending, btn_ordProgress, btn_ordCompleted, btn_ordRated, btn_ordCancelled;
    private ImageButton btn_buyerO_message;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String OStatus, BuyerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        pendingCount = findViewById(R.id.pendingCount);
        progressCount = findViewById(R.id.progressCount);
        completeCount = findViewById(R.id.completeCount);
        RatedCount = findViewById(R.id.RatedCount);
        CancelCount = findViewById(R.id.CancelCount);

        btn_ordPending = findViewById(R.id.btn_ordPending);
        btn_ordProgress = findViewById(R.id.btn_ordProgress);
        btn_ordCompleted = findViewById(R.id.btn_ordCompleted);
        btn_ordRated = findViewById(R.id.btn_ordRated);
        btn_ordCancelled = findViewById(R.id.btn_ordCancelled);

        BottomNavigationView buyerNV = findViewById(R.id.buyerNavigation);

        //set Order Selected
        buyerNV.setSelectedItemId(R.id.buyerOrder);
        buyerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buyerHome:
                        startActivity(new Intent(getApplicationContext(), BuyerHome.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerOrder:
                        return true;

                    case R.id.buyerMessage:
                        startActivity(new Intent(getApplicationContext(), ChatMessage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerAccount:
                        startActivity(new Intent(getApplicationContext(), BuyerAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        /*btn_buyerO_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent OrderIntentToChat = new Intent(Orders.this, ChatMessage.class);
                startActivity(OrderIntentToChat);
            }
        });
        */
        btn_ordPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordPendingLs = new Intent(Orders.this, BuyerOrderPendingList.class);
                startActivity(intent2ordPendingLs);
            }
        });

        btn_ordProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordProgressLs = new Intent(Orders.this, BuyerOrderInProgressList.class);
                startActivity(intent2ordProgressLs);
            }
        });

        btn_ordCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordCompleteLs = new Intent(Orders.this, BuyerOrderCompletedList.class);
                startActivity(intent2ordCompleteLs);
            }
        });

        btn_ordRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordRatedLs = new Intent(Orders.this, BuyerOrderRatedList.class);
                startActivity(intent2ordRatedLs);
            }
        });

        btn_ordCancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordCancelLs = new Intent(Orders.this, BuyerOrderCancelList.class);
                startActivity(intent2ordCancelLs);
            }
        });

        auth = FirebaseAuth.getInstance();

        pendingCount.setVisibility(View.GONE);
        completeCount.setVisibility(View.GONE);
        progressCount.setVisibility(View.GONE);
        RatedCount.setVisibility(View.GONE);
        CancelCount.setVisibility(View.GONE);
    }

}