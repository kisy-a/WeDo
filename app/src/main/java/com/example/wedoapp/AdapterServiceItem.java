package com.example.wedoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AdapterServiceItem extends RecyclerView.Adapter<AdapterServiceItem.HolderServiceItemList>implements Filterable {

    private Context context;
    public ArrayList<Model_Service> ServiceItemList, filterList;
    private FilterService filter;

    public AdapterServiceItem(Context context, ArrayList<Model_Service> serviceItemList){
        this.context = context;
        this.ServiceItemList = serviceItemList;
        this.filterList = serviceItemList;
    }
    @NonNull
    @Override
    public AdapterServiceItem.HolderServiceItemList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_list_buyer,parent,false);
        return new AdapterServiceItem.HolderServiceItemList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterServiceItem.HolderServiceItemList holder, int position) {
        Model_Service model_service = ServiceItemList.get(position);

        String serviceID = model_service.getSID();
        String userID = model_service.getUID();
        String sTitle = model_service.getSTitle();
        String sDescription = model_service.getSDescription();
        String sCategory = model_service.getSCategory();
        String sPrice = model_service.getSPrice();
        String sDeliveryDays = model_service.getSDeliveryDays();
        String sImage = model_service.getSImage();

        loadReview(model_service, holder);//load avg review, set to rating bar
        loadUser(model_service, holder);//load avg review, set to rating bar

        //set data
        holder.SerItem_Title.setText(sTitle);
        //holder.SerItem_Days.setText(sDeliveryDays + " Delivery Hours");
        holder.SerItem_Price.setText("PHP " + sPrice);

        try {
            Picasso.get().load(sImage).placeholder(R.drawable.ic_no_image).into(holder.SerItem_Image);
        }
        catch (Exception e) {
            holder.SerItem_Image.setImageResource(R.drawable.ic_no_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details
                Intent sIntentViewsService = new Intent(context, BuyerServiceDetail.class);
                sIntentViewsService.putExtra("SID", serviceID);
                sIntentViewsService.putExtra("UID", userID);
                context.startActivity(sIntentViewsService);

                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();

                final String timestamp = "" + System.currentTimeMillis();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ViewID", "" + timestamp);
                hashMap.put("ViewSID", "" + serviceID);
                hashMap.put("ViewBID", "" + auth.getUid());

                DatabaseReference viewUpdateRef = FirebaseDatabase.getInstance().getReference("RecentlyView");
                viewUpdateRef.child(auth.getUid()).child(serviceID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
    }

    private void loadUser(Model_Service model_service, HolderServiceItemList holder) {
        String userID = model_service.getUID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String ProfileImage = ""+ds.child("ProfileImage").getValue();

                    holder.SerItem_SellerName.setText(FullName);

                    try {
                        Picasso.get().load(ProfileImage).placeholder(R.drawable.profile).into(holder.SerItem_SellerImage);
                    }
                    catch (Exception e) {
                        holder.SerItem_SellerImage.setImageResource(R.drawable.profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private float ratingSum = 0;
    private void loadReview(Model_Service model_service, HolderServiceItemList holder) {

        String serviceID = model_service.getSID();

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Reviews");
        dr.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    ratingSum = 0;

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        float rating = Float.parseFloat("" + ds.child("ORating").getValue());
                        ratingSum = ratingSum + rating;

                    }

                    long numberOfReviews = snapshot.getChildrenCount();
                    holder.SerItem_TotalRating.setText("(" + (int) numberOfReviews + ")");


                    float avgRating = ratingSum / numberOfReviews;
                    holder.SerItem_RatingAverage.setText(String.format("%.2f", avgRating));
                    holder.SerItem_RatingBarAverage.setRating(avgRating);

                }

                else{
                    holder.SerItem_TotalRating.setText("No Rating");
                    holder.SerItem_RatingAverage.setVisibility(View.GONE);
                    //buyer_serRatingBarAverage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return ServiceItemList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterService(this, filterList);
        }
        return filter;
    }



    public class HolderServiceItemList extends RecyclerView.ViewHolder{

        ImageView SerItem_Image, SerItem_SellerImage;
        TextView SerItem_Title,SerItem_Days,SerItem_Price, SerItem_RatingAverage, SerItem_TotalRating, SerItem_SellerName;
        RatingBar SerItem_RatingBarAverage;

        public HolderServiceItemList(@NonNull View itemView) {
            super(itemView);

            SerItem_SellerName = itemView.findViewById(R.id.SerItem_SellerName);
            SerItem_SellerImage = itemView.findViewById(R.id.SerItem_SellerImage);
            SerItem_RatingAverage = itemView.findViewById(R.id.SerItem_RatingAverage);
            SerItem_TotalRating = itemView.findViewById(R.id.SerItem_TotalRating);
            SerItem_RatingBarAverage = itemView.findViewById(R.id.SerItem_RatingBarAverage);
            SerItem_Image = itemView.findViewById(R.id.SerItem_Image);
            SerItem_Title = itemView.findViewById(R.id.SerItem_Title);
            //SerItem_Days = itemView.findViewById(R.id.SerItem_Days);
            SerItem_Price = itemView.findViewById(R.id.SerItem_Price);

        }
    }
}
