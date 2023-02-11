package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SellerFaq_JobBrowsing extends AppCompatActivity {

    private ImageButton JBFaqMainBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_faq__job_browsing);

        JBFaqMainBack = findViewById(R.id.JBFaqMainBack);

        JBFaqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}