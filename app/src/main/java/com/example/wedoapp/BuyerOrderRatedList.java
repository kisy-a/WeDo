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

public class BuyerOrderRatedList extends AppCompatActivity {

    private TextView tv_NoROrder;
    private ImageView iv_NoROrder;
    private ImageButton RatedBackToOrderList;
    private ArrayList<Model_Order> BuyerOrderRatedList;

    private RecyclerView recyclerViewBuyerRatedList;
    private AdapterOrderRated adapterOrderRated;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_rated_list);

        tv_NoROrder = findViewById(R.id.tv_NoROrder);
        iv_NoROrder = findViewById(R.id.iv_NoROrder);
        RatedBackToOrderList = findViewById(R.id.RatedBackToOrderList);
        recyclerViewBuyerRatedList = findViewById(R.id.recyclerViewBuyerRatedList);

        loadRatedOrders();

        RatedBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent ratedBack2ordList = new Intent(BuyerOrderRatedList.this, Orders.class);
                //startActivity(ratedBack2ordList);
            }
        });
    }

    private void loadRatedOrders() {
        BuyerOrderRatedList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("BuyerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all rated orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Rated")) {
                        BuyerOrderRatedList.add(0, model_order);
                    }
                }
                //setup adapter
                adapterOrderRated = new AdapterOrderRated(BuyerOrderRatedList.this, BuyerOrderRatedList);
                //set adapter
                recyclerViewBuyerRatedList.setAdapter(adapterOrderRated);

                if(BuyerOrderRatedList.isEmpty()){
                    tv_NoROrder.setVisibility(View.VISIBLE);
                    iv_NoROrder.setVisibility(View.VISIBLE);
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