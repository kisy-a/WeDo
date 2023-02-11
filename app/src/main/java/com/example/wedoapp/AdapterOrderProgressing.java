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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOrderProgressing extends RecyclerView.Adapter<AdapterOrderProgressing.HolderOrderProgressing> {

    private Context context;
    private ArrayList<Model_Order> OrderProgressingList;

    public AdapterOrderProgressing(Context context, ArrayList<Model_Order> orderProgressingList){
        this.context = context;
        this.OrderProgressingList = orderProgressingList;
    }
    @NonNull
    @Override
    public AdapterOrderProgressing.HolderOrderProgressing onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressing_list_buyer,parent,false);
        return new AdapterOrderProgressing.HolderOrderProgressing(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderProgressing.HolderOrderProgressing holder, int position) {
        //get data
        Model_Order model_order = OrderProgressingList.get(position);

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
        holder.orderProgress_serStatus.setText(oStatus);

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

        holder.btn_progressCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String timestamp = "" + System.currentTimeMillis();
                String orderID = model_order.getOrderID();
                String oStatus = "Completed";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
                databaseReference.child(orderID).child("OStatus").setValue(oStatus);
                databaseReference.child(orderID).child("CompletedTime").setValue(timestamp);

                //Intent sIntentToRate = new Intent(context, BuyerOrderCompletedList.class);
                //context.startActivity(sIntentToRate);

                Toast.makeText(context,"Order Completed",Toast.LENGTH_SHORT).show();
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

    private void loadServiceDetail(Model_Order model_order, HolderOrderProgressing holder) {
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

                    holder.orderProgress_serTitle.setText(STitle);
                    holder.orderProgress_serCategory.setText(SCategory);
                    holder.orderProgress_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.orderProgress_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderProgress_serImage);
                    }
                    catch (Exception e) {
                        holder.orderProgress_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderOrderProgressing holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.orderProgress_serSellerName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderProgressingList.size();
    }

    public class HolderOrderProgressing extends RecyclerView.ViewHolder{

        Button btn_progressCompleted;
        ImageView orderProgress_serImage;
        TextView orderProgress_serTitle,orderProgress_serCategory,orderProgress_serSellerName,orderProgress_serDays, orderProgress_serStatus, orderProgress_serPrice;

        public HolderOrderProgressing(@NonNull View itemView) {
            super(itemView);

            btn_progressCompleted = itemView.findViewById(R.id.btn_progressCompleted);
            orderProgress_serImage = itemView.findViewById(R.id.orderProgress_serImage);
            orderProgress_serTitle = itemView.findViewById(R.id.orderProgress_serTitle);
            orderProgress_serCategory = itemView.findViewById(R.id.orderProgress_serCategory);
            orderProgress_serSellerName = itemView.findViewById(R.id.orderProgress_serSellerName);
            orderProgress_serDays = itemView.findViewById(R.id.orderProgress_serDays);
            orderProgress_serStatus = itemView.findViewById(R.id.orderProgress_serStatus);
            orderProgress_serPrice = itemView.findViewById(R.id.orderProgress_serPrice);


        }
    }
}