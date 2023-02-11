package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class EditProject extends AppCompatActivity {

    private ImageButton backToProjectDetails;
    private RadioButton edit_radioHair,edit_radioVenue,edit_radioDress,edit_radioData,edit_radioCar,edit_radioCatering,edit_radioFlower,edit_radioOpen,edit_radioClose,edit_radioCompleted;
    private EditText edit_postTitle,edit_postDesc,edit_postBudget,edit_postDays,edit_post_date,edit_post_time;
    private Button btn_projectSave, btnDatePicker, btnTimePicker;
    private RadioGroup edit_rgCategory, edit_rgStatus;

    private String projectID;
    private String graphic,writing,tech,data,video,other,open,close,complete;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String PTitle, PDescription, PDeliveryDays, PDeliveryDate, PDeliveryTime, PStatus, PCategory, PBudget;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        edit_post_date = findViewById(R.id.edit_post_date);
        edit_post_time = findViewById(R.id.edit_post_time);
        backToProjectDetails = findViewById(R.id.backToProjectDetails);
        btn_projectSave = findViewById(R.id.btn_projectSave);

        edit_radioHair = findViewById(R.id.edit_radioHair);
        edit_radioVenue = findViewById(R.id.edit_radioVenue);
        edit_radioDress = findViewById(R.id.edit_radioDress);
        edit_radioData = findViewById(R.id.edit_radioData);
        edit_radioCar = findViewById(R.id.edit_radioCar);
        edit_radioCatering = findViewById(R.id.edit_radioCatering);
        edit_radioFlower= findViewById(R.id.edit_radioFlower);
        edit_radioOpen = findViewById(R.id.edit_radioOpen);
        edit_radioClose = findViewById(R.id.edit_radioClose);
        edit_radioCompleted = findViewById(R.id.edit_radioCompleted);

        edit_rgCategory = findViewById(R.id.edit_rgCategory);
        edit_rgStatus = findViewById(R.id.edit_rgStatus);

        edit_postTitle = findViewById(R.id.edit_postTitle);
        edit_postDesc = findViewById(R.id.edit_postDesc);
        edit_postBudget = findViewById(R.id.edit_postBudget);
        edit_postDays = findViewById(R.id.edit_postDays);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        projectID = getIntent().getStringExtra("ProjectID");

        backToProjectDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent back2proDetail = new Intent(EditProject.this, ManagePostingList.class);
                //startActivity(back2proDetail);
            }
        });

        btn_projectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProject();

            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProject.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edit_post_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditProject.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edit_post_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        loadProjectDetails();

    }

    private void loadProjectDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.child(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ProjectID = ""+snapshot.child("ProjectID").getValue();
                String PTitle = ""+snapshot.child("PTitle").getValue();
                String PDescription = ""+snapshot.child("PDescription").getValue();
                String PCategory = ""+snapshot.child("PCategory").getValue();
                String PBudget = ""+snapshot.child("PBudget").getValue();
                String PDeliveryDays = ""+snapshot.child("PDeliveryDays").getValue();
                String PDeliveryDate = ""+snapshot.child("PDeliveryDate").getValue();
                String PDeliveryTime = ""+snapshot.child("PDeliveryTime").getValue();
                String PStatus = ""+snapshot.child("PStatus").getValue();

                //set data to text view
                edit_postTitle.setText(PTitle);
                edit_postDesc.setText(PDescription);
                edit_rgCategory.getCheckedRadioButtonId();
                edit_postBudget.setText(PBudget);
                edit_postDays.setText(PDeliveryDays);
                edit_post_date.setText(PDeliveryDate);
                edit_post_time.setText(PDeliveryTime);
                edit_rgStatus.getCheckedRadioButtonId();

                if (PCategory.equals("Hair and Makeup")) {
                    edit_radioHair.setChecked(true);
                } else {
                    edit_radioHair.setChecked(false);
                }
                if (PCategory.equals("Venue")) {
                    edit_radioVenue.setChecked(true);
                } else {
                    edit_radioVenue.setChecked(false);
                }
                if (PCategory.equals("Dress Tailoring")) {
                    edit_radioDress.setChecked(true);
                } else {
                    edit_radioDress.setChecked(false);
                }
                if (PCategory.equals("Audio and Video")) {
                    edit_radioData.setChecked(true);
                } else {
                    edit_radioData.setChecked(false);
                }
                if (PCategory.equals("Car Services")) {
                    edit_radioCar.setChecked(true);
                } else {
                    edit_radioCar.setChecked(false);
                }
                if (PCategory.equals("Catering Services")) {
                    edit_radioCatering.setChecked(true);
                } else {
                    edit_radioCatering.setChecked(false);
                }
                if (PCategory.equals("Flowers")) {
                    edit_radioFlower.setChecked(true);
                } else {
                    edit_radioFlower.setChecked(false);
                }


                if (PStatus.equals("Open")) {
                    edit_radioOpen.setChecked(true);
                } else {
                    edit_radioOpen.setChecked(false);
                }
                if (PStatus.equals("Closed")) {
                    edit_radioClose.setChecked(true);
                } else {
                    edit_radioClose.setChecked(false);
                }
                if (PStatus.equals("Completed")) {
                    edit_radioCompleted.setChecked(true);
                } else {
                    edit_radioCompleted.setChecked(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void insertProject() {
        PTitle = edit_postTitle.getText().toString().trim();
        PDescription = edit_postDesc.getText().toString().trim();
        PBudget = edit_postBudget.getText().toString().trim();
        PDeliveryDays = edit_postDays.getText().toString().trim();
        PDeliveryDate = edit_post_date.getText().toString().trim();
        PDeliveryTime = edit_post_time.getText().toString().trim();

        if (edit_radioHair.isChecked()) {
            PCategory = "Hair and Makeup";
        } else if (edit_radioVenue.isChecked()) {
            PCategory = "Venue";
        } else if (edit_radioDress.isChecked()) {
            PCategory = "Dress Tailoring";
        } else if (edit_radioData.isChecked()) {
            PCategory = "Audio/Video";
        } else if (edit_radioCar.isChecked()) {
            PCategory = "Car Rental";
        } else if (edit_radioCatering.isChecked()) {
            PCategory = "Catering Services";
        } else if (edit_radioFlower.isChecked()) {
            PCategory = "Flowers";
        }

        if (edit_radioOpen.isChecked()) {
            PStatus = "Open";
        } else if (edit_radioClose.isChecked()) {
            PStatus = "Closed";
        } else if (edit_radioCompleted.isChecked()) {
            PStatus = "Completed";
        }


        if(PTitle.isEmpty()){
            edit_postTitle.setError("Title is required.");
            return;
        }
        if(PDescription.isEmpty()){
            edit_postDesc.setError("Description is required.");
            return;
        }
        if(PBudget.isEmpty()){
            edit_postBudget.setError("Budget is required.");
            return;
        }
        if(PDeliveryDays.isEmpty()){
            edit_postDays.setError("Delivery Hours is required.");
            return;
        }
        if(PDeliveryDate.isEmpty()){
            edit_post_date.setError("Delivery Date is required.");
            return;
        }
        if(PDeliveryTime.isEmpty()){
            edit_post_time.setError("Delivery Time is required.");
            return;
        }

        if(edit_rgCategory.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(edit_rgStatus.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Status is required.",Toast.LENGTH_SHORT).show();
            return;
        }
        //add data into database
        updateProject();
    }

    private void updateProject() {

        progressDialog.setMessage("Saving...");
        progressDialog.show();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("PTitle", "" + PTitle);
            hashMap.put("PDescription", "" + PDescription);
            hashMap.put("PBudget", "" + PBudget);
            hashMap.put("PDeliveryDays", "" + PDeliveryDays);
            hashMap.put("PDeliveryDate", "" + PDeliveryDate);
            hashMap.put("PDeliveryTime", "" + PDeliveryTime);
            hashMap.put("PCategory", "" + PCategory);
            hashMap.put("PStatus", "" + PStatus);

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Project");
            dr.child(projectID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Intent intent2ordPendingLs = new Intent(EditProject.this, ManagePostingList.class);
                    startActivity(intent2ordPendingLs);
                    Toast.makeText(getApplicationContext(),"Updated.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
    }
}
