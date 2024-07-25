package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecoverPasswordThree extends AppCompatActivity {
    private Button btnPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password_three);

        btnPasswordConfirm = findViewById(R.id.btnPasswordConfirm);

        btnPasswordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecoverPasswordThree.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void goBack(View v){
        onBackPressed();
    }
}