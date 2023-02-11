package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class BuyerOrderInProgressList extends AppCompatActivity {

    private TextView tv_NoIPOrder;
    private ImageView iv_NoIPOrder;
    private ImageButton InProgressBackToOrderList;
    private Button btn_progressCompleted;
    private ArrayList<Model_Order> BuyerInProgressList;

    private RecyclerView recyclerViewBuyerInProgressList;
    private AdapterOrderProgressing adapterOrderProgressing;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_in_progress_list);

        tv_NoIPOrder = findViewById(R.id.tv_NoIPOrder);
        iv_NoIPOrder = findViewById(R.id.iv_NoIPOrder);
        InProgressBackToOrderList = findViewById(R.id.InProgressBackToOrderList);
        btn_progressCompleted = findViewById(R.id.btn_progressCompleted);
        recyclerViewBuyerInProgressList = findViewById(R.id.recyclerViewBuyerInProgressList);

        auth = FirebaseAuth.getInstance();

        loadProgressOrders();
        loadAdapter();

        InProgressBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadAdapter() {
        recyclerViewBuyerInProgressList.setHasFixedSize(true);
        recyclerViewBuyerInProgressList.setLayoutManager(new LinearLayoutManager(this));
        //setup adapter
        adapterOrderProgressing = new AdapterOrderProgressing(BuyerOrderInProgressList.this, BuyerInProgressList);
        //set adapter
        recyclerViewBuyerInProgressList.setAdapter(adapterOrderProgressing);
    }

    private void loadProgressOrders() {
        BuyerInProgressList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("BuyerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BuyerInProgressList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all progressing orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Progressing")) {
                        BuyerInProgressList.add(0, model_order);
                    }
                }
                adapterOrderProgressing.notifyDataSetChanged();

                if(BuyerInProgressList.isEmpty()){
                    tv_NoIPOrder.setVisibility(View.VISIBLE);
                    iv_NoIPOrder.setVisibility(View.VISIBLE);
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