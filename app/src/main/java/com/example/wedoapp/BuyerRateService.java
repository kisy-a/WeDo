package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BuyerRateService extends AppCompatActivity {

    private TextView rate_ordServiceID, rate_serTitle, rate_ordID, rate_serCategory, rate_serDeliveryDays;
    private EditText rate_comments;
    private RatingBar rate_service;
    private ImageButton backToCompleteList;
    private Button btn_rateSubmit;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private String orderID, serviceID, OComment, ORating,OCategory, OTitle,OHours, sellerID, BuyerName, BuyerImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_rate_service);

        backToCompleteList = findViewById(R.id.backToCompleteList);
        btn_rateSubmit = findViewById(R.id.btn_rateSubmit);
        rate_serCategory = findViewById(R.id.rate_serCategory);
        rate_ordID = findViewById(R.id.rate_ordID);
        rate_ordServiceID = findViewById(R.id.rate_ordServiceID);
        rate_serTitle = findViewById(R.id.rate_serTitle);
        rate_comments = findViewById(R.id.rate_comments);
        rate_service = findViewById(R.id.rate_service);
        rate_serDeliveryDays = findViewById(R.id.rate_serDeliveryDays);
        auth = FirebaseAuth.getInstance();

        orderID = getIntent().getStringExtra("OrderID");
        serviceID = getIntent().getStringExtra("ServiceID");
        sellerID = getIntent().getStringExtra("SellerID");
        loadOrderInfo();

        backToCompleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_rateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputReview();
            }
        });

    }

    private void loadOrderInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String OrderID = ""+snapshot.child("OrderID").getValue();
                serviceID = ""+snapshot.child("ServiceID").getValue();

                //set data to text view
                rate_ordServiceID.setText(serviceID);
                rate_ordID.setText(OrderID);

                rate_ordServiceID.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference serRef = FirebaseDatabase.getInstance().getReference("Services");
        serRef.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    String ServiceTitle = ""+ds.child("STitle").getValue();
                    String ServiceCategory = ""+ds.child("SCategory").getValue();
                    sellerID = ""+ds.child("UID").getValue();
                    rate_serTitle.setText(ServiceTitle);
                    rate_serCategory.setText(ServiceCategory);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inputReview() {

        OComment = rate_comments.getText().toString().trim();
        ORating = ""+rate_service.getRating();
        serviceID = rate_ordServiceID.getText().toString().trim();
        OTitle = rate_serTitle.getText().toString().trim();
        OCategory = rate_serCategory.getText().toString().trim();
        OHours = rate_serDeliveryDays.getText().toString().trim();
        orderID = rate_ordID.getText().toString().trim();

        if(Float.parseFloat(ORating)==0)
        {
            Toast.makeText(getApplicationContext(),"Rating Star is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        saveReview();
    }

    private void saveReview() {

                final String timestamp = "" + System.currentTimeMillis();

                //add review to database
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("RatingID", "" + timestamp);
                hashMap.put("OComment", "" + OComment);
                hashMap.put("ORating", "" + ORating);
                hashMap.put("OTitle", "" + OTitle);
                hashMap.put("OCategory", ""+ OCategory);
                hashMap.put("OHours", ""+ OHours);
                hashMap.put("SellerID", "" + sellerID);
                hashMap.put("ServiceID", "" + serviceID);
                hashMap.put("OrderID", "" + orderID);
                hashMap.put("BuyerID", "" + auth.getUid());

                //save to database (Review table)
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Reviews");
                dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update status
                        //String orderID = databaseReference.getOrderID();
                        String oStatus = "Rated";

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
                        databaseReference.child(orderID).child("OStatus").setValue(oStatus);

                        Toast.makeText(getApplicationContext(), "Rated successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent2ordList = new Intent(BuyerRateService.this, BuyerOrderRatedList.class);
                        startActivity(intent2ordList);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                });

                //save to database (Services -> Review table)
                /*DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Services");
                databaseReference1.child(serviceID).child("Reviews").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                });*/

            }

}