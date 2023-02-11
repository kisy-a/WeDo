package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AdapterOrderPending extends RecyclerView.Adapter<AdapterOrderPending.HolderOrderPending> {

    private Context context;
    private ArrayList<Model_Order> OrderPendingList;

    public AdapterOrderPending(Context context, ArrayList<Model_Order> orderPendingList){
        this.context = context;
        this.OrderPendingList = orderPendingList;
    }
    @NonNull
    @Override
    public AdapterOrderPending.HolderOrderPending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_list_buyer,parent,false);
        return new AdapterOrderPending.HolderOrderPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderPending.HolderOrderPending holder, int position) {
        //get data
        Model_Order model_order = OrderPendingList.get(position);

        String buyerID = model_order.getBuyerID();
        String sellerID = model_order.getSellerID();
        String serviceID = model_order.getServiceID();
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
        holder.orderPending_serStatus.setText(oStatus);

        //view detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details
                Intent intentViewsPOrderDetail = new Intent(context, OrderStatusPenDetail.class);
                intentViewsPOrderDetail.putExtra("OrderID", orderID);
                intentViewsPOrderDetail.putExtra("SellerID", sellerID);
                intentViewsPOrderDetail.putExtra("ServiceID", serviceID);
                context.startActivity(intentViewsPOrderDetail);
            }
        });

        holder.btn_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sIntentCancelOrder = new Intent(context, BuyerCancelOrder.class);
                sIntentCancelOrder.putExtra("OrderID", orderID);
                sIntentCancelOrder.putExtra("ServiceID", serviceID);
                context.startActivity(sIntentCancelOrder);

                notifyDataSetChanged();

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

    private void loadServiceDetail(Model_Order model_order, HolderOrderPending holder) {
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

                    holder.orderPending_serTitle.setText(STitle);
                    holder.orderPending_serCategory.setText(SCategory);
                    holder.orderPending_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.orderPending_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderPending_serImage);
                    }
                    catch (Exception e) {
                        holder.orderPending_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderOrderPending holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.orderPending_serSellerName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return OrderPendingList.size();
    }

    public class HolderOrderPending extends RecyclerView.ViewHolder{

        ImageView orderPending_serImage;
        Button btn_cancelOrder;
        TextView orderPending_serTitle,orderPending_serCategory,orderPending_serSellerName,orderPending_serDays, orderPending_serStatus, orderPending_serPrice;

        public HolderOrderPending(@NonNull View itemView) {
            super(itemView);

            btn_cancelOrder = itemView.findViewById(R.id.btn_cancelOrder);
            orderPending_serImage = itemView.findViewById(R.id.orderPending_serImage);
            orderPending_serTitle = itemView.findViewById(R.id.orderPending_serTitle);
            orderPending_serCategory = itemView.findViewById(R.id.orderPending_serCategory);
            orderPending_serSellerName = itemView.findViewById(R.id.orderPending_serSellerName);
            orderPending_serDays = itemView.findViewById(R.id.orderPending_serDays);
            orderPending_serStatus = itemView.findViewById(R.id.orderPending_serStatus);
            orderPending_serPrice = itemView.findViewById(R.id.orderPending_serPrice);


        }
    }
}