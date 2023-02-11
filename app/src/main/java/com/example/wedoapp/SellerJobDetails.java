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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SellerJobDetails extends AppCompatActivity {

    private ImageButton backToJobList;
    private Button btn_contactClient;
    private TextView jobTitle, jobCategory, jobPrice, jobDays, jobDate, jobTime, jobDesc, job_clientLocation, job_clientEmail, job_clientPhone, job_clientName;
    private ImageView job_clientImage;

    private String projectID, userID;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_job_details);

        backToJobList = findViewById(R.id.backToJobList);
        btn_contactClient = findViewById(R.id.btn_contactClient);

        job_clientImage = findViewById(R.id.job_clientImage);
        jobTitle = findViewById(R.id.jobTitle);
        jobCategory = findViewById(R.id.jobCategory);
        jobPrice = findViewById(R.id.jobPrice);
        jobDays = findViewById(R.id.jobDays);
        jobDate = findViewById(R.id.jobDate);
        jobTime = findViewById(R.id.jobTime);
        jobDesc = findViewById(R.id.jobDesc);
        job_clientLocation = findViewById(R.id.job_clientLocation);
        job_clientEmail = findViewById(R.id.job_clientEmail);
        job_clientPhone = findViewById(R.id.job_clientPhone);
        job_clientName = findViewById(R.id.job_clientName);

        auth = FirebaseAuth.getInstance();
        projectID = getIntent().getStringExtra("ProjectID");
        userID = getIntent().getStringExtra("UID");

        loadProjectInfo();

        backToJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent rateBack2compList = new Intent(SellerJobDetails.this, BrowseJobList.class);
                //startActivity(rateBack2compList);
            }
        });

        btn_contactClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jobIntentToContact = new Intent (SellerJobDetails.this , JobContact.class);
                jobIntentToContact.putExtra("ProjectID", projectID);
                jobIntentToContact.putExtra("UID", userID);
                startActivity(jobIntentToContact);
            }
        });
    }

    private void loadProjectInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.child(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PTitle = ""+snapshot.child("PTitle").getValue();
                String PDescription = ""+snapshot.child("PDescription").getValue();
                String PCategory = ""+snapshot.child("PCategory").getValue();
                String PBudget = "PHP "+snapshot.child("PBudget").getValue();
                String PDeliveryDays = ""+snapshot.child("PDeliveryDays").getValue() + " Hours";
                String PDeliveryDate = ""+snapshot.child("PDeliveryDate").getValue();
                String PDeliveryTime = ""+snapshot.child("PDeliveryTime").getValue();
                userID = ""+snapshot.child("UID").getValue();
                //String UName = ""+snapshot.child("UName").getValue();
                //String UEmail = ""+snapshot.child("UEmail").getValue();
                //String UContact = ""+snapshot.child("UContact").getValue();
                //String ULocation = ""+snapshot.child("ULocation").getValue();
                //String UImage = ""+snapshot.child("UImage").getValue();

                //set data to text view

                jobTitle.setText(CensorWords.censor(PTitle));
                jobDesc.setText(CensorWords.censor(PDescription));
                jobCategory.setText(PCategory);
                jobPrice.setText(PBudget);
                jobDays.setText(PDeliveryDays);
                jobDate.setText(PDeliveryDate);
                jobTime.setText(PDeliveryTime);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.orderByChild("UID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String UImage = ""+ds.child("ProfileImage").getValue();
                    String UEmail = ""+ds.child("EmailAddress").getValue();
                    String UContact = ""+ds.child("PhoneContact").getValue();
                    String ULocation = ""+ds.child("Location").getValue();

                    job_clientName.setText(FullName);
                    job_clientEmail.setText(UEmail);
                    job_clientPhone.setText(UContact);
                    job_clientLocation.setText(ULocation);

                    try {
                        Picasso.get().load(UImage).placeholder(R.drawable.profile).into(job_clientImage);
                    }
                    catch (Exception e) {
                        job_clientImage.setImageResource(R.drawable.profile);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}