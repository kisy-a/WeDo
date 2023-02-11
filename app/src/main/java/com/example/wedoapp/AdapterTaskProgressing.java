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

public class AdapterTaskProgressing extends RecyclerView.Adapter<AdapterTaskProgressing.HolderTaskProgressing> {

    private Context context;
    private ArrayList<Model_Order> TaskProgressingList;

    public AdapterTaskProgressing(Context context, ArrayList<Model_Order> taskProgressingList){
        this.context = context;
        this.TaskProgressingList = taskProgressingList;
    }
    @NonNull
    @Override
    public AdapterTaskProgressing.HolderTaskProgressing onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_list_seller,parent,false);
        return new AdapterTaskProgressing.HolderTaskProgressing(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTaskProgressing.HolderTaskProgressing holder, int position) {
        //get data
        Model_Order model_order = TaskProgressingList.get(position);

        String buyerName = model_order.getBuyerName();
        String oDeliveryDays = model_order.getODeliveryDays();
        String serviceImage = model_order.getServiceImage();
        String oImage = model_order.getOImage();
        String oPrice = model_order.getOPrice();
        String oStatus = model_order.getOStatus();
        String oTitle = model_order.getOTitle();
        String orderID = model_order.getOrderID();
        String userID = model_order.getBuyerID();
        String buyerID = model_order.getBuyerID();
        String sellerName = model_order.getSellerName();
        String oCategory = model_order.getOCategory();
        String oPaymentMethod = model_order.getOPaymentMethod();
        String serviceID = model_order.getServiceID();

        //to show the user info
        loadUserDetail(model_order, holder);
        loadServiceDetail(model_order, holder);

        //set data
        holder.taskAccept_serStatus.setText(oStatus);

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

    private void loadServiceDetail(Model_Order model_order, HolderTaskProgressing holder) {
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

                    holder.taskAccept_serTitle.setText(STitle);
                    holder.taskAccept_serCategory.setText(SCategory);
                    holder.taskAccept_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.taskAccept_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.taskAccept_serImage);
                    }
                    catch (Exception e) {
                        holder.taskAccept_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderTaskProgressing holder) {
        String buyerID = model_order.getBuyerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(buyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.taskAccept_serClientName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return TaskProgressingList.size();
    }

    public class HolderTaskProgressing extends RecyclerView.ViewHolder{

        ImageView taskAccept_serImage;
        TextView taskAccept_serTitle,taskAccept_serCategory,taskAccept_serDays,taskAccept_serClientName, taskAccept_serStatus, taskAccept_serPrice;

        public HolderTaskProgressing(@NonNull View itemView) {
            super(itemView);

            taskAccept_serImage = itemView.findViewById(R.id.taskAccept_serImage);
            taskAccept_serTitle = itemView.findViewById(R.id.taskAccept_serTitle);
            taskAccept_serCategory = itemView.findViewById(R.id.taskAccept_serCategory);
            taskAccept_serClientName = itemView.findViewById(R.id.taskAccept_serClientName);
            taskAccept_serDays = itemView.findViewById(R.id.taskAccept_serDays);
            taskAccept_serStatus = itemView.findViewById(R.id.taskAccept_serStatus);
            taskAccept_serPrice = itemView.findViewById(R.id.taskAccept_serPrice);


        }
    }
}