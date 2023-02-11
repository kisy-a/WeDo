package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class BuyerFaq_ProjectPost extends AppCompatActivity {

    private ImageButton PPFaqMainBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_faq__project_post);

        PPFaqMainBack = findViewById(R.id.PPFaqMainBack);

        PPFaqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}