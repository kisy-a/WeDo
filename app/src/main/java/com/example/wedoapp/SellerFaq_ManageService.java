package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SellerFaq_ManageService extends AppCompatActivity {

    private ImageButton MSFaqMainBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_faq__manage_service);

        MSFaqMainBack = findViewById(R.id.MSFaqMainBack);

        MSFaqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}