package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class OrderStatusPenDetail extends AppCompatActivity {

    private ImageButton PDetailBackToOrderList;
    private Button Btn_SellerOrderContact;
    private TextView buyerOrderDetail_NoticePending, buyerOrderDetail_serSellerName, buyerOrderDetail_SerTitle, buyerOrderDetail_serPrice;
    private TextView ReasonDesc, buyerOrderDetail_orderID, buyerOrderDetail_OrderTime, buyerOrderDetail_AcceptedTime, buyerOrderDetail_CompletedTime, buyerOrderDetail_CancelTime;
    private ImageView buyerOrderDetail_serSelleImage, buyerOrderDetail_SignalLogo, buyerOrderDetailSer_Image, buyerOrderDetail_viewProfile;
    private LinearLayout linearLayoutReason, linearLayoutReminder, linearCompletedTime, linearAcceptedTime, linearCancelTime;

    private String orderID, sellerID, serviceID;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_pen_detail);

        PDetailBackToOrderList = findViewById(R.id.PDetailBackToOrderList);
        Btn_SellerOrderContact = findViewById(R.id.Btn_SellerOrderContact);

        linearLayoutReason = findViewById(R.id.linearLayoutReason);
        linearCancelTime = findViewById(R.id.linearCancelTime);
        linearAcceptedTime = findViewById(R.id.linearAcceptedTime);
        linearCompletedTime = findViewById(R.id.linearCompletedTime);
        linearLayoutReminder = findViewById(R.id.linearLayoutReminder);

        ReasonDesc = findViewById(R.id.ReasonDesc);
        buyerOrderDetail_CancelTime = findViewById(R.id.buyerOrderDetail_CancelTime);
        buyerOrderDetail_NoticePending = findViewById(R.id.buyerOrderDetail_NoticePending);
        buyerOrderDetail_serSellerName = findViewById(R.id.buyerOrderDetail_serSellerName);
        buyerOrderDetail_SerTitle = findViewById(R.id.buyerOrderDetail_SerTitle);
        buyerOrderDetail_serPrice = findViewById(R.id.buyerOrderDetail_serPrice);
        buyerOrderDetail_orderID = findViewById(R.id.buyerOrderDetail_orderID);
        buyerOrderDetail_OrderTime = findViewById(R.id.buyerOrderDetail_OrderTime);
        buyerOrderDetail_AcceptedTime = findViewById(R.id.buyerOrderDetail_AcceptedTime);
        buyerOrderDetail_CompletedTime = findViewById(R.id.buyerOrderDetail_CompletedTime);
        buyerOrderDetail_serSelleImage = findViewById(R.id.buyerOrderDetail_serSelleImage);
        buyerOrderDetail_SignalLogo = findViewById(R.id.buyerOrderDetail_SignalLogo);
        buyerOrderDetailSer_Image = findViewById(R.id.buyerOrderDetailSer_Image);
        buyerOrderDetail_viewProfile = findViewById(R.id.buyerOrderDetail_viewProfile);

        auth = FirebaseAuth.getInstance();
        orderID = getIntent().getStringExtra("OrderID");
        sellerID = getIntent().getStringExtra("SellerID");
        serviceID = getIntent().getStringExtra("ServiceID");

        loadOrderInfo();

        PDetailBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buyerOrderDetailSer_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntentToDetail = new Intent (OrderStatusPenDetail.this , BuyerServiceDetail.class);
                BackIntentToDetail.putExtra("SID", serviceID);
                BackIntentToDetail.putExtra("UID", sellerID);
                startActivity(BackIntentToDetail);
            }
        });

        buyerOrderDetail_viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentToUProfile = new Intent (OrderStatusPenDetail.this , BuyerViewFreelancerProfile.class);
                IntentToUProfile.putExtra("UID", sellerID);
                startActivity(IntentToUProfile);
            }
        });

        Btn_SellerOrderContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuyerIntentToContact = new Intent (OrderStatusPenDetail.this , ChatConversation.class);
                BuyerIntentToContact.putExtra("SID", serviceID);
                BuyerIntentToContact.putExtra("UID", sellerID);
                startActivity(BuyerIntentToContact);
            }
        });
    }

    private void loadOrderInfo() {
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("Tasks");
        orderReference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String OrderID = ""+snapshot.child("OrderID").getValue();
                String OrderTime = ""+snapshot.child("OrderID").getValue();
                String AcceptedTime = ""+snapshot.child("AcceptedTime").getValue();
                String CompletedTime = ""+snapshot.child("CompletedTime").getValue();
                sellerID = ""+snapshot.child("SellerID").getValue();
                serviceID = ""+snapshot.child("ServiceID").getValue();
                String OStatus = ""+snapshot.child("OStatus").getValue();

                //set data to text view

                buyerOrderDetail_orderID.setText(OrderID);

                //convert timestamp to proper format dd/MM/yyyy
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(Long.parseLong(OrderTime));
                String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();

                buyerOrderDetail_OrderTime.setText(dateFormat);

                if(OStatus.equals("Progressing")) {
                    Calendar calendar1 = Calendar.getInstance(Locale.ENGLISH);
                    calendar1.setTimeInMillis(Long.parseLong(AcceptedTime));
                    String dateFormat1 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar1).toString();
                    buyerOrderDetail_AcceptedTime.setText(dateFormat1);

                    linearLayoutReminder.setVisibility(View.GONE);
                    linearCancelTime.setVisibility(View.GONE);
                    buyerOrderDetail_NoticePending.setText(R.string.BuyerOrdersProgressDetail_SignalTitle);
                    buyerOrderDetail_SignalLogo.setImageResource(R.drawable.ic_progress);
                }

                if(OStatus.equals("Completed")) {
                    Calendar calendar3 = Calendar.getInstance(Locale.ENGLISH);
                    calendar3.setTimeInMillis(Long.parseLong(AcceptedTime));
                    String dateFormat3 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar3).toString();
                    buyerOrderDetail_AcceptedTime.setText(dateFormat3);

                    Calendar calendar2 = Calendar.getInstance(Locale.ENGLISH);
                    calendar2.setTimeInMillis(Long.parseLong(CompletedTime));
                    String dateFormat2 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar2).toString();
                    buyerOrderDetail_CompletedTime.setText(dateFormat2);

                    linearLayoutReminder.setVisibility(View.GONE);
                    linearCancelTime.setVisibility(View.GONE);
                    buyerOrderDetail_NoticePending.setText(R.string.BuyerOrdersCompDetail_SignalTitle);
                    buyerOrderDetail_SignalLogo.setImageResource(R.drawable.ic_completed);
                }

                if(OStatus.equals("Cancelled")) {
                    linearCancelTime.setVisibility(View.VISIBLE);
                    linearLayoutReason.setVisibility(View.VISIBLE);
                    linearCompletedTime.setVisibility(View.GONE);
                    linearAcceptedTime.setVisibility(View.GONE);
                    linearLayoutReminder.setVisibility(View.GONE);
                    Btn_SellerOrderContact.setVisibility(View.GONE);

                    buyerOrderDetail_NoticePending.setText(R.string.BuyerOrdersCancelDetail_SignalTitle);
                    buyerOrderDetail_SignalLogo.setImageResource(R.drawable.ic_cancelled);
                }

                if(AcceptedTime.isEmpty()){
                    linearAcceptedTime.setVisibility(View.GONE);
                }
                if(CompletedTime.isEmpty()){
                    linearCompletedTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.orderByChild("UID").equalTo(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String UImage = ""+ds.child("ProfileImage").getValue();

                    buyerOrderDetail_serSellerName.setText(FullName);

                    try {
                        Picasso.get().load(UImage).placeholder(R.drawable.profile).into(buyerOrderDetail_serSelleImage);
                    }
                    catch (Exception e) {
                        buyerOrderDetail_serSelleImage.setImageResource(R.drawable.profile);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference("Services");
        serviceReference.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String STitle = ""+ds.child("STitle").getValue();
                    String SPrice = "PHP "+ds.child("SPrice").getValue();
                    String SImage = ""+ds.child("SImage").getValue();

                    buyerOrderDetail_SerTitle.setText(STitle);
                    buyerOrderDetail_serPrice.setText(SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(buyerOrderDetailSer_Image);
                    }
                    catch (Exception e) {
                        buyerOrderDetailSer_Image.setImageResource(R.drawable.ic_no_image);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference cancelReference = FirebaseDatabase.getInstance().getReference("CancelOrder");
        cancelReference.orderByChild("OrderID").equalTo(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String CancelID = ""+ds.child("CancelID").getValue();
                    String CReason = ""+ds.child("CReason").getValue();

                    linearCancelTime.setVisibility(View.VISIBLE);
                    //convert timestamp to proper format dd/MM/yyyy
                    Calendar calendarCancel = Calendar.getInstance(Locale.ENGLISH);
                    calendarCancel.setTimeInMillis(Long.parseLong(CancelID));
                    String dateFormatCancel = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendarCancel).toString();

                    buyerOrderDetail_CancelTime.setText(dateFormatCancel);
                    ReasonDesc.setText(CReason);

                    linearCompletedTime.setVisibility(View.GONE);
                    linearAcceptedTime.setVisibility(View.GONE);
                    linearLayoutReminder.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}