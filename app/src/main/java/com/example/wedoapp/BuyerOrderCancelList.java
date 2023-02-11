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

public class BuyerOrderCancelList extends AppCompatActivity {

    private TextView tv_NoCancelOrder;
    private ImageView iv_NoCancelOrder;
    private ImageButton CancelBackToOrderList;
    private ArrayList<Model_Order> OrderCancelledList;

    private RecyclerView recyclerViewBuyerCancelList;
    private AdapterOrderCancelled adapterOrderCancelled;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_cancel_list);

        tv_NoCancelOrder = findViewById(R.id.tv_NoCancelOrder);
        iv_NoCancelOrder = findViewById(R.id.iv_NoCancelOrder);
        CancelBackToOrderList = findViewById(R.id.CancelBackToOrderList);
        recyclerViewBuyerCancelList = findViewById(R.id.recyclerViewBuyerCancelList);

        loadRatedOrders();

        CancelBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent ratedBack2ordList = new Intent(BuyerOrderRatedList.this, Orders.class);
                //startActivity(ratedBack2ordList);
            }
        });
    }

    private void loadRatedOrders() {
        OrderCancelledList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("BuyerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderCancelledList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all pending orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Cancelled")) {
                        OrderCancelledList.add(0, model_order);
                    }
                }
                //setup adapter
                adapterOrderCancelled = new AdapterOrderCancelled(BuyerOrderCancelList.this, OrderCancelledList);
                //set adapter
                recyclerViewBuyerCancelList.setAdapter(adapterOrderCancelled);
                adapterOrderCancelled.notifyDataSetChanged();

                if(OrderCancelledList.isEmpty()){
                    tv_NoCancelOrder.setVisibility(View.VISIBLE);
                    iv_NoCancelOrder.setVisibility(View.VISIBLE);
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