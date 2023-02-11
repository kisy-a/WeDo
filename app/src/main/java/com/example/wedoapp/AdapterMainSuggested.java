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

public class AdapterMainSuggested extends RecyclerView.Adapter<AdapterMainSuggested.HolderViewSuggestedServices> {

    private Context context;
    private ArrayList<Model_Service> SuggestedServicesList;

    public AdapterMainSuggested(Context context, ArrayList<Model_Service>suggestedServicesList){
        this.context = context;
        this.SuggestedServicesList = suggestedServicesList;
    }
    @NonNull
    @Override
    public AdapterMainSuggested.HolderViewSuggestedServices onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_suggested_service,parent,false);
        return new AdapterMainSuggested.HolderViewSuggestedServices(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMainSuggested.HolderViewSuggestedServices holder, int position) {
        Model_Service model_service = SuggestedServicesList.get(position);

        String userID = model_service.getUID();
        String serviceID = model_service.getSID();
        String SCategory = model_service.getSCategory();
        String STitle = model_service.getSTitle();
        String SPrice = model_service.getSPrice();
        String SImage = model_service.getSImage();

        loadServiceReview(model_service, holder);//load avg review, set to rating bar

        //set data
        //holder.serReview_rating.setRating(Float.parseFloat(ORating));
        holder.Suggested_SerItem_Category.setText(SCategory);
        holder.Suggested_SerItem_Title.setText(CensorWords.censor(STitle));
        holder.Suggested_SerItem_Price.setText("PHP " + SPrice);

        try {
            Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(holder.Suggested_SerItem_Image);
        }
        catch (Exception e) {
            holder.Suggested_SerItem_Image.setImageResource(R.drawable.ic_no_image);
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
    private void loadServiceReview(Model_Service model_service, AdapterMainSuggested.HolderViewSuggestedServices holder) {
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
                    holder.Suggested_SerItem_TotalRating.setText("(" + (int) numberOfReviews + ")");


                    float avgRating = ratingSum / numberOfReviews;
                    holder.Suggested_SerItem_RatingAverage.setText(String.format("%.2f", avgRating));
                    holder.Suggested_SerItem_RatingBarAverage.setRating(avgRating);
                }

                else{
                    holder.Suggested_SerItem_TotalRating.setText("No Rating");
                    holder.Suggested_SerItem_RatingAverage.setVisibility(View.GONE);
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
        int limit = 10;
        if(SuggestedServicesList.size() > limit){
            return limit;
        }
        else
        {
            return SuggestedServicesList.size();
        }
        //return SuggestedServicesList.size();
    }


    public class HolderViewSuggestedServices extends RecyclerView.ViewHolder{

        TextView Suggested_SerItem_Category, Suggested_SerItem_Title, Suggested_SerItem_Price, Suggested_SerItem_RatingAverage, Suggested_SerItem_TotalRating;
        RatingBar Suggested_SerItem_RatingBarAverage;
        ImageView Suggested_SerItem_Image;

        public HolderViewSuggestedServices(@NonNull View itemView) {
            super(itemView);

            Suggested_SerItem_Category = itemView.findViewById(R.id.Suggested_SerItem_Category);
            Suggested_SerItem_Title = itemView.findViewById(R.id.Suggested_SerItem_Title);
            Suggested_SerItem_Price = itemView.findViewById(R.id.Suggested_SerItem_Price);
            Suggested_SerItem_RatingAverage = itemView.findViewById(R.id.Suggested_SerItem_RatingAverage);
            Suggested_SerItem_TotalRating = itemView.findViewById(R.id.Suggested_SerItem_TotalRating);
            Suggested_SerItem_RatingBarAverage = itemView.findViewById(R.id.Suggested_SerItem_RatingBarAverage);
            Suggested_SerItem_Image = itemView.findViewById(R.id.Suggested_SerItem_Image);

        }
    }
}
