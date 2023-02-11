package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class BuyerCancelOrder extends AppCompatActivity {

    private TextView cancel_ordID, cancel_serTitle, cancel_ordServiceID;
    private EditText cancel_reason;
    private ImageButton backToBuyerPendingList;
    private Button btn_cancelSubmit;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private String orderID, serviceID, CancelReason, OTitle, sellerID, BuyerName, BuyerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_cancel_order);

        cancel_ordID = findViewById(R.id.cancel_ordID);
        cancel_serTitle = findViewById(R.id.cancel_serTitle);

        cancel_ordServiceID = findViewById(R.id.cancel_ordServiceID);
        cancel_reason = findViewById(R.id.cancel_reason);
        backToBuyerPendingList = findViewById(R.id.backToBuyerPendingList);
        btn_cancelSubmit = findViewById(R.id.btn_cancelSubmit);

        auth = FirebaseAuth.getInstance();

        orderID = getIntent().getStringExtra("OrderID");
        serviceID = getIntent().getStringExtra("ServiceID");
        sellerID = getIntent().getStringExtra("SellerID");
        loadOrderInfo();

        backToBuyerPendingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_cancelSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputCancel();
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
                cancel_ordServiceID.setText(serviceID);
                cancel_ordID.setText(OrderID);

                cancel_ordServiceID.setVisibility(View.INVISIBLE);

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
                    sellerID = ""+ds.child("UID").getValue();
                    cancel_serTitle.setText(ServiceTitle);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inputCancel() {

        CancelReason = cancel_reason.getText().toString().trim();
        serviceID = cancel_ordServiceID.getText().toString().trim();
        OTitle = cancel_serTitle.getText().toString().trim();
        orderID = cancel_ordID.getText().toString().trim();

        if(CancelReason.isEmpty()){
            Toast.makeText(getApplicationContext(),"Reason is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        saveReason();
    }

    private void saveReason() {

        final String timestamp = "" + System.currentTimeMillis();

        //add review to database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("CancelID", "" + timestamp);
        hashMap.put("CReason", "" + CancelReason);
        //hashMap.put("OTitle", "" + OTitle);
        hashMap.put("SellerID", "" + sellerID);
        hashMap.put("ServiceID", "" + serviceID);
        hashMap.put("OrderID", "" + orderID);
        hashMap.put("BuyerID", "" + auth.getUid());

        //save to database (Cancel table)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CancelOrder");
        databaseReference.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //update status
                //String orderID = databaseReference.getOrderID();
                String oStatus = "Cancelled";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
                databaseReference.child(orderID).child("OStatus").setValue(oStatus);

                Toast.makeText(getApplicationContext(), "Cancel successfully.", Toast.LENGTH_SHORT).show();

                Intent intent2cancelList = new Intent(BuyerCancelOrder.this, BuyerOrderCancelList.class);
                startActivity(intent2cancelList);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}