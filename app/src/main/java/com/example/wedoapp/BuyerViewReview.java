package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class BuyerViewReview extends AppCompatActivity {

    private TextView tv_NoReviewBuyer;
    private ImageView iv_NoReviewBuyer;

    private ImageButton ReviewBackToSerDetail;
    private ArrayList<Model_Rating> BuyerReviewList;
    private RecyclerView recyclerViewBuyerReview;
    private AdapterBuyerViewReview adapterBuyerViewReview;

    private FirebaseAuth auth;
    private String serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_view_review);

        iv_NoReviewBuyer = findViewById(R.id.iv_NoReviewBuyer);
        tv_NoReviewBuyer = findViewById(R.id.tv_NoReviewBuyer);

        ReviewBackToSerDetail = findViewById(R.id.ReviewBackToSerDetail);
        recyclerViewBuyerReview = findViewById(R.id.recyclerViewBuyerReview);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");

        loadReview();

        //set divider
        DividerItemDecoration divider = new DividerItemDecoration(recyclerViewBuyerReview.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider)));
        recyclerViewBuyerReview.addItemDecoration(divider);

        ReviewBackToSerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadReview() {
        BuyerReviewList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");
        databaseReference.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Rating model_rating = ds.getValue(Model_Rating.class);
                    BuyerReviewList.add(model_rating);

                }
                //setup adapter
                adapterBuyerViewReview = new AdapterBuyerViewReview(BuyerViewReview.this, BuyerReviewList);
                //set adapter
                recyclerViewBuyerReview.setAdapter(adapterBuyerViewReview);

                if(BuyerReviewList.isEmpty()){
                    tv_NoReviewBuyer.setVisibility(View.VISIBLE);
                    iv_NoReviewBuyer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}