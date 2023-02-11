package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportUser extends AppCompatActivity {
    private Button btn_confirmReport;
    private EditText reportDescription;
    private ImageButton btn_ViewBackToSer;
    private RadioGroup categoryGroup;
    private RadioButton reportCategoryScam,reportCategoryOffensive,reportCategoryVulgar,reportCategoryTransaction,reportCategoryPrivacy;
    private String reportDesc, reportCat;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private String receiverID;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        btn_ViewBackToSer = findViewById(R.id.ViewBackToSer);
        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        categoryGroup = findViewById(R.id.categoryGroup);
        reportCategoryScam = findViewById(R.id.reportCategory1);
        reportCategoryOffensive = findViewById(R.id.reportCategory2);
        reportCategoryVulgar = findViewById(R.id.reportCategory3);
        reportCategoryTransaction = findViewById(R.id.reportCategory4);
        reportCategoryPrivacy = findViewById(R.id.reportCategory5);

        receiverID = getIntent().getStringExtra("ReceiverID");

        reportDescription = findViewById(R.id.reportDescription);
        btn_confirmReport = findViewById(R.id.btn_confirmReport);
        auth = FirebaseAuth.getInstance();

        btn_ViewBackToSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_confirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputReport();
            }
        });
    }
    private void inputReport(){
        reportDesc = reportDescription.getText().toString().trim();

        if (reportCategoryScam.isChecked()) {
            reportCat = "Scam";
        } else if (reportCategoryOffensive.isChecked()) {
            reportCat = "Offensive Messages";
        } else if (reportCategoryVulgar.isChecked()) {
            reportCat = "Vulgar Content";
        } else if (reportCategoryTransaction.isChecked()) {
            reportCat = "Transaction Outside WeDo";
        } else if (reportCategoryPrivacy.isChecked()) {
            reportCat = "Data Privacy Violation";
        }

        if(categoryGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(reportDesc.isEmpty()){
            reportDescription.setError("Description is required.");
            return;
        }

        createReport();
    }
    public void createReport(){

        progressDialog.setMessage("Reporting User...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        String status = "Pending";
        //add project data to database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ReportID", "" + timestamp);
        hashMap.put("ReportedID", "" + receiverID);
        hashMap.put("ReporterID", "" + auth.getUid());
        hashMap.put("ReportCategory", "" + reportCat);
        hashMap.put("ReportDescription", "" + reportDesc);
        hashMap.put("ReportStatus", "" + status);

        //save to database
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("ReportedUsers");
        dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(ReportUser.this,"User Reported",Toast.LENGTH_SHORT).show();
                clearData();
                onBackPressed();
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
        reportDesc = reportDescription.getText().toString().trim();

        reportDescription.setText("");
    }
}