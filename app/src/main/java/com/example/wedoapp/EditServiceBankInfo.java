package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditServiceBankInfo extends AppCompatActivity {

    private ImageButton BankEditBackToBankInfo;
    private Button btn_editBankSave;
    private EditText edit_bankName, edit_beneficialName, edit_accountNumber;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String SBankName, SBeneficiaryName, SAccountNumber;
    private String bID;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_bank_info);

        BankEditBackToBankInfo = findViewById(R.id.BankEditBackToBankInfo);
        btn_editBankSave = findViewById(R.id.btn_editBankSave);

        edit_bankName = findViewById(R.id.edit_bankName);
        edit_beneficialName = findViewById(R.id.edit_beneficialName);
        edit_accountNumber = findViewById(R.id.edit_accountNumber);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        loadBank();

        BankEditBackToBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent Back2Edit1= new Intent(EditServiceBankInfo.this, BankInformation.class);
                //startActivity(Back2Edit1);
            }
        });

        btn_editBankSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBank();
            }
        });

    }

    private void loadBank() {

        //load bank info and set to view
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BankInformation");
        databaseReference.orderByChild("UID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String BankID = ""+snapshot.child("BID").getValue();
                    String SBankName = "" + ds.child("SBankName").getValue();
                    String SBeneficiaryName = "" + ds.child("SBeneficiaryName").getValue();
                    String SAccountNumber = "" + ds.child("SAccountNumber").getValue();

                    edit_bankName.setText(SBankName);
                    edit_beneficialName.setText(SBeneficiaryName);
                    edit_accountNumber.setText(SAccountNumber);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void insertBank() {

        SBankName = edit_bankName.getText().toString().trim();
        SBeneficiaryName = edit_beneficialName.getText().toString().trim();
        SAccountNumber = edit_accountNumber.getText().toString().trim();

        if(SBankName.isEmpty()){
            edit_bankName.setError("Bank Name is empty.");
            return;
        }
        if(SBeneficiaryName.isEmpty()){
            edit_beneficialName.setError("Beneficiary Name is empty.");
            return;
        }
        if(SAccountNumber.isEmpty()){
            edit_accountNumber.setError("Account Number is empty.");
            return;
        }

        //add data into database
        updateBank();
    }

    private void updateBank() {

        progressDialog.setMessage("Saving...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("SBankName", "" + SBankName);
        hashMap.put("SBeneficiaryName", "" + SBeneficiaryName);
        hashMap.put("SAccountNumber", "" + SAccountNumber);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("BankInformation");
        dr.child(auth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Updated.",Toast.LENGTH_SHORT).show();
                Intent intentToEditSave = new Intent (EditServiceBankInfo.this , SellerAccount.class);
                startActivity(intentToEditSave);
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