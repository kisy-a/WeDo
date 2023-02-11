package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

public class PaymentStatement extends AppCompatActivity {

    private ImageView bankSlip;
    private ImageButton payStateBackToPendingList;
    private TextView tv_NoPaymentSlip;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_statement);

        tv_NoPaymentSlip = findViewById(R.id.tv_NoPaymentSlip);
        payStateBackToPendingList = findViewById(R.id.payStateBackToPendingList);
        bankSlip = findViewById(R.id.bankSlip);

        auth = FirebaseAuth.getInstance();
        orderID = getIntent().getStringExtra("OrderID");

        loadPaymentSlip();

        payStateBackToPendingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // payStateBack2taskList = new Intent(PaymentStatement.this, SellerTaskPendingList.class);
                //startActivity(payStateBack2taskList);
            }
        });
    }

    private void loadPaymentSlip() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String OImage = ""+snapshot.child("OImage").getValue();

                //set data to text view

                try {
                    Picasso.get().load(OImage).placeholder(R.drawable.ic_no_image).into(bankSlip);
                }
                catch (Exception e) {
                    bankSlip.setImageResource(R.drawable.ic_no_image);
                }

                if(OImage.isEmpty()){
                    bankSlip.setVisibility(View.GONE);
                    tv_NoPaymentSlip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}