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

public class BuyerOrderPendingList extends AppCompatActivity {

    private TextView tv_NoPOrder;
    private ImageView iv_NoPOrder;

    private ImageButton PendingBackToOrderList;
    private RecyclerView recyclerViewBuyerPendingList;
    private ArrayList<Model_Order> OrderPendingList;
    private AdapterOrderPending adapterOrderPending;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_pending_list);

        tv_NoPOrder = findViewById(R.id.tv_NoPOrder);
        iv_NoPOrder = findViewById(R.id.iv_NoPOrder);
        PendingBackToOrderList = findViewById(R.id.PendingBackToOrderList);
        recyclerViewBuyerPendingList = findViewById(R.id.recyclerViewBuyerPendingList);

        auth = FirebaseAuth.getInstance();

        loadPendingOrders();

        PendingBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent pendBack2ordList = new Intent(BuyerOrderPendingList.this, Orders.class);
                //startActivity(pendBack2ordList);
            }
        });
    }

    private void loadPendingOrders() {
        OrderPendingList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("BuyerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrderPendingList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all pending orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Pending")) {
                        OrderPendingList.add(0,model_order);
                    }
                }
                //setup adapter
                adapterOrderPending = new AdapterOrderPending(BuyerOrderPendingList.this, OrderPendingList);
                //set adapter
                recyclerViewBuyerPendingList.setAdapter(adapterOrderPending);
                adapterOrderPending.notifyDataSetChanged();

                if(OrderPendingList.isEmpty()){
                    tv_NoPOrder.setVisibility(View.VISIBLE);
                    iv_NoPOrder.setVisibility(View.VISIBLE);
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