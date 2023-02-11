package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyerViewFreelancerProfile extends AppCompatActivity {

    private ImageView profile_serSellerImage;
    private TextView profile_SellerName, profile_Location;
    private Button btn_userReport;
    private ImageButton ViewListDetBackToSer;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<Model_Service> AllServicesList;
    private RecyclerView recyclerViewSellerProfile;
    private AdapterProfileViewServices adapterProfileViewServices;

    private String sellerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_view_freelancer_profile);


        recyclerViewSellerProfile = findViewById(R.id.recyclerViewSellerProfile);
        profile_serSellerImage = findViewById(R.id.profile_serSellerImage);
        profile_SellerName = findViewById(R.id.profile_SellerName);
        profile_Location = findViewById(R.id.profile_Location);
        ViewListDetBackToSer  = findViewById(R.id.ViewListDetBackToSer);
        btn_userReport = findViewById(R.id.btn_userReport);
        auth = FirebaseAuth.getInstance();
        sellerID = getIntent().getStringExtra("UID");

        loadUserInfo();
        loadAllServices();

        ViewListDetBackToSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // Intent sBackToSerList = new Intent (BuyerServiceDetail.this , ServiceCategoryHair.class);
                //startActivity(sBackToSerList);
            }
        });

        btn_userReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToReportSeller = new Intent (BuyerViewFreelancerProfile.this , ReportUser.class);
                intentToReportSeller.putExtra("ReceiverID", sellerID);
                startActivity(intentToReportSeller);
            }
        });

        recyclerViewSellerProfile.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void loadUserInfo() {
        DatabaseReference imageReference = FirebaseDatabase.getInstance().getReference("Users");
        imageReference.orderByChild("UID").equalTo(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String SellerImage = ""+ds.child("ProfileImage").getValue();
                    String Location = ""+ds.child("Location").getValue();

                    profile_SellerName.setText(FullName);
                    profile_Location.setText(Location);

                    try {
                        Picasso.get().load(SellerImage).placeholder(R.drawable.profile).into(profile_serSellerImage);
                    }
                    catch (Exception e) {
                        profile_serSellerImage.setImageResource(R.drawable.profile);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadAllServices() {
        AllServicesList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("UID").equalTo(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);
                    AllServicesList.add(model_service);

                    //show other services except current service
                    //String SID = ""+ds.child("SID").getValue();
                   // if(!SID.equals(serviceID)){
                    //    OtherServicesList.add(model_service);
                    //}

                }
                //setup adapter
                adapterProfileViewServices = new AdapterProfileViewServices(BuyerViewFreelancerProfile.this, AllServicesList);
                //set adapter
                recyclerViewSellerProfile.setAdapter(adapterProfileViewServices);
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