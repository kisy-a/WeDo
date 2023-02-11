package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Objects;

public class ServiceCategoryAudioVideo extends AppCompatActivity {
    private TextView tv_NoDService, tv_NoDService2;
    private ImageView iv_NoDService;

    private ImageButton DataBackToMainServiceList, FilterButton;
    private ArrayList<Model_Service> ServiceItemList, filteredList;
    private RecyclerView recyclerViewDataItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText DataSearch;
    private FirebaseAuth auth;
    private String s1, s2,s3,s4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_audio_video);

        tv_NoDService = findViewById(R.id.tv_NoDService);
        tv_NoDService2 = findViewById(R.id.tv_NoDService2);
        iv_NoDService = findViewById(R.id.iv_NoDService);

        DataSearch = findViewById(R.id.DataSearch);
        DataBackToMainServiceList = findViewById(R.id.DataBackToMainServiceList);
        recyclerViewDataItemList = findViewById(R.id.recyclerViewDataItemList);
        FilterButton = findViewById(R.id.FilterButton);

        auth = FirebaseAuth.getInstance();
        s2 = "0";
        s3 = "0";
        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        s1 = sh.getString("sortby", "");
        s2 = sh.getString("minprice", "");
        s3 = sh.getString("maxprice", "");
        s4 = sh.getString("ratingby", "");

        loadServices();

        //search services
        DataSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterServiceItem.getFilter().filter(s);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        DataBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Data2Home = new Intent(ServiceCategoryAudioVideo.this, BuyerHome.class);
                startActivity(Data2Home);
                finish();
            }
        });

        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("ServiceCat", "ServiceCategoryAudioVideo");
                myEdit.apply();
                Intent Data2Filter = new Intent(ServiceCategoryAudioVideo.this, FilterServiceCategory.class);
                startActivity(Data2Filter);
            }
        });
    }

    private void loadServices() {
        ServiceItemList = new ArrayList<>();
        filteredList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Audio/Video").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                filteredList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all data Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }

                    switch (s1) {
                        case "low2high":
                            sortListByPriceAscending(ServiceItemList);
                            break;
                        case "high2low":
                            sortListByPriceDescending(ServiceItemList);
                            break;
                        case "recent":
                            sortListByRecent(ServiceItemList);
                            break;
                    }
                }
                if (!s2.equals("") && !s3.equals("")) {
                    filterList(s2, s3); //bookmark
                } else if (!s2.equals("")) {
                    filterListNoMax(s2);
                } else if (!s3.equals("")) {
                    filterListNoMin(s3);
                }
                else{
                    //setup adapter
                    adapterServiceItem = new AdapterServiceItem(ServiceCategoryAudioVideo.this, ServiceItemList);
                    //set adapter
                    recyclerViewDataItemList.setAdapter(adapterServiceItem);
                }

                if(ServiceItemList.isEmpty()){
                    tv_NoDService.setVisibility(View.VISIBLE);
                    tv_NoDService2.setVisibility(View.VISIBLE);
                    iv_NoDService.setVisibility(View.VISIBLE);
                    DataSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void filterList(String minPriceString, String maxPriceString) {
        double minPrice = Double.parseDouble(minPriceString);
        double maxPrice = Double.parseDouble(maxPriceString);
        ArrayList<Model_Service> filteredList = new ArrayList<>();

        for (Model_Service item : ServiceItemList) {
            if (Double.parseDouble(item.getSPrice()) >= minPrice && Double.parseDouble(item.getSPrice()) <= maxPrice) {
                filteredList.add(item);
            }
        }
        //setup adapter
        adapterServiceItem = new AdapterServiceItem(ServiceCategoryAudioVideo.this, filteredList);
        //set adapter
        recyclerViewDataItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoDService.setVisibility(View.VISIBLE);
            tv_NoDService2.setVisibility(View.VISIBLE);
            iv_NoDService.setVisibility(View.VISIBLE);
            DataSearch.setVisibility(View.GONE);
        }
    }
    public void filterListNoMax(String minPriceString) {
        double minPrice = Double.parseDouble(minPriceString);
        ArrayList<Model_Service> filteredList = new ArrayList<>();

        for (Model_Service item : ServiceItemList) {
            if (Double.parseDouble(item.getSPrice()) >= minPrice) {
                filteredList.add(item);
            }
        }
        //setup adapter
        adapterServiceItem = new AdapterServiceItem(ServiceCategoryAudioVideo.this, filteredList);
        //set adapter
        recyclerViewDataItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoDService.setVisibility(View.VISIBLE);
            tv_NoDService2.setVisibility(View.VISIBLE);
            iv_NoDService.setVisibility(View.VISIBLE);
            DataSearch.setVisibility(View.GONE);
        }
    }
    public void filterListNoMin(String maxPriceString) {
        double maxPrice = Double.parseDouble(maxPriceString);
        ArrayList<Model_Service> filteredList = new ArrayList<>();

        for (Model_Service item : ServiceItemList) {
            if (Double.parseDouble(item.getSPrice()) <= maxPrice) {
                filteredList.add(item);
            }
        }
        //setup adapter
        adapterServiceItem = new AdapterServiceItem(ServiceCategoryAudioVideo.this, filteredList);
        //set adapter
        recyclerViewDataItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoDService.setVisibility(View.VISIBLE);
            tv_NoDService2.setVisibility(View.VISIBLE);
            iv_NoDService.setVisibility(View.VISIBLE);
            DataSearch.setVisibility(View.GONE);
        }
    }
    private void sortListByPriceDescending(ArrayList<Model_Service> ServiceItemList) {

        Collections.sort(ServiceItemList, new SortByPrice());
    }
    private void sortListByPriceAscending(ArrayList<Model_Service> ServiceItemList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(ServiceItemList, new SortByPrice().reversed());
        }
    }

    private class SortByPrice implements java.util.Comparator<Model_Service> {
        @Override
        public int compare(Model_Service o1, Model_Service o2) {
            int price1 = Integer.parseInt(o1.getSPrice());
            int price2 = Integer.parseInt(o2.getSPrice());

            return Integer.compare(price2, price1);

        }
    }

    private void filterPrice(ArrayList<Model_Service> ServiceItemList) {

        Collections.sort(ServiceItemList, new SortByPrice());

    }
    private void sortListByRecent(ArrayList<Model_Service> ServiceItemList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(ServiceItemList, new SortByRecent().reversed());
        }
    }

    private class SortByRecent implements java.util.Comparator<Model_Service> {
        @Override
        public int compare(Model_Service o1, Model_Service o2) {
            String sid1, sid2;
            sid1 = o1.getSID().trim();
            sid2 = o2.getSID().trim();
            return sid1.compareTo(sid2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}