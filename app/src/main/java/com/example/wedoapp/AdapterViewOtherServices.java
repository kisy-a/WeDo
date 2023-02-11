package com.example.wedoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import java.util.HashMap;

public class AdapterViewOtherServices extends RecyclerView.Adapter<AdapterViewOtherServices.HolderViewOtherServices> {

    private Context context;
    private ArrayList<Model_Service> OtherServicesList;

    public AdapterViewOtherServices(Context context, ArrayList<Model_Service>otherServicesList){
        this.context = context;
        this.OtherServicesList = otherServicesList;
    }
    @NonNull
    @Override
    public AdapterViewOtherServices.HolderViewOtherServices onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_other_services,parent,false);
        return new AdapterViewOtherServices.HolderViewOtherServices(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewOtherServices.HolderViewOtherServices holder, int position) {
        Model_Service model_service = OtherServicesList.get(position);

        String userID = model_service.getUID();
        String serviceID = model_service.getSID();
        String SCategory = model_service.getSCategory();
        String STitle = model_service.getSTitle();
        String SPrice = model_service.getSPrice();
        String SImage = model_service.getSImage();

        loadServiceReview(model_service, holder);//load avg review, set to rating bar

        //set data
        //holder.serReview_rating.setRating(Float.parseFloat(ORating));
        holder.ViewAll_SerItem_Category.setText(CensorWords.censor(SCategory));
        holder.ViewAll_SerItem_Title.setText(CensorWords.censor(STitle));
        holder.ViewAll_SerItem_Price.setText("PHP " + SPrice);

        try {
            Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.ViewAll_SerItem_Image);
        }
        catch (Exception e) {
            holder.ViewAll_SerItem_Image.setImageResource(R.drawable.ic_no_image);
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

    private float ratingSum = 0;
    private void loadServiceReview(Model_Service model_service, HolderViewOtherServices holder) {
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
                    holder.ViewAll_SerItem_TotalRating.setText("(" + (int) numberOfReviews + ")");


                    float avgRating = ratingSum / numberOfReviews;
                    holder.ViewAll_SerItem_RatingAverage.setText(String.format("%.2f", avgRating));
                    holder.ViewAll_SerItem_RatingBarAverage.setRating(avgRating);
                }

                else{
                    holder.ViewAll_SerItem_TotalRating.setText("No Rating");
                    holder.ViewAll_SerItem_RatingAverage.setVisibility(View.GONE);
                }
            }

            //buyer_serRatingAverage.setVisibility(View.GONE);
            //buyer_serRatingBarAverage.setVisibility(View.GONE);

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        int limit = 5;
        if(OtherServicesList.size() > limit){
            return limit;
        }
        else
        {
            return OtherServicesList.size();
        }
        //return BuyerReviewList.size();
    }


    public class HolderViewOtherServices extends RecyclerView.ViewHolder{

        TextView ViewAll_SerItem_Category, ViewAll_SerItem_Title, ViewAll_SerItem_Price, ViewAll_SerItem_TotalRating, ViewAll_SerItem_RatingAverage;
        RatingBar ViewAll_SerItem_RatingBarAverage;
        ImageView ViewAll_SerItem_Image;

        public HolderViewOtherServices(@NonNull View itemView) {
            super(itemView);

            ViewAll_SerItem_RatingBarAverage = itemView.findViewById(R.id.ViewAll_SerItem_RatingBarAverage);
            ViewAll_SerItem_Image = itemView.findViewById(R.id.ViewAll_SerItem_Image);
            ViewAll_SerItem_Price = itemView.findViewById(R.id.ViewAll_SerItem_Price);
            ViewAll_SerItem_TotalRating = itemView.findViewById(R.id.ViewAll_SerItem_TotalRating);
            ViewAll_SerItem_Category = itemView.findViewById(R.id.ViewAll_SerItem_Category);
            ViewAll_SerItem_RatingAverage = itemView.findViewById(R.id.ViewAll_SerItem_RatingAverage);
            ViewAll_SerItem_Title = itemView.findViewById(R.id.ViewAll_SerItem_Title);

        }
    }
}
