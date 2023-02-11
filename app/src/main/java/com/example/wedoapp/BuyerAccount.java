package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class BuyerAccount extends AppCompatActivity {

    private ImageView iv_profilePic;
    private Button btn_editProfile, btn_postProject, btn_managePosting, btn_switchSeller, btn_buyerLogout, btn_buyerFaq, btn_buyerSupport;
    private TextView tv_BuyerAccUserName, tv_BuyerAccUserEmail;
    private String UID;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ImageButton btn_buyerA_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_account);

        btn_buyerSupport = findViewById(R.id.btn_buyerSupport);
        btn_buyerFaq = findViewById(R.id.btn_buyerFaq);
        iv_profilePic = findViewById(R.id.iv_profilePic);
        tv_BuyerAccUserName = findViewById(R.id.tv_BuyerAccUserName);
        tv_BuyerAccUserEmail = findViewById(R.id.tv_BuyerAccUserEmail);
        btn_editProfile = findViewById(R.id.btn_editProfile);
        btn_postProject = findViewById(R.id.btn_postProject);
        btn_managePosting = findViewById(R.id.btn_managePosting);
        //btn_switchSeller = findViewById(R.id.btn_switchSeller);
        btn_buyerLogout = findViewById(R.id.btn_buyerLogout);
        BottomNavigationView buyerNV = findViewById(R.id.buyerNavigation);

        loadBuyerAccount();

        //set Buyer Account Selected
        buyerNV.setSelectedItemId(R.id.buyerAccount);
        buyerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buyerHome:
                        startActivity(new Intent(getApplicationContext(), BuyerHome.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerOrder:
                        startActivity(new Intent(getApplicationContext(), Orders.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerMessage:
                        startActivity(new Intent(getApplicationContext(), ChatMessage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerAccount:
                        return true;
                }
                return false;
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditProfile = new Intent (BuyerAccount.this , EditProfile.class);
                startActivity(intentToEditProfile);
            }
        });

        btn_postProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToPostProject = new Intent (BuyerAccount.this , BuyerPostProject.class);
                startActivity(intentToPostProject);
            }
        });

        btn_managePosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToManagePost = new Intent (BuyerAccount.this , ManagePostingList.class);
                startActivity(intentToManagePost);
            }
        });
        btn_buyerFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToFAQ = new Intent (BuyerAccount.this , BuyerFaq.class);
                startActivity(intentToFAQ);
            }
        });

        btn_buyerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSupport = new Intent (BuyerAccount.this , BuyerSupport.class);
                startActivity(intentToSupport);
            }
        });

        btn_buyerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intentToBLogout = new Intent (BuyerAccount.this , MainActivity.class);
                intentToBLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentToBLogout);
                finish();
            }
        });

    }

    private void signOut() {
        final String timestamp = "" + System.currentTimeMillis();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        databaseReference.child("OnlineStatus").setValue(timestamp);
        preferences.clearData(BuyerAccount.this);
        auth.signOut();

    }

    private void loadBuyerAccount() {

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

                tv_BuyerAccUserName.setText(fullName);
                tv_BuyerAccUserEmail.setText(email);

                try{
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(iv_profilePic);
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}