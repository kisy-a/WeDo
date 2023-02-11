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

public class SellerTaskStatusDetail extends AppCompatActivity {

    private ImageButton TDetailBackToOrderList;
    private Button Btn_BuyerOrderContact;
    private TextView sellerTaskDetail_NoticePending, sellerTaskDetail_buyerName, sellerTaskDetail_SerTitle, sellerTaskDetail_serPrice;
    private TextView sellerTaskDetail_CancelTime, SReasonDesc, sellerTaskDetail_orderID, sellerTaskDetail_OrderTime, sellerTaskDetail_AcceptedTime, sellerTaskDetail_CompletedTime, sellerTaskDetail_RatedTime;
    private ImageView sellerTaskDetail_SignalLogo, sellerTaskDetailSer_Image, sellerTaskDetail_buyerImage;
    private LinearLayout linearLayoutSReason, linearSellerCancelTime, linearLayoutTaskReminder, linearTaskAcceptedTime, linearTaskCompletedTime, linearTaskRatedTime;

    private String orderID, buyerID, serviceID;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_status_detail);

        TDetailBackToOrderList = findViewById(R.id.TDetailBackToOrderList);
        Btn_BuyerOrderContact = findViewById(R.id.Btn_BuyerOrderContact);

        SReasonDesc = findViewById(R.id.SReasonDesc);
        linearLayoutSReason = findViewById(R.id.linearLayoutSReason);
        linearSellerCancelTime = findViewById(R.id.linearSellerCancelTime);

        sellerTaskDetail_CancelTime = findViewById(R.id.sellerTaskDetail_CancelTime);
        sellerTaskDetail_NoticePending = findViewById(R.id.sellerTaskDetail_NoticePending);
        sellerTaskDetail_buyerName = findViewById(R.id.sellerTaskDetail_buyerName);
        sellerTaskDetail_SerTitle = findViewById(R.id.sellerTaskDetail_SerTitle);
        sellerTaskDetail_serPrice = findViewById(R.id.sellerTaskDetail_serPrice);
        sellerTaskDetail_orderID = findViewById(R.id.sellerTaskDetail_orderID);
        sellerTaskDetail_OrderTime = findViewById(R.id.sellerTaskDetail_OrderTime);
        sellerTaskDetail_AcceptedTime = findViewById(R.id.sellerTaskDetail_AcceptedTime);
        sellerTaskDetail_CompletedTime = findViewById(R.id.sellerTaskDetail_CompletedTime);
        sellerTaskDetail_RatedTime = findViewById(R.id.sellerTaskDetail_RatedTime);
        sellerTaskDetail_SignalLogo = findViewById(R.id.sellerTaskDetail_SignalLogo);
        sellerTaskDetailSer_Image = findViewById(R.id.sellerTaskDetailSer_Image);
        sellerTaskDetail_buyerImage = findViewById(R.id.sellerTaskDetail_buyerImage);
        linearLayoutTaskReminder = findViewById(R.id.linearLayoutTaskReminder);
        linearTaskAcceptedTime = findViewById(R.id.linearTaskAcceptedTime);
        linearTaskCompletedTime = findViewById(R.id.linearTaskCompletedTime);
        linearTaskRatedTime = findViewById(R.id.linearTaskRatedTime);

        auth = FirebaseAuth.getInstance();
        orderID = getIntent().getStringExtra("OrderID");
        buyerID = getIntent().getStringExtra("UID");
        serviceID = getIntent().getStringExtra("ServiceID");

        loadTaskInfo();

        TDetailBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Btn_BuyerOrderContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuyerIntentToContact = new Intent (SellerTaskStatusDetail.this , SellerServiceContactBuyer.class);
                BuyerIntentToContact.putExtra("OrderID", orderID);
                BuyerIntentToContact.putExtra("UID", buyerID);
                startActivity(BuyerIntentToContact);
            }
        });

    }

    private void loadTaskInfo() {
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("Tasks");
        orderReference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String OrderID = ""+snapshot.child("OrderID").getValue();
                String OrderTime = ""+snapshot.child("OrderID").getValue();
                String AcceptedTime = ""+snapshot.child("AcceptedTime").getValue();
                String CompletedTime = ""+snapshot.child("CompletedTime").getValue();
                String RatedTime = ""+snapshot.child("RatedTime").getValue();
                buyerID = ""+snapshot.child("BuyerID").getValue();
                serviceID = ""+snapshot.child("ServiceID").getValue();
                String OStatus = ""+snapshot.child("OStatus").getValue();

                //set data to text view

                sellerTaskDetail_orderID.setText(OrderID);

                //convert timestamp to proper format dd/MM/yyyy
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(Long.parseLong(OrderTime));
                String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();

                sellerTaskDetail_OrderTime.setText(dateFormat);

                if(OStatus.equals("Pending")) {
                    linearTaskRatedTime.setVisibility(View.GONE);
                }


                if(OStatus.equals("Progressing")) {
                    Calendar calendar1 = Calendar.getInstance(Locale.ENGLISH);
                    calendar1.setTimeInMillis(Long.parseLong(AcceptedTime));
                    String dateFormat1 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar1).toString();
                    sellerTaskDetail_AcceptedTime.setText(dateFormat1);

                    linearSellerCancelTime.setVisibility(View.GONE);
                    linearLayoutTaskReminder.setVisibility(View.GONE);
                    sellerTaskDetail_NoticePending.setText(R.string.BuyerOrdersProgressDetail_SignalTitle);
                    sellerTaskDetail_SignalLogo.setImageResource(R.drawable.ic_progress_task);
                    linearTaskRatedTime.setVisibility(View.GONE);
                }

                if(OStatus.equals("Completed")) {
                    Calendar calendar3 = Calendar.getInstance(Locale.ENGLISH);
                    calendar3.setTimeInMillis(Long.parseLong(AcceptedTime));
                    String dateFormat3 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar3).toString();
                    sellerTaskDetail_AcceptedTime.setText(dateFormat3);

                    Calendar calendar2 = Calendar.getInstance(Locale.ENGLISH);
                    calendar2.setTimeInMillis(Long.parseLong(CompletedTime));
                    String dateFormat2 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar2).toString();
                    sellerTaskDetail_CompletedTime.setText(dateFormat2);

                    linearSellerCancelTime.setVisibility(View.GONE);
                    linearLayoutTaskReminder.setVisibility(View.GONE);
                    sellerTaskDetail_NoticePending.setText(R.string.BuyerOrdersCompDetail_SignalTitle);
                    sellerTaskDetail_SignalLogo.setImageResource(R.drawable.ic_complete_task);
                    linearTaskRatedTime.setVisibility(View.GONE);
                }

                if(OStatus.equals("Rated")) {
                    Calendar calendar3 = Calendar.getInstance(Locale.ENGLISH);
                    calendar3.setTimeInMillis(Long.parseLong(AcceptedTime));
                    String dateFormat3 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar3).toString();
                    sellerTaskDetail_AcceptedTime.setText(dateFormat3);

                    Calendar calendar2 = Calendar.getInstance(Locale.ENGLISH);
                    calendar2.setTimeInMillis(Long.parseLong(CompletedTime));
                    String dateFormat2 = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar2).toString();
                    sellerTaskDetail_CompletedTime.setText(dateFormat2);

                    linearSellerCancelTime.setVisibility(View.GONE);
                    linearLayoutTaskReminder.setVisibility(View.GONE);
                    sellerTaskDetail_NoticePending.setText(R.string.BuyerOrdersRatedDetail_SignalTitle);
                    sellerTaskDetail_SignalLogo.setImageResource(R.drawable.ic_complete_task);
                }

                if(OStatus.equals("Cancelled")) {
                    linearSellerCancelTime.setVisibility(View.VISIBLE);
                    linearLayoutSReason.setVisibility(View.VISIBLE);

                    linearTaskRatedTime.setVisibility(View.GONE);
                    linearTaskCompletedTime.setVisibility(View.GONE);
                    linearTaskAcceptedTime.setVisibility(View.GONE);
                    linearLayoutTaskReminder.setVisibility(View.GONE);
                    Btn_BuyerOrderContact.setVisibility(View.GONE);

                    sellerTaskDetail_NoticePending.setText(R.string.BuyerOrdersCancelDetail_SignalTitle);
                    sellerTaskDetail_SignalLogo.setImageResource(R.drawable.ic_cancelled);
                }


                if(AcceptedTime.isEmpty()){
                    linearTaskAcceptedTime.setVisibility(View.GONE);
                }
                if(CompletedTime.isEmpty()){
                    linearTaskCompletedTime.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.orderByChild("UID").equalTo(buyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String UImage = ""+ds.child("ProfileImage").getValue();

                    sellerTaskDetail_buyerName.setText(FullName);

                    try {
                        Picasso.get().load(UImage).placeholder(R.drawable.profile).into(sellerTaskDetail_buyerImage);
                    }
                    catch (Exception e) {
                        sellerTaskDetail_buyerImage.setImageResource(R.drawable.profile);
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

                    sellerTaskDetail_SerTitle.setText(STitle);
                    sellerTaskDetail_serPrice.setText(SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(sellerTaskDetailSer_Image);
                    }
                    catch (Exception e) {
                        sellerTaskDetailSer_Image.setImageResource(R.drawable.ic_no_image);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference timeReference = FirebaseDatabase.getInstance().getReference("Reviews");
        timeReference.orderByChild("OrderID").equalTo(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String RatingID = ""+ds.child("RatingID").getValue();

                    //convert timestamp to proper format dd/MM/yyyy
                    Calendar calendarRate = Calendar.getInstance(Locale.ENGLISH);
                    calendarRate.setTimeInMillis(Long.parseLong(RatingID));
                    String dateFormatRate = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendarRate).toString();

                    sellerTaskDetail_RatedTime.setText(dateFormatRate);

                    linearLayoutTaskReminder.setVisibility(View.GONE);
                    sellerTaskDetail_NoticePending.setText(R.string.BuyerOrdersRatedDetail_SignalTitle);
                    sellerTaskDetail_SignalLogo.setImageResource(R.drawable.ic_complete_task);

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

                    linearSellerCancelTime.setVisibility(View.VISIBLE);
                    //convert timestamp to proper format dd/MM/yyyy
                    Calendar calendarCancel = Calendar.getInstance(Locale.ENGLISH);
                    calendarCancel.setTimeInMillis(Long.parseLong(CancelID));
                    String dateFormatCancel = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendarCancel).toString();

                    sellerTaskDetail_CancelTime.setText(dateFormatCancel);
                    SReasonDesc.setText(CReason);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}