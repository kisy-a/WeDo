package com.example.wedoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
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

public class AdapterOrderRated extends RecyclerView.Adapter<AdapterOrderRated.HolderOrderRated> {

    private Context context;
    private ArrayList<Model_Order> OrderRatedList;

    public AdapterOrderRated(Context context, ArrayList<Model_Order> orderRatedList){
        this.context = context;
        this.OrderRatedList = orderRatedList;
    }
    @NonNull
    @Override
    public AdapterOrderRated.HolderOrderRated onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rated_list_buyer,parent,false);
        return new AdapterOrderRated.HolderOrderRated(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderRated.HolderOrderRated holder, int position) {
        Model_Order model_order = OrderRatedList.get(position);

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
        holder.orderRated_serStatus.setText(oStatus);
    }

    private void loadServiceDetail(Model_Order model_order, HolderOrderRated holder) {
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

                    holder.orderRated_serTitle.setText(STitle);
                    holder.orderRated_serCategory.setText(SCategory);
                    holder.orderRated_serDays.setText(SDeliveryDays + " Delivery Hours");
                    holder.orderRated_serPrice.setText("PHP " + SPrice);

                    try {
                        Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.orderRated_serImage);
                    }
                    catch (Exception e) {
                        holder.orderRated_serImage.setImageResource(R.drawable.ic_no_image);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserDetail(Model_Order model_order, HolderOrderRated holder) {
        String SellerID = model_order.getSellerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();

                    holder.orderRated_serSellerName.setText(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return OrderRatedList.size();
    }

    public class HolderOrderRated extends RecyclerView.ViewHolder{

        ImageView orderRated_serImage;
        TextView orderRated_serTitle,orderRated_serCategory,orderRated_serSellerName,orderRated_serDays, orderRated_serStatus, orderRated_serPrice;

        public HolderOrderRated(@NonNull View itemView) {
            super(itemView);
            orderRated_serImage = itemView.findViewById(R.id.orderRated_serImage);
            orderRated_serTitle = itemView.findViewById(R.id.orderRated_serTitle);
            orderRated_serCategory = itemView.findViewById(R.id.orderRated_serCategory);
            orderRated_serSellerName = itemView.findViewById(R.id.orderRated_serSellerName);
            orderRated_serDays = itemView.findViewById(R.id.orderRated_serDays);
            orderRated_serStatus = itemView.findViewById(R.id.orderRated_serStatus);
            orderRated_serPrice = itemView.findViewById(R.id.orderRated_serPrice);


        }
    }
}