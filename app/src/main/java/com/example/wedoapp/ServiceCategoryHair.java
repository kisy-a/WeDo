package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ServiceCategoryHair extends AppCompatActivity {

    private TextView tv_NoGService, tv_NoGService2;
    private ImageView iv_NoGService;

    private ImageButton BackToMainServiceList, FilterButton;
    private ArrayList<Model_Service> ServiceItemList, filteredList;
    private RecyclerView recyclerViewServiceItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText GraphicSearch;
    private FirebaseAuth auth;
    private String s1, s2,s3,s4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_hair_makeup);

        FilterButton = findViewById(R.id.FilterButton);
        tv_NoGService = findViewById(R.id.tv_NoGService);
        tv_NoGService2 = findViewById(R.id.tv_NoGService2);
        iv_NoGService = findViewById(R.id.iv_NoGService);

        GraphicSearch = findViewById(R.id.GraphicSearch);
        BackToMainServiceList = findViewById(R.id.BackToMainServiceList);
        recyclerViewServiceItemList = findViewById(R.id.recyclerViewServiceItemList);

        auth = FirebaseAuth.getInstance();
        s2 = "0";
        s3 = "0";

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        s1 = sh.getString("sortby", "");
        s2 = sh.getString("minprice", "");
        s3 = sh.getString("maxprice", "");
        s4 = sh.getString("ratingby", "");

        loadServices();

        GraphicSearch.addTextChangedListener(new TextWatcher() {
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

        BackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Data2Home = new Intent(ServiceCategoryHair.this, BuyerHome.class);
                startActivity(Data2Home);
                finish();
            }
        });
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("ServiceCat", "ServiceCategoryHair");
                myEdit.apply();
                Intent Data2Filter = new Intent(ServiceCategoryHair.this, FilterServiceCategory.class);
                startActivity(Data2Filter);
            }
        });
    }

    private void loadServices() {
        ServiceItemList = new ArrayList<>();
        filteredList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Hair and Makeup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                filteredList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all graphic Services except from current login user
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
                    adapterServiceItem = new AdapterServiceItem(ServiceCategoryHair.this, ServiceItemList);
                    //set adapter
                    recyclerViewServiceItemList.setAdapter(adapterServiceItem);
                }

                if(ServiceItemList.isEmpty()){
                    tv_NoGService.setVisibility(View.VISIBLE);
                    tv_NoGService2.setVisibility(View.VISIBLE);
                    iv_NoGService.setVisibility(View.VISIBLE);
                    GraphicSearch.setVisibility(View.GONE);
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
            adapterServiceItem = new AdapterServiceItem(ServiceCategoryHair.this, filteredList);
            //set adapter
            recyclerViewServiceItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoGService.setVisibility(View.VISIBLE);
            tv_NoGService2.setVisibility(View.VISIBLE);
            iv_NoGService.setVisibility(View.VISIBLE);
            GraphicSearch.setVisibility(View.GONE);
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
        adapterServiceItem = new AdapterServiceItem(ServiceCategoryHair.this, filteredList);
        //set adapter
        recyclerViewServiceItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoGService.setVisibility(View.VISIBLE);
            tv_NoGService2.setVisibility(View.VISIBLE);
            iv_NoGService.setVisibility(View.VISIBLE);
            GraphicSearch.setVisibility(View.GONE);
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
        adapterServiceItem = new AdapterServiceItem(ServiceCategoryHair.this, filteredList);
        //set adapter
        recyclerViewServiceItemList.setAdapter(adapterServiceItem);
        if(filteredList.isEmpty()){
            tv_NoGService.setVisibility(View.VISIBLE);
            tv_NoGService2.setVisibility(View.VISIBLE);
            iv_NoGService.setVisibility(View.VISIBLE);
            GraphicSearch.setVisibility(View.GONE);
        }
    }

    private void sortListByPriceDescending(ArrayList<Model_Service> ServiceItemList) {

        Collections.sort(ServiceItemList, new ServiceCategoryHair.SortByPrice());
    }
    private void sortListByPriceAscending(ArrayList<Model_Service> ServiceItemList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(ServiceItemList, new ServiceCategoryHair.SortByPrice().reversed());
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

        Collections.sort(ServiceItemList, new ServiceCategoryHair.SortByPrice());

    }
    private void sortListByRecent(ArrayList<Model_Service> ServiceItemList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(ServiceItemList, new ServiceCategoryHair.SortByRecent().reversed());
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
