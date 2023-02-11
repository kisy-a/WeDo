package com.example.wedoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.window.OnBackInvokedCallback;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerTypeActivity extends AppCompatActivity {
    private Button btnBuyer, btnSeller;
    ImageButton Back;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        btnBuyer = findViewById(R.id.user_buyer);
        btnSeller = findViewById(R.id.user_seller);

        Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = "buyer";
                Intent intent = new Intent(CustomerTypeActivity.this, SignUp.class);
                intent.putExtra("userType", str);
                startActivity(intent);
            }
        });

        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = "seller";
                Intent intent = new Intent(CustomerTypeActivity.this, SignUp.class);
                intent.putExtra("userType", str);
                startActivity(intent);
            }
        });
    }
}