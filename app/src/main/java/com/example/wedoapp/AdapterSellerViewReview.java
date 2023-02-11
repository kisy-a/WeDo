package com.example.wedoapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.util.Calendar;
import java.util.Locale;

public class AdapterSellerViewReview extends RecyclerView.Adapter<AdapterSellerViewReview.HolderSellerReview> {

    private Context context;
    private ArrayList<Model_Rating> SellerReviewList;

    public AdapterSellerViewReview(Context context, ArrayList<Model_Rating>sellerReviewList){
        this.context = context;
        this.SellerReviewList = sellerReviewList;
    }
    @NonNull
    @Override
    public AdapterSellerViewReview.HolderSellerReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_seller,parent,false);
        return new AdapterSellerViewReview.HolderSellerReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSellerViewReview.HolderSellerReview holder, int position) {
        Model_Rating model_rating = SellerReviewList.get(position);

        String ORating = model_rating.getORating();
        String OComment = model_rating.getOComment();
        String RatingID = model_rating.getRatingID();

        //to show the user info for those rate the service
        loadUserDetail(model_rating, holder);

        //convert timestamp to proper format dd/MM/yyyy
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(RatingID));
        String dateFormat = DateFormat.format("dd-MM-yyyy hh:mm aa" ,calendar).toString();

        //set data
        holder.serReviewHistory_rating.setRating(Float.parseFloat(ORating));
        holder.serReviewHistory_comment.setText(CensorWords.censor(OComment));
        holder.serReviewHistory_date.setText(dateFormat);

        if(model_rating.getOComment().isEmpty()){
            holder.serReviewHistory_comment.setVisibility(View.GONE);
        }
    }

    private void loadUserDetail(Model_Rating model_rating, HolderSellerReview holder) {
        String BuyerID = model_rating.getBuyerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(BuyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String BuyerImage = ""+ds.child("ProfileImage").getValue();

                    String replaceName = FullName.replaceAll("\\B\\w\\B", "*");

                    holder.serReviewHistory_buyerName.setText(replaceName);

                    try {
                        Picasso.get().load(BuyerImage).placeholder(R.drawable.profile).into(holder.serReviewHistory_buyerProfileImage);
                    }
                    catch (Exception e) {
                        holder.serReviewHistory_buyerProfileImage.setImageResource(R.drawable.profile);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return SellerReviewList.size();
    }

    public class HolderSellerReview extends RecyclerView.ViewHolder{

        ImageView serReviewHistory_buyerProfileImage;
        TextView serReviewHistory_buyerName, serReviewHistory_comment, serReviewHistory_date;
        RatingBar serReviewHistory_rating;

        public HolderSellerReview(@NonNull View itemView) {
            super(itemView);

            serReviewHistory_date = itemView.findViewById(R.id.serReviewHistory_date);
            serReviewHistory_buyerProfileImage = itemView.findViewById(R.id.serReviewHistory_buyerProfileImage);
            serReviewHistory_buyerName = itemView.findViewById(R.id.serReviewHistory_buyerName);
            serReviewHistory_rating = itemView.findViewById(R.id.serReviewHistory_rating);
            serReviewHistory_comment = itemView.findViewById(R.id.serReviewHistory_comment);




        }
    }
}