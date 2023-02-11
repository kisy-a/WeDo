package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmailVerification extends AppCompatActivity {

    private Button btn_verLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        btn_verLogin = findViewById(R.id.btn_verLogin);

        btn_verLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2Login = new Intent(EmailVerification.this, MainActivity.class);
                startActivity(intent2Login);
            }
        });
    }
}

