package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BuyerFaq extends AppCompatActivity {

    private ImageButton faqMainBack;
    private ImageView view_browseSer, view_projectPost, view_paymentBuyer, view_manageOrder;
    private ImageView view_serList, view_manageSer, view_jobBrowse, view_paymentSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_faq);

        faqMainBack = findViewById(R.id.faqMainBack);
        view_browseSer = findViewById(R.id.view_browseSer);
        view_projectPost = findViewById(R.id.view_projectPost);
        view_paymentBuyer = findViewById(R.id.view_paymentBuyer);
        view_manageOrder = findViewById(R.id.view_manageOrder);
        view_serList = findViewById(R.id.view_serList);
        view_manageSer = findViewById(R.id.view_manageSer);
        view_jobBrowse = findViewById(R.id.view_jobBrowse);
        view_paymentSeller = findViewById(R.id.view_paymentSeller);

        faqMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //buyerFaq

        view_browseSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent toBrowseSer = new Intent (BuyerFaq.this , BuyerFaq_BrowseService.class);
               startActivity(toBrowseSer);
            }
        });

        view_projectPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProjectPost = new Intent (BuyerFaq.this , BuyerFaq_ProjectPost.class);
                startActivity(toProjectPost);
            }
        });

        view_paymentBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBPayment = new Intent (BuyerFaq.this , BuyerFaq_BPayment.class);
                startActivity(toBPayment);
            }
        });

        view_manageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toManageOrd = new Intent (BuyerFaq.this , BuyerFaq_ManageOrder.class);
                startActivity(toManageOrd);
            }
        });

        //sellerFaq

        view_serList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSerList = new Intent (BuyerFaq.this , SellerFaq_ServiceListing.class);
                startActivity(toSerList);
            }
        });

        view_manageSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toManageSer = new Intent (BuyerFaq.this , SellerFaq_ManageService.class);
                startActivity(toManageSer);
            }
        });

        view_jobBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBrowseJob = new Intent (BuyerFaq.this , SellerFaq_JobBrowsing.class);
                startActivity(toBrowseJob);
            }
        });

        view_paymentSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSPayment = new Intent (BuyerFaq.this , SellerFaq_BPayment.class);
                startActivity(toSPayment);
            }
        });

    }

}