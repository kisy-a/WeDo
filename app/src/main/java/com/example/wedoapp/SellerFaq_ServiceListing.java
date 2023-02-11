package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SellerFaq_ServiceListing extends AppCompatActivity {

    private ImageButton SLFaqMainBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_faq__service_listing);

        SLFaqMainBack = findViewById(R.id.SLFaqMainBack);

        SLFaqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}