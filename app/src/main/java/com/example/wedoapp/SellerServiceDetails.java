package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class SellerServiceDetails extends AppCompatActivity {

    private ImageButton DetailBackToSerList;
    private ImageView serImage;
    private TextView serTitle, serDesc, serCategory,  serPrice, serDays;
    private Button btn_serEdit, btn_serDelete;

    private String serviceID;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_service_details);

        serImage = findViewById(R.id.serImage);
        serTitle = findViewById(R.id.serTitle);
        serDesc = findViewById(R.id.serDesc);
        serCategory = findViewById(R.id.serCategory);
        serPrice = findViewById(R.id.serPrice);
        serDays = findViewById(R.id.serDays);

        DetailBackToSerList = findViewById(R.id.DetailBackToSerList);
        btn_serEdit = findViewById(R.id.btn_serEdit);
        btn_serDelete = findViewById(R.id.btn_serDelete);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");


        DetailBackToSerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_serEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditSer = new Intent (SellerServiceDetails.this , EditService.class);
                intentToEditSer.putExtra("SID", serviceID);
                startActivity(intentToEditSer);
            }
        });

        btn_serDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteService(serviceID);
                Intent intentToDelSer = new Intent (SellerServiceDetails.this , SellerAccount.class);
                startActivity(intentToDelSer);
                finish();
            }
        });
        loadServiceDetail();
    }

    private void loadServiceDetail() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String STitle = ""+snapshot.child("STitle").getValue();
                String SDescription = ""+snapshot.child("SDescription").getValue();
                String SCategory = ""+snapshot.child("SCategory").getValue();
                String SPrice = ""+snapshot.child("SPrice").getValue();
                String SDeliveryDays = ""+snapshot.child("SDeliveryDays").getValue();
                String SImage = ""+snapshot.child("SImage").getValue();

                //set data to text view
                serTitle.setText(CensorWords.censor(STitle));
                serDesc.setText(CensorWords.censor(SDescription));
                serCategory.setText(SCategory);
                serPrice.setText("PHP " + SPrice);
                serDays.setText(SDeliveryDays + " Hours");

                try {
                    Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(serImage);
                }
                catch (Exception e) {
                    serImage.setImageResource(R.drawable.ic_no_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteService(String serviceID) {

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
        dr.child(serviceID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Deleted.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}