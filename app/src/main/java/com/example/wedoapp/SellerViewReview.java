package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SellerViewReview extends AppCompatActivity {

    private TextView tv_NoReviewSeller;
    private ImageView iv_NoReviewSeller;

    private ImageButton ReviewBackToManageSer;

    private RecyclerView recyclerViewSellerReview;
    private AdapterSellerViewReview adapterSellerViewReview;
    private ArrayList<Model_Rating> SellerReviewList;

    private FirebaseAuth auth;
    private String serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_review);

        iv_NoReviewSeller = findViewById(R.id.iv_NoReviewSeller);
        tv_NoReviewSeller = findViewById(R.id.tv_NoReviewSeller);

        ReviewBackToManageSer = findViewById(R.id.ReviewBackToManageSer);
        recyclerViewSellerReview = findViewById(R.id.recyclerViewSellerReview);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");
        loadReviewHistory();

        ReviewBackToManageSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadReviewHistory() {
        SellerReviewList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");
        databaseReference.orderByChild("ServiceID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Rating model_rating = ds.getValue(Model_Rating.class);
                    SellerReviewList.add(model_rating);

                }
                //setup adapter
                adapterSellerViewReview = new AdapterSellerViewReview(SellerViewReview.this, SellerReviewList);
                //set adapter
                recyclerViewSellerReview.setAdapter(adapterSellerViewReview);

                if(SellerReviewList.isEmpty()){
                    tv_NoReviewSeller.setVisibility(View.VISIBLE);
                    iv_NoReviewSeller.setVisibility(View.VISIBLE);
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