package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BuyerFaq_ManageOrder extends AppCompatActivity {

    private ImageButton MOFaqMainBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_faq__manage_order);

        MOFaqMainBack = findViewById(R.id.MOFaqMainBack);

        MOFaqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}