package com.example.wedoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpConditions extends AppCompatActivity {
    TextView textview1, textview2;
    ImageButton back;
    Button btn_continue;
    RadioGroup radioGroup;
    RadioButton condition_agree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_conditions);

        textview1 = findViewById(R.id.text_terms_and_conditions);
        textview1.setMovementMethod(new ScrollingMovementMethod());

        textview2 = findViewById(R.id.text_privacy_conditions);
        textview2.setMovementMethod(new ScrollingMovementMethod());
        condition_agree = findViewById(R.id.condition_agree);
        radioGroup = findViewById(R.id.categoryGroup);
        btn_continue = findViewById(R.id.btn_continue);

        back = findViewById(R.id.Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupHyperlink();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (condition_agree.isChecked()){
                    Intent intentToSignUp = new Intent (SignUpConditions.this , CustomerTypeActivity.class);
                    startActivity(intentToSignUp);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Read and agree to conditions to continue registration",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupHyperlink() {
        TextView linkTextView1 = findViewById(R.id.txt_hyperlink1);
        linkTextView1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView linkTextView2 = findViewById(R.id.txt_hyperlink2);
        linkTextView2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}