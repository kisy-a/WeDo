package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FilterServiceCategory extends AppCompatActivity {
    private RadioGroup sortGroup, ratingGroup;
    private RadioButton sort_high_to_low, sort_low_to_high, sort_recent, sort_nearby,
            filterRadioStar1, filterRadioStar2, filterRadioStar3, filterRadioStar4,filterRadioStar5;
    private Button btn_purchaseConfirm, btn_chooseFile;
    private String sortBy, ratingBy, minPrice, maxPrice;
    private EditText filterMinimumPrice, filterMaximumPrice;
    private TextView txtratingGroup;
    private AdapterServiceItem adapterServiceItem;
    private String s5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_service_category);

        ImageButton back = findViewById(R.id.back);

        sortGroup = findViewById(R.id.sortGroup);
        sort_high_to_low = findViewById(R.id.sort_high_to_low);
        sort_low_to_high = findViewById(R.id.sort_low_to_high);
        sort_recent = findViewById(R.id.sort_recent);

        filterMinimumPrice = findViewById(R.id.filterMinimumPrice);
        filterMaximumPrice = findViewById(R.id.filterMaximumPrice);

        ratingGroup = findViewById(R.id.ratingGroup);
        filterRadioStar1 = findViewById(R.id.filterRadioStar1);
        filterRadioStar2 = findViewById(R.id.filterRadioStar2);
        filterRadioStar3 = findViewById(R.id.filterRadioStar3);
        filterRadioStar4 = findViewById(R.id.filterRadioStar4);
        filterRadioStar5 = findViewById(R.id.filterRadioStar5);

        Button btn_clearFilter = findViewById(R.id.btn_clearFilter);
        Button btn_applyFilter = findViewById(R.id.btn_applyFilter);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("sortby", "");
        String s2 = sh.getString("minprice", "");
        String s3 = sh.getString("maxprice", "");
        String s4 = sh.getString("ratingby", "");
        s5 = sh.getString("ServiceCat", "");
        switch (s1){
            case "recent":
                sortGroup.check(sort_recent.getId());
                break;
            case "high2low":
                sortGroup.check(sort_high_to_low.getId());
                break;
            case "low2high":
                sortGroup.check(sort_low_to_high.getId());
                break;
        }
        filterMinimumPrice.setText(String.valueOf(s2));
        filterMaximumPrice.setText(String.valueOf(s3));
        switch (s4){
            case "1":
                ratingGroup.check(filterRadioStar1.getId());
                break;
            case "2":
                ratingGroup.check(filterRadioStar2.getId());
                break;
            case "3":
                ratingGroup.check(filterRadioStar3.getId());
                break;
            case "4":
                ratingGroup.check(filterRadioStar4.getId());
                break;
            case "5":
                ratingGroup.check(filterRadioStar5.getId());
                break;
        }
        //back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });

        //button for clear filter
        btn_clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        //button for apply filter
        btn_applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFilter();
            }
        });
    }
    //functions
    public void checkFilter(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        //radio button for sorting
        if (sort_high_to_low.isChecked()) {
            sortBy ="high2low";
        } else if (sort_low_to_high.isChecked()) {
            sortBy ="low2high";
        } else if (sort_recent.isChecked()) {
            sortBy ="recent";
        }

        //text input for minimum and maximum price
        minPrice = filterMinimumPrice.getText().toString().trim();
        maxPrice = filterMaximumPrice.getText().toString().trim();

        //radio button for ratings
        if (filterRadioStar1.isChecked()) {
            ratingBy = "1";
        } else if (filterRadioStar2.isChecked()) {
            ratingBy = "2";
        } else if (filterRadioStar3.isChecked()) {
            ratingBy = "3";
        } else if (filterRadioStar4.isChecked()) {
            ratingBy = "4";
        } else if (filterRadioStar5.isChecked()) {
            ratingBy = "5";
        }

        myEdit.putString("sortby", sortBy);
        myEdit.putString("minprice", minPrice);
        myEdit.putString("maxprice", maxPrice);
        myEdit.putString("ratingby", ratingBy);
        myEdit.apply();

        applyFilter();

    }
    //apply filter
    public void applyFilter(){
        switch (s5){
            case "ServiceCategoryAudioVideo":
                Intent intentToData1 = new Intent (FilterServiceCategory.this , ServiceCategoryAudioVideo.class);
                startActivity(intentToData1);
                finish();
                break;
            case "ServiceCategoryCar":
                Intent intentToData2 = new Intent (FilterServiceCategory.this , ServiceCategoryCar.class);
                startActivity(intentToData2);
                finish();
                break;
            case "ServiceCategoryCatering":
                Intent intentToData3 = new Intent (FilterServiceCategory.this , ServiceCategoryCatering.class);
                startActivity(intentToData3);
                finish();
                break;
            case "ServiceCategoryDress":
                Intent intentToData4 = new Intent (FilterServiceCategory.this , ServiceCategoryDress.class);
                startActivity(intentToData4);
                finish();
                break;
            case "ServiceCategoryFlower":
                Intent intentToData5 = new Intent (FilterServiceCategory.this , ServiceCategoryFlower.class);
                startActivity(intentToData5);
                finish();
                break;
            case "ServiceCategoryHair":
                Intent intentToData6 = new Intent (FilterServiceCategory.this , ServiceCategoryHair.class);
                startActivity(intentToData6);
                finish();
                break;
            case "ServiceCategoryVenue":
                Intent intentToData7 = new Intent (FilterServiceCategory.this , ServiceCategoryVenue.class);
                startActivity(intentToData7);
                finish();
                break;
        }
    }

    //clear data
    public void clearData(){
        sortGroup.clearCheck();
        ratingGroup.clearCheck();

        sortBy ="";
        ratingBy="";
        //minPrice = filterMinimumPrice.getText().toString().trim();
        //maxPrice = filterMaximumPrice.getText().toString().trim();

        filterMinimumPrice.setText("");
        filterMaximumPrice.setText("");

        SharedPreferences preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        preferences.edit().remove("sortby").apply();
        preferences.edit().remove("minprice").apply();
        preferences.edit().remove("maxprice").apply();
        preferences.edit().remove("ratingby").apply();
    }
}