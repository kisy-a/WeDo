package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class AdminBookingTransactionsList extends AppCompatActivity {
    private Button btn_savePdf;
    private ImageButton backToAdminHome;
    private ArrayList<Model_Order> OrderBookingTransactionsList;

    private RecyclerView recyclerViewAdminBookingTransactions;
    private AdapterOrderBookingTransactions adapterOrderBookingTransactions;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_transactions);

        btn_savePdf = findViewById(R.id.btn_savePdf);
        backToAdminHome = findViewById(R.id.backToAdminHome);
        recyclerViewAdminBookingTransactions = findViewById(R.id.recyclerViewAdminBookingTransactions);

        loadOrderBookingTransactions();

        backToAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_savePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadOrderBookingTransactions() {
        OrderBookingTransactionsList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("OrderID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);

                    OrderBookingTransactionsList.add(model_order);
                }
                //setup adapter
                adapterOrderBookingTransactions = new AdapterOrderBookingTransactions(AdminBookingTransactionsList.this, OrderBookingTransactionsList);
                //set adapter
                recyclerViewAdminBookingTransactions.setAdapter(adapterOrderBookingTransactions);
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