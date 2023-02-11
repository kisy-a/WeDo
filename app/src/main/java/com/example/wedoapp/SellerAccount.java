package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerAccount extends AppCompatActivity {

    private ImageView iv_sellerProfilePic;
    private RatingBar seller_RatingBarAverage;
    private ImageButton btn_sellerAcc_message;
    private Button btn_addService, btn_manageService, btn_editProfile, btn_sellerLogout, btn_bankInfo;
    private TextView tv_sellerAccUserName, tv_sellerAccUserEmail, seller_avgRating;
    private String UID;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_account);

        seller_avgRating = findViewById(R.id.seller_avgRating);
        seller_RatingBarAverage = findViewById(R.id.seller_RatingBarAverage);
        btn_bankInfo = findViewById(R.id.btn_bankInfo);
        btn_editProfile = findViewById(R.id.btn_editProfile);
        iv_sellerProfilePic = findViewById(R.id.iv_sellerProfilePic);
        tv_sellerAccUserName = findViewById(R.id.tv_sellerAccUserName);
        //tv_sellerAccUserEmail = findViewById(R.id.tv_sellerAccUserEmail);
        btn_addService = findViewById(R.id.btn_addService);
        btn_manageService = findViewById(R.id.btn_manageService);
        //btn_switchBuyer = findViewById(R.id.btn_switchBuyer);
        btn_sellerLogout = findViewById(R.id.btn_sellerLogout);

        loadSellerAccount();
        loadAvgRating();

        BottomNavigationView sellerNV = findViewById(R.id.sellerNavigation);


        //set Account Selected
        sellerNV.setSelectedItemId(R.id.sellerAccount);
        sellerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerHome:
                        startActivity(new Intent(getApplicationContext(), BrowseJobList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerTask:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerMessage:
                        startActivity(new Intent(getApplicationContext(), SellerChatMessage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerAccount:
                        return true;
                }
                return false;
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditProfile = new Intent (SellerAccount.this , EditSellerProfile.class);
                startActivity(intentToEditProfile);
            }
        });
        btn_addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToPostSer = new Intent (SellerAccount.this , SellerPostService.class);
                startActivity(intentToPostSer);
            }
        });

        btn_bankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToBankInfoPage = new Intent (SellerAccount.this , BankInformation.class);
                startActivity(intentToBankInfoPage);
            }
        });

        btn_manageService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToManageSer = new Intent (SellerAccount.this , ManageServiceList.class);
                startActivity(intentToManageSer);
            }
        });
        btn_sellerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intentToSLogout = new Intent (SellerAccount.this , MainActivity.class);
                intentToSLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentToSLogout);
                finish();
            }
        });

    }

    private void signOut() {
        final String timestamp = "" + System.currentTimeMillis();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        databaseReference.child("OnlineStatus").setValue(timestamp);
        preferences.clearData(SellerAccount.this);
        auth.signOut();
    }

    private void loadSellerAccount() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //get user ID
        UID = user.getUid();

        databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.child("FullName").getValue().toString();
                String email = snapshot.child("EmailAddress").getValue().toString();
                String image = "" + snapshot.child("ProfileImage").getValue();

                tv_sellerAccUserName.setText(fullName);
                /*tv_sellerAccUserEmail.setText(email);

               try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(iv_sellerProfilePic);
                }
                catch (Exception e) {

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private float ratingSum = 0;
    private void loadAvgRating() {

        auth = FirebaseAuth.getInstance();

        DatabaseReference ratingReference = FirebaseDatabase.getInstance().getReference("Reviews");
        ratingReference.orderByChild("SellerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    ratingSum = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        float rating = Float.parseFloat("" + ds.child("ORating").getValue());
                        ratingSum = ratingSum + rating;

                    }

                    long numberOfReviews = snapshot.getChildrenCount();

                    float avgRating = ratingSum/numberOfReviews;
                    seller_avgRating.setText(String.format("%.2f", avgRating) + " Out of 5.00");
                    seller_RatingBarAverage.setRating(avgRating);
                }

                else{
                    seller_avgRating.setText("0 Out of 5.0");
                    seller_avgRating.setTextColor(getResources().getColor(R.color.grey2));
                    //buyer_serRatingAverage.setVisibility(View.GONE);
                    //buyer_serRatingBarAverage.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}