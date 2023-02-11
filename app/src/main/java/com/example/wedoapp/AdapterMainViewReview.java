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

public class AdapterMainViewReview extends RecyclerView.Adapter<AdapterMainViewReview.HolderMainViewReview> {

    private Context context;
    private ArrayList<Model_Rating> MainViewReviewList;

    public AdapterMainViewReview(Context context, ArrayList<Model_Rating>mainViewReviewList){
        this.context = context;
        this.MainViewReviewList = mainViewReviewList;
    }
    @NonNull
    @Override
    public AdapterMainViewReview.HolderMainViewReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_buyer,parent,false);
        return new AdapterMainViewReview.HolderMainViewReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMainViewReview.HolderMainViewReview holder, int position) {
        Model_Rating model_rating = MainViewReviewList.get(position);

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
        holder.serReview_rating.setRating(Float.parseFloat(ORating));
        holder.serReview_comment.setText(CensorWords.censor(OComment));
        holder.serReview_date.setText(dateFormat);

        if(model_rating.getOComment().isEmpty()){
            holder.serReview_comment.setVisibility(View.GONE);
        }


    }

    private void loadUserDetail(Model_Rating model_rating, AdapterMainViewReview.HolderMainViewReview holder) {
        String BuyerID = model_rating.getBuyerID();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.orderByChild("UID").equalTo(BuyerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String FullName = ""+ds.child("FullName").getValue();
                    String BuyerImage = ""+ds.child("ProfileImage").getValue();

                    String replaceName = FullName.replaceAll("\\B\\w\\B", "*");

                    holder.serReview_buyerName.setText(replaceName);

                    try {
                        Picasso.get().load(BuyerImage).placeholder(R.drawable.profile).into(holder.serReview_buyerProfileImage);
                    }
                    catch (Exception e) {
                        holder.serReview_buyerProfileImage.setImageResource(R.drawable.profile);
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
        int limit = 2;
        if(MainViewReviewList.size() > limit){
            return limit;
        }
        else
        {
            return MainViewReviewList.size();
        }
        //return BuyerReviewList.size();
    }


    public class HolderMainViewReview extends RecyclerView.ViewHolder{

        ImageView serReview_buyerProfileImage;
        TextView serReview_buyerName, serReview_comment, serReview_date;
        RatingBar serReview_rating;

        public HolderMainViewReview(@NonNull View itemView) {
            super(itemView);

            serReview_date = itemView.findViewById(R.id.serReview_date);
            serReview_buyerProfileImage = itemView.findViewById(R.id.serReview_buyerProfileImage);
            serReview_buyerName = itemView.findViewById(R.id.serReview_buyerName);
            serReview_rating = itemView.findViewById(R.id.serReview_rating);
            serReview_comment = itemView.findViewById(R.id.serReview_comment);

        }
    }
}
