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

public class AdminHome extends AppCompatActivity {
    private TextView bookingTransactionCount, homeContentCount, userReportsCount, RatedCount, CancelCount;
    private Button btn_ordPending, btn_ordProgress, btn_content, btn_bookingTransaction, btn_userReports;
    private ImageButton btn_buyerO_message;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String OStatus, BuyerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        bookingTransactionCount = findViewById(R.id.bookingTransactionCount);

        homeContentCount = findViewById(R.id.homeContentCount);
        userReportsCount = findViewById(R.id.userReportsCount);
        /*
        RatedCount = findViewById(R.id.RatedCount);
        CancelCount = findViewById(R.id.CancelCount);
        */
        btn_content = findViewById(R.id.btn_content);
        btn_bookingTransaction = findViewById(R.id.btn_bookingTransaction);
        btn_userReports = findViewById(R.id.btn_userReports);


        BottomNavigationView adminNV = findViewById(R.id.adminNavigation);

        //set Home Selected
        adminNV.setSelectedItemId(R.id.adminHome);
        adminNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminHome:
                        return true;

                    case R.id.adminAccount:
                        startActivity(new Intent(getApplicationContext(), AdminAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        btn_userReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdminHome.this, AdminReportedUsers.class);
                startActivity(intent1);
            }
        });
        /*
        btn_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2ordCompleteLs = new Intent(AdminHome.this, BuyerOrderCompletedList.class);
                startActivity(intent2ordCompleteLs);
            }
        });

         */
        btn_bookingTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdminHome.this, AdminBookingTransactionsList.class);
                startActivity(intent1);
            }
        });


        userReportsCount.setVisibility(View.GONE);
        homeContentCount.setVisibility(View.GONE);
        bookingTransactionCount.setVisibility(View.GONE);
        /*
        auth = FirebaseAuth.getInstance();
        RatedCount.setVisibility(View.GONE);
        CancelCount.setVisibility(View.GONE);
        */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}