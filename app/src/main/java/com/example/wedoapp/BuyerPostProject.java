package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class BuyerPostProject extends AppCompatActivity {

    private ImageButton backToBuyerAcc;
    private RadioGroup categoryGroup, statusGroup;
    private RadioButton post_radioHair,post_radioVenue,post_radioDress,post_radioData,post_radioCar,post_radioCatering,post_radioFlower, post_radioOpen, post_radioClose, post_radioCompleted;
    private Button btn_projectPublish;
    private EditText post_title, post_desc, post_budget, post_days ;
    private String PTitle, PDescription, PBudget, PDeliveryDays, PDeliveryDate, PDeliveryTime, PCategory, PStatus, UName, UContact, UEmail, ULocation, UImage;
    Button btnDatePicker, btnTimePicker;
    EditText post_date, post_time;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_post_project);

        categoryGroup = findViewById(R.id.categoryGroup);
        statusGroup = findViewById(R.id.statusGroup);
        backToBuyerAcc = findViewById(R.id.backToBuyerAcc);
        btn_projectPublish = findViewById(R.id.btn_projectPublish);
        post_radioHair = findViewById(R.id.post_radioHair);
        post_radioVenue = findViewById(R.id.post_radioVenue);
        post_radioDress = findViewById(R.id.post_radioDress);
        post_radioData = findViewById(R.id.post_radioData);
        post_radioCar = findViewById(R.id.post_radioCar);
        post_radioCatering = findViewById(R.id.post_radioCatering);
        post_radioFlower= findViewById(R.id.post_radioFlower);
        post_radioOpen = findViewById(R.id.post_radioOpen);
        post_radioClose = findViewById(R.id.post_radioClose);
        post_radioCompleted = findViewById(R.id.post_radioCompleted);

        post_title = findViewById(R.id.post_title);
        post_desc = findViewById(R.id.post_desc);
        post_budget = findViewById(R.id.post_budget);
        post_days = findViewById(R.id.post_days);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        post_date=(EditText)findViewById(R.id.post_date);
        post_time=(EditText)findViewById(R.id.post_time);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BuyerPostProject.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                post_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getButton(datePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BuyerPostProject.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                post_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                timePickerDialog.getButton(timePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                timePickerDialog.getButton(timePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            }
        });
        btn_projectPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputProject();
            }
        });

        backToBuyerAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //insert data
    private void inputProject() {
        PTitle = post_title.getText().toString().trim();
        PDescription = post_desc.getText().toString().trim();
        PBudget = post_budget.getText().toString().trim();
        PDeliveryDays = post_days.getText().toString().trim();
        PDeliveryDate = post_date.getText().toString().trim();
        PDeliveryTime = post_time.getText().toString().trim();

        if (post_radioHair.isChecked()) {
            PCategory = "Hair and Makeup";
        } else if (post_radioVenue.isChecked()) {
            PCategory = "Venue";
        } else if (post_radioDress.isChecked()) {
            PCategory = "Dress Tailoring";
        } else if (post_radioData.isChecked()) {
            PCategory = "Audio/Video";
        } else if (post_radioCar.isChecked()) {
            PCategory = "Car Rental";
        } else if (post_radioCatering.isChecked()) {
            PCategory = "Catering Services";
        } else if (post_radioFlower.isChecked()) {
            PCategory = "Flowers";
        }

        if (post_radioOpen.isChecked()) {
            PStatus = "Open";
        } else if (post_radioClose.isChecked()) {
            PStatus = "Closed";
        } else if (post_radioCompleted.isChecked()) {
            PStatus = "Completed";
        }

        if(PTitle.isEmpty()){
            post_title.setError("Title is required.");
            return;
        }
        if(PDescription.isEmpty()){
            post_desc.setError("Description is required.");
            return;
        }
        if(PBudget.isEmpty()){
            post_budget.setError("Budget is required.");
            return;
        }
        if(PDeliveryDays.isEmpty()){
            post_days.setError("Delivery Hours is required.");
            return;
        }
        if(PDeliveryDate.isEmpty()){
            post_date.setError("Delivery Date is required.");
            return;
        }
        if(PDeliveryTime.isEmpty()){
            post_time.setError("Delivery Time is required.");
            return;
        }

        if(categoryGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(statusGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Status is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        createProject();
    }

    //create project
    private void createProject() {

        progressDialog.setMessage("Posting Project...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();

        //add project data to database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ProjectID", "" + timestamp);
        hashMap.put("PTitle", "" + PTitle);
        hashMap.put("PDescription", "" + PDescription);
        hashMap.put("PBudget", "" + PBudget);
        hashMap.put("PDeliveryDays", "" + PDeliveryDays);
        hashMap.put("PDeliveryDate", "" + PDeliveryDate);
        hashMap.put("PDeliveryTime", "" + PDeliveryTime);
        hashMap.put("PCategory", "" + PCategory);
        hashMap.put("PStatus", "" + PStatus);
        hashMap.put("UID", "" + auth.getUid());

        //save to database

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Project");
        dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(BuyerPostProject.this,"Project posted.",Toast.LENGTH_SHORT).show();
                clearData();

                Intent intentToBuyerAcc = new Intent (BuyerPostProject.this , BuyerAccount.class);
                startActivity(intentToBuyerAcc);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearData() {
        PTitle = post_title.getText().toString().trim();
        PDescription = post_desc.getText().toString().trim();
        PBudget = post_budget.getText().toString().trim();
        PDeliveryDays = post_days.getText().toString().trim();
        PDeliveryDate = post_date.getText().toString().trim();
        PDeliveryTime = post_time.getText().toString().trim();

        post_title.setText("");
        post_desc.setText("");
        post_budget.setText("");
        post_days.setText("");
        post_date.setText("");
        post_time.setText("");
    }

}