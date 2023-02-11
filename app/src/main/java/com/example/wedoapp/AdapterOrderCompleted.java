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

public class AdapterOrderCompleted extends RecyclerView.Adapter<AdapterOrderCompleted.HolderOrderCompleted> {

    private Context context;
    private ArrayList<Model_Order> OrderCompletedList;

    public AdapterOrderCompleted(Context context, ArrayList<Model_Order> orderCompletedList){
        this.context = context;
        this.OrderCompletedList = orderCompletedList;
    }
    @NonNull
    @Override
    public AdapterOrderCompleted.HolderOrderCompleted onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_list_buyer,parent,false);
        return new AdapterOrderCompleted.HolderOrderCompleted(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderCompleted.HolderOrderCompleted holder, int position) {
        Model_Order model_order = OrderCompletedList.get(position);

        //holder.orderComplete_serImage.setImageResource(currentItem.getServiceImage());
        //holder.orderComplete_serTitle.setText(currentItem.getTitle());
       // holder.orderComplete_serCategory.setText(currentItem.getCategory());
        //holder.orderComplete_serClientName.setText(currentItem.getClientName());
       // holder.orderComplete_serDays.setText(currentItem.getDays());
       // holder.orderComplete_serStatus.setText(currentItem.getStatus());
       // holder.orderComplete_serPrice.setText(currentItem.getPrice());

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
        holder.orderComplete_serStatus.setText(oStatus);

        try {
            Picasso.get().load(serviceImage).placeholder(R.drawable.ic_no_image).into(holder.orderComplete_serImage);
        }
        catch (Exception e) {
            holder.orderComplete_serImage.setImageResource(R.drawable.ic_no_image);
        }

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

        holder.btn_rated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sIntentRate = new Intent(context, BuyerRateService.class);
                sIntentRate.putExtra("OrderID", orderID);
                sIntentRate.putExtra("ServiceID", serviceID);
                context.startActivity(sIntentRate);

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

    private void loadServiceDetail(Model_Order model_order, HolderOrderCompleted holder) {
        String serviceID = model_order.getServiceID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Services");
        userRef.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    String STitle = ""+ds.child("STitle").getValue();
                    String SCategory = ""+ds.child("SCategory").getValue();
                    String SDeliveryDays = ""+ds.child("SDeliveryDays").getValue();
                    String SPrice = ""+ds.child("SPrice").getValue();
                    String SImage = ""+ds.child("SImage").getValue();

                    holder.orderComplete_serTitle.setText(STitle);
                    holder.orderComplete_serCategory.setText(SCategory);
                    holder.orderComplete_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.orderComplete_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderComplete_serImage);
                    }
                    catch (Exception e) {
                        holder.orderComplete_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderOrderCompleted holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.orderComplete_serSellerName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderCompletedList.size();
    }

    public class HolderOrderCompleted extends RecyclerView.ViewHolder{

        Button btn_rated;
        ImageView orderComplete_serImage;
        TextView orderComplete_serTitle,orderComplete_serCategory,orderComplete_serSellerName,orderComplete_serDays, orderComplete_serStatus, orderComplete_serPrice;

        public HolderOrderCompleted(@NonNull View itemView) {
            super(itemView);

            btn_rated = itemView.findViewById(R.id.btn_rated);
            orderComplete_serImage = itemView.findViewById(R.id.orderComplete_serImage);
            orderComplete_serTitle = itemView.findViewById(R.id.orderComplete_serTitle);
            orderComplete_serCategory = itemView.findViewById(R.id.orderComplete_serCategory);
            orderComplete_serSellerName = itemView.findViewById(R.id.orderComplete_serSellerName);
            orderComplete_serDays = itemView.findViewById(R.id.orderComplete_serDays);
            orderComplete_serStatus = itemView.findViewById(R.id.orderComplete_serStatus);
            orderComplete_serPrice = itemView.findViewById(R.id.orderComplete_serPrice);


        }
    }
}
