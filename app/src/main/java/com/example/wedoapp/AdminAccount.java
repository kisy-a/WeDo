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

public class AdminAccount extends AppCompatActivity {

    private ImageView iv_profilePic;
    private Button btn_editProfile, btn_adminLogout;
    private TextView tv_AdminAccUserName, tv_AdminAccUserEmail;
    private String UID;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ImageButton btn_buyerA_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        iv_profilePic = findViewById(R.id.iv_profilePic);
        tv_AdminAccUserName = findViewById(R.id.tv_AdminAccUserName);
        tv_AdminAccUserEmail = findViewById(R.id.tv_AdminAccUserEmail);
        btn_editProfile = findViewById(R.id.btn_editProfile);
        btn_adminLogout = findViewById(R.id.btn_adminLogout);
        BottomNavigationView adminNV = findViewById(R.id.adminNavigation);

        loadAdminAccount();

        //set Admin Account Selected
        adminNV.setSelectedItemId(R.id.adminAccount);
        adminNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminHome:
                        startActivity(new Intent(getApplicationContext(), AdminHome.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.adminAccount:
                        return true;
                }
                return false;
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToEditProfile = new Intent (AdminAccount.this , EditAdminProfile.class);
                startActivity(intentToEditProfile);
            }
        });

        btn_adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intentToALogout = new Intent (AdminAccount.this , MainActivity.class);
                intentToALogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentToALogout);
                finishAffinity();
            }
        });

    }

    private void signOut() {
        final String timestamp = "" + System.currentTimeMillis();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        databaseReference.child("OnlineStatus").setValue(timestamp);
        auth.signOut();

    }

    private void loadAdminAccount() {

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

                tv_AdminAccUserName.setText(fullName);
                tv_AdminAccUserEmail.setText(email);

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