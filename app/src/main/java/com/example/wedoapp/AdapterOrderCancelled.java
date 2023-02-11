package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOrderCancelled extends RecyclerView.Adapter<AdapterOrderCancelled.HolderOrderCancelled> {

    private Context context;
    private ArrayList<Model_Order> OrderCancelledList;

    public AdapterOrderCancelled(Context context, ArrayList<Model_Order> orderCancelledList){
        this.context = context;
        this.OrderCancelledList = orderCancelledList;
    }
    @NonNull
    @Override
    public AdapterOrderCancelled.HolderOrderCancelled onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancelled_list_buyer,parent,false);
        return new AdapterOrderCancelled.HolderOrderCancelled(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderCancelled.HolderOrderCancelled holder, int position) {
        Model_Order model_order = OrderCancelledList.get(position);

        String sellerID = model_order.getSellerID();
        String serviceID = model_order.getServiceID();
        String buyerID = model_order.getBuyerID();
        String buyerName = model_order.getBuyerName();
        String oDeliveryDays = model_order.getODeliveryDays();
        String serviceImage = model_order.getServiceImage();
        String oImage = model_order.getOImage();
        String oPrice = model_order.getOPrice();
        String oStatus = model_order.getOStatus();
        String oTitle = model_order.getOTitle();
        String orderID = model_order.getOrderID();
        String sellerName = model_order.getSellerName();
        String oCategory = model_order.getOCategory();
        String oPaymentMethod = model_order.getOPaymentMethod();

        //to show the user info
        loadUserDetail(model_order, holder);
        loadServiceDetail(model_order, holder);

        //set data
        holder.orderCancel_serStatus.setText(oStatus);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details
                Intent intentViewsCOrderDetail = new Intent(context, OrderStatusPenDetail.class);
                intentViewsCOrderDetail.putExtra("OrderID", orderID);
                intentViewsCOrderDetail.putExtra("SellerID", sellerID);
                intentViewsCOrderDetail.putExtra("ServiceID", serviceID);
                context.startActivity(intentViewsCOrderDetail);
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    v.setBackgroundColor(Color.parseColor("#f0f0f0"));
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

    }

    private void loadServiceDetail(Model_Order model_order, AdapterOrderCancelled.HolderOrderCancelled holder) {
        String serviceID = model_order.getServiceID();

        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("Services");
        servicesRef.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    String STitle = ""+ds.child("STitle").getValue();
                    String SCategory = ""+ds.child("SCategory").getValue();
                    String SDeliveryDays = ""+ds.child("SDeliveryDays").getValue();
                    String SPrice = ""+ds.child("SPrice").getValue();
                    String SImage = ""+ds.child("SImage").getValue();

                    holder.orderCancel_serTitle.setText(STitle);
                    holder.orderCancel_serCategory.setText(SCategory);
                    holder.orderCancel_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.orderCancel_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderCancel_serImage);
                    }
                    catch (Exception e) {
                        holder.orderCancel_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, AdapterOrderCancelled.HolderOrderCancelled holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.orderCancel_serSellerName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderCancelledList.size();
    }

    public class HolderOrderCancelled extends RecyclerView.ViewHolder{

        ImageView orderCancel_serImage;
        TextView orderCancel_serTitle,orderCancel_serCategory,orderCancel_serSellerName,orderCancel_serDays, orderCancel_serStatus, orderCancel_serPrice;

        public HolderOrderCancelled(@NonNull View itemView) {
            super(itemView);
            orderCancel_serImage = itemView.findViewById(R.id.orderCancel_serImage);
            orderCancel_serTitle = itemView.findViewById(R.id.orderCancel_serTitle);
            orderCancel_serCategory = itemView.findViewById(R.id.orderCancel_serCategory);
            orderCancel_serSellerName = itemView.findViewById(R.id.orderCancel_serSellerName);
            orderCancel_serDays = itemView.findViewById(R.id.orderCancel_serDays);
            orderCancel_serStatus = itemView.findViewById(R.id.orderCancel_serStatus);
            orderCancel_serPrice = itemView.findViewById(R.id.orderCancel_serPrice);


        }
    }
}