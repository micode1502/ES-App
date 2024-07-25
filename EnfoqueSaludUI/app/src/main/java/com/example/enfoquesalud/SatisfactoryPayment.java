package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SatisfactoryPayment extends AppCompatActivity {
    private Button btnReturn;
    private String token, name,lastname;
    private int patientdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfactory_payment);

        Intent intent = getIntent();
        token = getIntent().getStringExtra("TOKEN");
        name = getIntent().getStringExtra("CURRENT_USER_NAME");
        lastname = getIntent().getStringExtra("CURRENT_USER_LASTNAME");
        patientdId = intent.getIntExtra("CURRENT_USER_ID", patientdId);
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SatisfactoryPayment.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("TOKEN",token);
                intent.putExtra("CURRENT_USER_ID",patientdId);
                intent.putExtra("CURRENT_USER_NAME",name);
                intent.putExtra("CURRENT_USER_LASTNAME",lastname);
                startActivity(intent);
            }
        });
    }
}