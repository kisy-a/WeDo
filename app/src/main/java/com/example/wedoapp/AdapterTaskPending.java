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

public class AdapterTaskPending extends RecyclerView.Adapter<AdapterTaskPending.HolderTaskPending> {

    private Context context;
    private ArrayList<Model_Order> TaskPendingList;

    public AdapterTaskPending(Context context, ArrayList<Model_Order> taskPendingList){
        this.context = context;
        this.TaskPendingList = taskPendingList;
    }
    @NonNull
    @Override
    public AdapterTaskPending.HolderTaskPending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_list_seller,parent,false);
        return new AdapterTaskPending.HolderTaskPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTaskPending.HolderTaskPending holder, int position) {
        //get data
        Model_Order model_order = TaskPendingList.get(position);

        String buyerName = model_order.getBuyerName();
        String oDeliveryDays = model_order.getODeliveryDays();
        String serviceImage = model_order.getServiceImage();
        String oImage = model_order.getOImage();
        String serviceID = model_order.getServiceID();
        String oPrice = model_order.getOPrice();
        String oStatus = model_order.getOStatus();
        String oTitle = model_order.getOTitle();
        String orderID = model_order.getOrderID();
        String buyerID = model_order.getBuyerID();
        String sellerName = model_order.getSellerName();
        String oCategory = model_order.getOCategory();
        String oPaymentMethod = model_order.getOPaymentMethod();

        //to show the user info
        loadUserDetail(model_order, holder);
        loadServiceDetail(model_order, holder);

        //set data
        holder.taskPending_serStatus.setText(oStatus);

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

        holder.btn_viewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntentViewsPayment = new Intent(context, PaymentStatement.class);
                sIntentViewsPayment.putExtra("OrderID", orderID);
                context.startActivity(sIntentViewsPayment);
            }
        });

        holder.btn_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String timestamp = "" + System.currentTimeMillis();
                String orderID = model_order.getOrderID();
                String oStatus = "Progressing";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
                databaseReference.child(orderID).child("OStatus").setValue(oStatus);
                databaseReference.child(orderID).child("AcceptedTime").setValue(timestamp);

                //Intent toProgress = new Intent(context, SellerTaskAcceptedList.class);
                //context.startActivity(toProgress);

                Toast.makeText(context,"Order Accepted",Toast.LENGTH_SHORT).show();
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

    private void loadServiceDetail(Model_Order model_order, HolderTaskPending holder) {
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

                    holder.taskPending_serTitle.setText(STitle);
                    holder.taskPending_serCategory.setText(SCategory);
                    holder.taskPending_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.taskPending_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.taskPending_serImage);
                    }
                    catch (Exception e) {
                        holder.taskPending_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderTaskPending holder) {
        String buyerID = model_order.getBuyerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(buyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.taskPending_serClientName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return TaskPendingList.size();
    }

    public class HolderTaskPending extends RecyclerView.ViewHolder{

        Button btn_viewPayment, btn_accepted;
        ImageView taskPending_serImage;
        TextView taskPending_serTitle,taskPending_serCategory,taskPending_serDays,taskPending_serClientName, taskPending_serStatus, taskPending_serPrice;

        public HolderTaskPending(@NonNull View itemView) {
            super(itemView);

            btn_viewPayment = itemView.findViewById(R.id.btn_viewPayment);
            btn_accepted = itemView.findViewById(R.id.btn_accepted);
            taskPending_serImage = itemView.findViewById(R.id.taskPending_serImage);
            taskPending_serTitle = itemView.findViewById(R.id.taskPending_serTitle);
            taskPending_serCategory = itemView.findViewById(R.id.taskPending_serCategory);
            taskPending_serClientName = itemView.findViewById(R.id.taskPending_serClientName);
            taskPending_serDays = itemView.findViewById(R.id.taskPending_serDays);
            taskPending_serStatus = itemView.findViewById(R.id.taskPending_serStatus);
            taskPending_serPrice = itemView.findViewById(R.id.taskPending_serPrice);
        }
    }
}
