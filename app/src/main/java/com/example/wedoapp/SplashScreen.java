package com.example.wedoapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String UID;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //splashscreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                if (user != null) {
                    //get user ID
                    UID = user.getUid();
                    databaseReference.child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String user_type = snapshot.child("userType").getValue().toString();
                            if (user_type.equals("buyer")) {
                                Intent intent2BuyerHome = new Intent(SplashScreen.this, BuyerHome.class);
                                startActivity(intent2BuyerHome);
                                finish();
                            }
                            if (user_type.equals("seller")) {
                                Intent intent2SellerHome = new Intent(SplashScreen.this, BrowseJobList.class);
                                startActivity(intent2SellerHome);
                                finish();
                            }
                            if (user_type.equals("admin")) {
                                Intent intent2SellerHome = new Intent(SplashScreen.this, AdminHome.class);
                                startActivity(intent2SellerHome);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }
        }, 1000);
    }
}
