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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private ImageButton resetBackToLogin;
    private Button btn_resetPW;
    private EditText reset_passwordEmail;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetBackToLogin= findViewById(R.id.resetBackToLogin);
        reset_passwordEmail= findViewById(R.id.reset_passwordEmail);
        btn_resetPW= findViewById(R.id.btn_resetPW);

        auth = FirebaseAuth.getInstance();

        resetBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent resetBackToLogin = new Intent (ResetPassword.this , MainActivity.class);
                //startActivity(resetBackToLogin);
            }
        });

        btn_resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailAdd = reset_passwordEmail.getText().toString().trim();

                if(isEmailValid(emailAdd)){
                    auth.sendPasswordResetEmail(reset_passwordEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Confirmation Link has been sent.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                //Intent intent2Main = new Intent(ResetPassword.this, MainActivity.class);
                                //startActivity(intent2Main);
                                finish();
                            }
                            else{
                                Toast.makeText(ResetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(ResetPassword.this, "Please insert valid email.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}