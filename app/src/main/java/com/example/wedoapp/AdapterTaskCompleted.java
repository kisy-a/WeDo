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

public class AdapterTaskCompleted extends RecyclerView.Adapter<AdapterTaskCompleted.HolderTaskCompleted> {

    private Context context;
    private ArrayList<Model_Order> TaskCompletedList;

    public AdapterTaskCompleted(Context context, ArrayList<Model_Order> taskCompletedList){
        this.context = context;
        this.TaskCompletedList = taskCompletedList;
    }
    @NonNull
    @Override
    public AdapterTaskCompleted.HolderTaskCompleted onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_list_seller,parent,false);
        return new AdapterTaskCompleted.HolderTaskCompleted(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTaskCompleted.HolderTaskCompleted holder, int position) {
        //get data
        Model_Order model_order = TaskCompletedList.get(position);

        String buyerName = model_order.getBuyerName();
        String oDeliveryDays = model_order.getODeliveryDays();
        String serviceImage = model_order.getServiceImage();
        String oImage = model_order.getOImage();
        String oPrice = model_order.getOPrice();
        String oStatus = model_order.getOStatus();
        String oTitle = model_order.getOTitle();
        String orderID = model_order.getOrderID();
        String userID = model_order.getBuyerID();
        String serviceID = model_order.getServiceID();
        String buyerID = model_order.getBuyerID();
        String sellerName = model_order.getSellerName();
        String oCategory = model_order.getOCategory();
        String oPaymentMethod = model_order.getOPaymentMethod();

        //to show the user info
        loadUserDetail(model_order, holder);
        loadServiceDetail(model_order, holder);

        //set data;
        holder.taskCompleted_serStatus.setText(oStatus);

        //view detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details
                Intent intentViewsPTaskDetail = new Intent(context, SellerTaskStatusDetail.class);
                intentViewsPTaskDetail.putExtra("OrderID", orderID);
                intentViewsPTaskDetail.putExtra("UID", buyerID);
                intentViewsPTaskDetail.putExtra("ServiceID", serviceID);
                context.startActivity(intentViewsPTaskDetail);
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

    private void loadServiceDetail(Model_Order model_order, HolderTaskCompleted holder) {
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

                    holder.taskCompleted_serTitle.setText(STitle);
                    holder.taskCompleted_serCategory.setText(SCategory);
                    holder.taskCompleted_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.taskCompleted_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.taskCompleted_serImage);
                    }
                    catch (Exception e) {
                        holder.taskCompleted_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderTaskCompleted holder) {
        String buyerID = model_order.getBuyerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(buyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.taskCompleted_serClientName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return TaskCompletedList.size();
    }

    public class HolderTaskCompleted extends RecyclerView.ViewHolder{

        ImageView taskCompleted_serImage;
        TextView taskCompleted_serTitle,taskCompleted_serCategory,taskCompleted_serDays,taskCompleted_serClientName, taskCompleted_serStatus, taskCompleted_serPrice;

        public HolderTaskCompleted(@NonNull View itemView) {
            super(itemView);

            taskCompleted_serImage = itemView.findViewById(R.id.taskCompleted_serImage);
            taskCompleted_serTitle = itemView.findViewById(R.id.taskCompleted_serTitle);
            taskCompleted_serCategory = itemView.findViewById(R.id.taskCompleted_serCategory);
            taskCompleted_serClientName = itemView.findViewById(R.id.taskCompleted_serClientName);
            taskCompleted_serDays = itemView.findViewById(R.id.taskCompleted_serDays);
            taskCompleted_serStatus = itemView.findViewById(R.id.taskCompleted_serStatus);
            taskCompleted_serPrice = itemView.findViewById(R.id.taskCompleted_serPrice);


        }
    }
}