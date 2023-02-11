package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class BankInformation extends AppCompatActivity {

    private ImageButton ListBackToSellerAcc;
    private Button btn_addBankInfo, btn_manageBankAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_information);

        ListBackToSellerAcc = findViewById(R.id.ListBackToSellerAcc);
        btn_addBankInfo = findViewById(R.id.btn_addBankInfo);
        btn_manageBankAcc = findViewById(R.id.btn_manageBankAcc);

        ListBackToSellerAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BankMainBack2Acc= new Intent(BankInformation.this, SellerAccount.class);
                startActivity(BankMainBack2Acc);
            }
        });

        btn_addBankInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2addBank = new Intent(BankInformation.this, SellerPostServiceBankInfo.class);
                startActivity(intent2addBank);
            }
        });

        btn_manageBankAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2EditBankInfo = new Intent(BankInformation.this, EditServiceBankInfo.class);
                startActivity(intent2EditBankInfo);
            }
        });
    }
}