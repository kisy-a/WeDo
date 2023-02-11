package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyerHome extends AppCompatActivity {

    private TextView tv_recentlyView, tv_lowestPrice;
    private Button btn_graphic, btn_writing, btn_video, btn_tech, btn_data, btn_others;
    private ImageButton btn_buyer_message;
    private RecyclerView recyclerViewMainCategory, recyclerViewSuggestedService, recyclerViewLowestPrice;
    private ArrayList<Model_Service> SuggestedServicesList;
    private ArrayList<Model_Service> LowestPriceList;
    private AdapterMainSuggested adapterMainSuggested;
    private AdapterMainCategory adapterMainCategory;
    private AdapterMainLowestPrice adapterMainLowestPrice;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String serviceID, oRating, buyerID, viewID ;
    int SPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        tv_lowestPrice = findViewById(R.id.tv_lowestPrice);
        recyclerViewLowestPrice = findViewById(R.id.recyclerViewLowestPrice);
        tv_recentlyView = findViewById(R.id.tv_recentlyView);
        recyclerViewSuggestedService = findViewById(R.id.recyclerViewSuggestedService);
        recyclerViewMainCategory = findViewById(R.id.recyclerViewMainCategory);
        btn_buyer_message = findViewById(R.id.btn_buyer_message);
        /*btn_graphic = findViewById(R.id.btn_graphic);
        btn_writing = findViewById(R.id.btn_writing);
        btn_video = findViewById(R.id.btn_video);
        btn_tech = findViewById(R.id.btn_tech);
        btn_data = findViewById(R.id.btn_data);
        btn_others = findViewById(R.id.btn_others);*/
        BottomNavigationView buyerNV = findViewById(R.id.buyerNavigation);
        //floating button
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View viewPopupwindow = layoutInflater.inflate(R.layout.popupwindowlayout, null);
                final PopupWindow popupWindow = new PopupWindow(viewPopupwindow, 900, 500, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                viewPopupwindow.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                //Snackbar.make(view, "Checklist", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        //set Home Selected
        buyerNV.setSelectedItemId(R.id.buyerHome);
        buyerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buyerHome:
                        return true;

                    case R.id.buyerOrder:
                        startActivity(new Intent(getApplicationContext(), Orders.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerMessage:
                        startActivity(new Intent(getApplicationContext(), ChatMessage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.buyerAccount:
                        startActivity(new Intent(getApplicationContext(), BuyerAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        auth = FirebaseAuth.getInstance();
    }
    private void checkUserStatus(){

        //get current user
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            // user sign in, stay here
            ArrayList<Model_MainCategory> MainCategoryList = new ArrayList<>();
            MainCategoryList.add(new Model_MainCategory("Hair and Makeup", "Hair and Makeup Services", R.drawable.hmua));
            MainCategoryList.add(new Model_MainCategory("Dress Tailoring", "Dress Tailoring Services", R.drawable.dresstailor));
            MainCategoryList.add(new Model_MainCategory("Car Rental", "Car Rental Services", R.drawable.carrental));
            MainCategoryList.add(new Model_MainCategory("Venue", "Venue Services", R.drawable.venue));
            MainCategoryList.add(new Model_MainCategory("Audio/Video", "Audio/Video Services", R.drawable.video));
            MainCategoryList.add(new Model_MainCategory("Catering Services", "Catering Services Services", R.drawable.catering));
            MainCategoryList.add(new Model_MainCategory("Flowers", "Flowers Services", R.drawable.flowers));

            adapterMainCategory = new AdapterMainCategory(this,MainCategoryList);
            recyclerViewMainCategory.setAdapter(adapterMainCategory);

            loadRecentlyViewValue();
            loadLowestPrice();
        }
        else{
            startActivity(new Intent(BuyerHome.this,MainActivity.class));
            finish();
        }
    }

    private void loadRecentlyViewValue() {
        auth = FirebaseAuth.getInstance();
        DatabaseReference viewReference = FirebaseDatabase.getInstance().getReference("RecentlyView").child(auth.getUid());
        viewReference.orderByChild("ViewID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        serviceID = ""+ds.child("ViewSID").getValue();
                        viewID = ""+ds.child("ViewID").getValue();
                        tv_recentlyView.setVisibility(View.VISIBLE);
                        loadRecentlyView();
                    }

                }
                else{
                    tv_recentlyView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadRecentlyView() {

        SuggestedServicesList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference serReference = FirebaseDatabase.getInstance().getReference("Services");
        serReference.orderByChild("SID").equalTo(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);
                    SuggestedServicesList.add(0,model_service);

                }

                //setup adapter
                adapterMainSuggested = new AdapterMainSuggested(BuyerHome.this, SuggestedServicesList);
                //set adapter
                recyclerViewSuggestedService.setAdapter(adapterMainSuggested);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadLowestPrice() {
        LowestPriceList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference priceReference = FirebaseDatabase.getInstance().getReference("Services");
        priceReference.orderByChild("SPrice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    LowestPriceList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Model_Service model_service = ds.getValue(Model_Service.class);
                        //LowestPriceList.add(model_service);
                        tv_lowestPrice.setVisibility(View.VISIBLE);

                        String UID = "" + ds.child("UID").getValue();
                        SPrice = Integer.parseInt((String.valueOf(ds.child("SPrice").getValue())));
                        if (!UID.equals(auth.getUid())) {
                            if (SPrice <= 300) {
                                LowestPriceList.add(0, model_service);

                            }
                        }

                    }
                }

                //setup adapter
                adapterMainLowestPrice = new AdapterMainLowestPrice(BuyerHome.this, LowestPriceList);
                //set adapter
                recyclerViewLowestPrice.setAdapter(adapterMainLowestPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        NotificationToken token1 = new NotificationToken(token);
        databaseReference.child(auth.getUid()).setValue(token1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}