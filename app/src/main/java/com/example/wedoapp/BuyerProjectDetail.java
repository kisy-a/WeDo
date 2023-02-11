package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyerProjectDetail extends AppCompatActivity {

    private ImageButton backToPostingList;
    private TextView projectTitle, projectDesc, PDetail_Category, jobDate, jobTime,  jobPrice, jobDays, PDetail_status;
    private Button btn_projectEdit, btn_projectDelete;
    private String projectID;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_project_detail);

        projectTitle = findViewById(R.id.projectTitle);
        projectDesc = findViewById(R.id.projectDesc);
        PDetail_Category = findViewById(R.id.PDetail_Category);
        jobPrice = findViewById(R.id.jobPrice);
        jobDays = findViewById(R.id.jobDays);
        jobDate= findViewById(R.id.jobDate);
        jobTime = findViewById(R.id.jobTime);
        PDetail_status = findViewById(R.id.PDetail_status);

        backToPostingList = findViewById(R.id.backToPostingList);
        btn_projectEdit = findViewById(R.id.btn_projectEdit);
        btn_projectDelete = findViewById(R.id.btn_projectDelete);

        auth = FirebaseAuth.getInstance();

        projectID = getIntent().getStringExtra("ProjectID");

        //load product details
        loadProjectDetails();

        backToPostingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent back2postList = new Intent(BuyerProjectDetail.this, ManagePostingList.class);
                startActivity(back2postList);
            }
        });

        btn_projectEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2edProject = new Intent(BuyerProjectDetail.this, EditProject.class);
                intent2edProject.putExtra("ProjectID", projectID);
                startActivity(intent2edProject);
            }
        });


        btn_projectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProject(projectID);
                Intent intent2delProject = new Intent(BuyerProjectDetail.this, ManagePostingList.class);
                startActivity(intent2delProject);
                finish();
            }
        });
    }

    private void deleteProject(String projectID) {

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Project");
        dr.child(projectID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Deleted.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProjectDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.child(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PTitle = ""+snapshot.child("PTitle").getValue();
                String PDescription = ""+snapshot.child("PDescription").getValue();
                String PCategory = ""+snapshot.child("PCategory").getValue();
                String PBudget = ""+snapshot.child("PBudget").getValue();
                String PDeliveryDays = ""+snapshot.child("PDeliveryDays").getValue();
                String PDeliveryDate = ""+snapshot.child("PDeliveryDate").getValue();
                String PDeliveryTime = ""+snapshot.child("PDeliveryTime").getValue();
                String PStatus = ""+snapshot.child("PStatus").getValue();

                //set data to text view
                projectTitle.setText(PTitle);
                projectDesc.setText(PDescription);
                PDetail_Category.setText(PCategory);
                jobPrice.setText(PBudget);
                jobDays.setText(PDeliveryDays);
                jobDate.setText(PDeliveryDate);
                jobTime.setText(PDeliveryTime);
                PDetail_status.setText(PStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}