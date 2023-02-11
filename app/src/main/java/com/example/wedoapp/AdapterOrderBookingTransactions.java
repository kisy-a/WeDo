package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AdapterOrderBookingTransactions extends RecyclerView.Adapter<AdapterOrderBookingTransactions.HolderOrderBookingTransactions> {

    private Context context;
    public ArrayList<Model_Order> OrderBookingTransactionsList, filterList;
    private FilterTasks filter;

    public AdapterOrderBookingTransactions(Context context, ArrayList<Model_Order> orderBookingTransactionsList){
        this.context = context;
        this.OrderBookingTransactionsList = orderBookingTransactionsList;
    }
    @NonNull
    @Override
    public AdapterOrderBookingTransactions.HolderOrderBookingTransactions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_transactions_list,parent,false);
        return new AdapterOrderBookingTransactions.HolderOrderBookingTransactions(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderBookingTransactions.HolderOrderBookingTransactions holder, int position) {
        Model_Order model_order = OrderBookingTransactionsList.get(position);

        String buyerID = model_order.getBuyerID();
        String oDeliveryDays = model_order.getODeliveryDays();
        String serviceImage = model_order.getServiceImage();
        String oImage = model_order.getOImage();
        String oTitle = model_order.getOTitle();

        String buyerName = model_order.getBuyerName();
        String oPrice = model_order.getOPrice();
        String sellerName = model_order.getSellerName();
        String oCategory = model_order.getOCategory();
        String oPaymentMethod = model_order.getOPaymentMethod();
        String orderAcceptedTime = model_order.getAcceptedTime();
        String orderCompletedTime = model_order.getCompletedTime();
        String oStatus = model_order.getOStatus();
        String orderID = model_order.getOrderID();
        String orderDate = model_order.getOrderDate();
        String orderTime = model_order.getOrderTime();
        String serviceID = model_order.getServiceID();
        //to show the user info
        loadUserDetail(model_order, holder);
        loadServiceDetail(model_order, holder);

        //set data
        holder.orderID.setText(orderID);
        holder.orderDate.setText(orderDate);
        holder.orderTime.setText(orderTime);
        holder.orderStatus.setText(oStatus);
        holder.orderAcceptedTime.setText(orderAcceptedTime);
        holder.orderCompletedTime.setText(orderCompletedTime);
        holder.orderPaymentMethod.setText(oPaymentMethod);
    }

    private void loadServiceDetail(Model_Order model_order, HolderOrderBookingTransactions holder) {
        String serviceID = model_order.getServiceID();
        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("Services");
        servicesRef.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String SDeliveryDays = ""+ds.child("SDeliveryDays").getValue();
                    String STitle = ""+ds.child("STitle").getValue();
                    String SCategory = ""+ds.child("SCategory").getValue();
                    String SPrice = ""+ds.child("SPrice").getValue();

                    holder.orderTitle.setText(STitle);
                    holder.orderHours.setText(SDeliveryDays + " Hours");
                    holder.orderCategory.setText(SCategory);
                    holder.orderPrice.setText("PHP " + SPrice);
                    /*
                    String SImage = ""+ds.child("SImage").getValue();
                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderRated_serImage);
                    }
                    catch (Exception e) {
                        holder.orderRated_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderOrderBookingTransactions holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userSellerRef = FirebaseDatabase.getInstance().getReference("Users");
        userSellerRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String SellerEmail = ""+ds.child("EmailAddress").getValue();
                    String SellerLocation = ""+ds.child("Location").getValue();

                    holder.orderSellerName.setText(FullName);
                    holder.orderSellerEmail.setText(SellerEmail);
                    holder.orderSellerLocation.setText(SellerLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String buyerID = model_order.getBuyerID();

        DatabaseReference userBuyerRef = FirebaseDatabase.getInstance().getReference("Users");
        userBuyerRef.orderByChild("UID").equalTo(buyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String BuyerEmail = ""+ds.child("EmailAddress").getValue();
                    String BuyerLocation = ""+ds.child("Location").getValue();

                    holder.orderBuyerName.setText(FullName);
                    holder.orderBuyerEmail.setText(BuyerEmail);
                    holder.orderBuyerLocation.setText(BuyerLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderBookingTransactionsList.size();
    }


    public class HolderOrderBookingTransactions extends RecyclerView.ViewHolder{

        ImageView orderRated_serImage;
        TextView orderDate, orderTime, orderStatus, orderID,
                orderAcceptedTime,orderCompletedTime, orderPaymentMethod, orderHours,orderTitle,
                orderPrice, orderCategory, orderSellerName, orderSellerEmail, orderBuyerEmail,
                orderBuyerLocation, orderBuyerName, orderSellerLocation;

        public HolderOrderBookingTransactions(@NonNull View itemView) {
            super(itemView);
            orderRated_serImage = itemView.findViewById(R.id.orderRated_serImage);
            orderTitle = itemView.findViewById(R.id.orderTitle);

            orderSellerEmail = itemView.findViewById(R.id.orderSellerEmail);
            orderBuyerEmail = itemView.findViewById(R.id.orderBuyerEmail);
            orderBuyerLocation = itemView.findViewById(R.id.orderBuyerLocation);
            orderSellerLocation = itemView.findViewById(R.id.orderSellerLocation);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderCategory = itemView.findViewById(R.id.orderCategory);
            orderSellerName = itemView.findViewById(R.id.orderSellerName);
            orderBuyerName = itemView.findViewById(R.id.orderBuyerName);
            orderHours = itemView.findViewById(R.id.orderHours);
            orderID = itemView.findViewById(R.id.orderID);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderTime = itemView.findViewById(R.id.orderTime);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderAcceptedTime = itemView.findViewById(R.id.orderAcceptedTime);
            orderCompletedTime = itemView.findViewById(R.id.orderCompletedTime);
            orderPaymentMethod = itemView.findViewById(R.id.orderPaymentMethod);
        }
    }
}