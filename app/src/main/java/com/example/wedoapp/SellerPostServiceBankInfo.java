package com.example.wedoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerPostServiceBankInfo extends AppCompatActivity {

    private ImageButton addBackToBankInfo;
    private EditText seller_bankName, seller_beneficialName, seller_accountNumber;
    private Button btn_bankAdd;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String SBankName, SBeneficiaryName, SAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_post_service_bank_info);

        seller_bankName = findViewById(R.id.seller_bankName);
        seller_beneficialName = findViewById(R.id.seller_beneficialName);
        seller_accountNumber = findViewById(R.id.seller_accountNumber);

        addBackToBankInfo = findViewById(R.id.addBackToBankInfo);
        btn_bankAdd = findViewById(R.id.btn_bankAdd);

        auth = FirebaseAuth.getInstance();

        addBackToBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent addBKBackBankInfo= new Intent(SellerPostServiceBankInfo.this, BankInformation.class);
                //startActivity(addBKBackBankInfo);
            }
        });


        btn_bankAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBankInfo();
            }
        });
    }

    private void inputBankInfo() {

        SBankName = seller_bankName.getText().toString().trim();
        SBeneficiaryName = seller_beneficialName.getText().toString().trim();
        SAccountNumber = seller_accountNumber.getText().toString().trim();

        if(SBankName.isEmpty()){
            seller_bankName.setError("Bank Name is required.");
            return;
        }
        if(SBeneficiaryName.isEmpty()){
            seller_beneficialName.setError("Beneficiary Name is required.");
            return;
        }
        if(SAccountNumber.isEmpty()) {
            seller_accountNumber.setError("Account Number is required.");
            return;
        }
        addBankInfo();
    }


    private void addBankInfo() {
        final String timestamp = "" + System.currentTimeMillis();

            //setup bank data to database
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("BID", "" + timestamp);
            hashMap.put("SBankName", "" + SBankName);
            hashMap.put("SBeneficiaryName", "" + SBeneficiaryName);
            hashMap.put("SAccountNumber", "" + SAccountNumber);
            hashMap.put("UID", "" + auth.getUid());

         //save to database

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("BankInformation");
        dr.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getApplicationContext(),"Bank Info added successfully.",Toast.LENGTH_SHORT).show();
                clearData();
                //btn_bankAdd.setVisibility(View.INVISIBLE);
                Intent intentToBankAdded = new Intent (SellerPostServiceBankInfo.this , SellerAccount.class);
                startActivity(intentToBankAdded);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
            }
        });
        }

    private void clearData() {
        SBankName = seller_bankName.getText().toString().trim();
        SBeneficiaryName = seller_beneficialName.getText().toString().trim();
        SAccountNumber = seller_accountNumber.getText().toString().trim();

        seller_bankName.setText("");
        seller_beneficialName.setText("");
        seller_accountNumber.setText("");

        btn_bankAdd.setVisibility(View.INVISIBLE);
    }

}

