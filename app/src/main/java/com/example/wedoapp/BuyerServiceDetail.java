package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyerServiceDetail extends AppCompatActivity {

    private ImageView buyer_serImage, buyer_serSelleImage;
    private TextView title_sampleDesign, tv_viewAllServices, tv_BViewReview, buyer_serSellerName, buyer_SerTitle, buyer_SerDeliveryDays, buyer_SerDesc, buyer_serPrice, buyer_serRatingAverage, buyer_serReviewNum;

    private ImageButton SerDetBackToSerList;
    private Button btn_OrderContact, btn_buy;
    private RatingBar buyer_serRatingBarAverage;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ArrayList<Model_Rating> MainViewReviewList;
    private ArrayList<Model_Service> OtherServicesList;
    private ArrayList<Model_PortfolioDesign> OnServicePortfolioList;
    private RecyclerView recyclerViewBuyerReviewMain, recyclerViewOtherServices, recyclerViewOnServicePortfolio;
    private AdapterMainViewReview adapterMainViewReview;
    private AdapterViewOtherServices adapterViewOtherServices;
    private AdapterViewOnServicePortfolio adapterViewOnServicePortfolio;

    private String serviceID, sellerName, deliveryDays, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_service_detail);

        title_sampleDesign = findViewById(R.id.title_sampleDesign);
        tv_viewAllServices = findViewById(R.id.tv_viewAllServices);
        recyclerViewOnServicePortfolio = findViewById(R.id.recyclerViewOnServicePortfolio);
        recyclerViewOtherServices = findViewById(R.id.recyclerViewOtherServices);
        recyclerViewBuyerReviewMain = findViewById(R.id.recyclerViewBuyerReviewMain);
        SerDetBackToSerList = findViewById(R.id.SerDetBackToSerList);
        tv_BViewReview = findViewById(R.id.tv_BViewReview);
        btn_OrderContact = findViewById(R.id.btn_OrderContact);
        btn_buy = findViewById(R.id.btn_buy);

        buyer_serRatingBarAverage = findViewById(R.id.buyer_serRatingBarAverage);
        buyer_serRatingAverage = findViewById(R.id.buyer_serRatingAverage);
        buyer_serReviewNum = findViewById(R.id.buyer_serReviewNum);
        buyer_serSelleImage = findViewById(R.id.buyer_serSelleImage);
        buyer_serImage = findViewById(R.id.buyer_serImage);
        buyer_serSellerName = findViewById(R.id.buyer_serSellerName);
        buyer_SerTitle = findViewById(R.id.buyer_SerTitle);
        buyer_SerDeliveryDays = findViewById(R.id.buyer_SerDeliveryDays);
        buyer_SerDesc = findViewById(R.id.buyer_SerDesc);
        buyer_serPrice = findViewById(R.id.buyer_serPrice);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");
        userID = getIntent().getStringExtra("UID");

        loadServiceInfo();
        loadMainReview();
        loadOtherServices();
        loadPortfolio();

        SerDetBackToSerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
               // Intent sBackToSerList = new Intent (BuyerServiceDetail.this , ServiceCategoryHair.class);
                //startActivity(sBackToSerList);
            }
        });

        btn_OrderContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serIntentToContact = new Intent (BuyerServiceDetail.this , ChatConversation.class);
                serIntentToContact.putExtra("SID", serviceID);
                serIntentToContact.putExtra("UID", userID);
                startActivity(serIntentToContact);
            }
        });

        tv_BViewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToViewRevB = new Intent (BuyerServiceDetail.this , BuyerViewReview.class);
                intentToViewRevB.putExtra("SID", serviceID);
                startActivity(intentToViewRevB);
            }
        });

        tv_viewAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToViewAllSer = new Intent (BuyerServiceDetail.this , BuyerViewFreelancerProfile.class);
                intentToViewAllSer.putExtra("UID", userID);
                startActivity(intentToViewAllSer);
            }
        });
        buyer_serSellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToViewAllSer = new Intent (BuyerServiceDetail.this , BuyerViewFreelancerProfile.class);
                intentToViewAllSer.putExtra("UID", userID);
                startActivity(intentToViewAllSer);
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToCheckout = new Intent (BuyerServiceDetail.this , BuyerOrderCheckout.class);
                intentToCheckout.putExtra("SID", serviceID);
                intentToCheckout.putExtra("UID", userID);
                startActivity(intentToCheckout);
            }
        });

    }

    private float ratingSum = 0;
    private void loadServiceInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String STitle = ""+snapshot.child("STitle").getValue();
                String SDescription = ""+snapshot.child("SDescription").getValue();
                String SPrice = "PHP "+snapshot.child("SPrice").getValue();
                String SDeliveryDays = ""+snapshot.child("SDeliveryDays").getValue() + " Delivery Hours";
                String SImage = ""+snapshot.child("SImage").getValue();
                userID = ""+snapshot.child("UID").getValue();

                //set data to text view
                buyer_SerTitle.setText(CensorWords.censor(STitle));
                buyer_SerDeliveryDays.setText(SDeliveryDays);
                buyer_SerDesc.setText(CensorWords.censor(SDescription));
                buyer_serPrice.setText(SPrice);

                try {
                    Picasso.get().load(SImage).resize(1260,980).centerCrop().placeholder(R.drawable.ic_no_image).into(buyer_serImage);
                }
                catch (Exception e) {
                    buyer_serImage.setImageResource(R.drawable.ic_no_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference imageReference = FirebaseDatabase.getInstance().getReference("Users");
        imageReference.orderByChild("UID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String SellerImage = ""+ds.child("ProfileImage").getValue();

                    buyer_serSellerName.setText(FullName);
                    try {
                        Picasso.get().load(SellerImage).placeholder(R.drawable.profile).into(buyer_serSelleImage);
                    }
                    catch (Exception e) {
                        buyer_serSelleImage.setImageResource(R.drawable.profile);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Reviews");
        dr.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    ratingSum = 0;

                    for (DataSnapshot ds: snapshot.getChildren()){
                        float rating = Float.parseFloat("" + ds.child("ORating").getValue());
                        ratingSum = ratingSum + rating;

                    }

                    long numberOfReviews = snapshot.getChildrenCount();
                    buyer_serReviewNum.setText("(" + (int) numberOfReviews + ")");


                    float avgRating = ratingSum/numberOfReviews;
                    buyer_serRatingAverage.setText(String.format("%.2f", avgRating));
                    buyer_serRatingBarAverage.setRating(avgRating);
                }

                else{
                    buyer_serRatingAverage.setText("No Rating");
                    buyer_serRatingAverage.setTextColor(getResources().getColor(R.color.grey2));
                    buyer_serReviewNum.setText("(0)");
                    //buyer_serRatingAverage.setVisibility(View.GONE);
                    //buyer_serRatingBarAverage.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadOtherServices() {
        OtherServicesList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("UID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);
                    //OtherServicesList.add(model_service);

                    //show other services except current service
                    String SID = ""+ds.child("SID").getValue();
                    if(!SID.equals(serviceID)){
                        OtherServicesList.add(model_service);
                    }

                }
                //setup adapter
                adapterViewOtherServices = new AdapterViewOtherServices(BuyerServiceDetail.this, OtherServicesList);
                //set adapter
                recyclerViewOtherServices.setAdapter(adapterViewOtherServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPortfolio() {
        OnServicePortfolioList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Portfolio");
        databaseReference.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_PortfolioDesign model_portfolioDesign = ds.getValue(Model_PortfolioDesign.class);
                    OnServicePortfolioList.add(model_portfolioDesign);

                }
                //setup adapter
                adapterViewOnServicePortfolio = new AdapterViewOnServicePortfolio(BuyerServiceDetail.this, OnServicePortfolioList);
                //set adapter
                recyclerViewOnServicePortfolio.setAdapter(adapterViewOnServicePortfolio);

                if(OnServicePortfolioList.isEmpty()){
                    title_sampleDesign.setText(R.string.BuyerServiceDetail_NoDesignPreview);
                    recyclerViewOnServicePortfolio.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadMainReview() {
        MainViewReviewList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");
        databaseReference.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Rating model_rating = ds.getValue(Model_Rating.class);
                    MainViewReviewList.add(0, model_rating);

                }
                //setup adapter
                adapterMainViewReview = new AdapterMainViewReview(BuyerServiceDetail.this, MainViewReviewList);
                //set adapter
                recyclerViewBuyerReviewMain.setAdapter(adapterMainViewReview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);

    }




}