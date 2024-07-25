package com.example.enfoquesalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Profile extends AppCompatActivity {
    private String token, name, lastname;
    private int patientId;
    private TextView txtNameProfile, txtLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNameProfile = findViewById(R.id.txtNameProfile);
        txtLogOut = findViewById(R.id.txtLogOut);
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
        patientId = intent.getIntExtra("CURRENT_USER_ID", -1);
        name = intent.getStringExtra("CURRENT_USER_NAME");
        lastname = intent.getStringExtra("CURRENT_USER_LASTNAME");
        txtNameProfile.setText(name + " " + lastname);

        ImageView imgView = findViewById(R.id.imgPerfil);
        Glide.with(this)
                .load(R.drawable.profile_default)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView);

        // Set menu icon selected
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.action_settings);
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    Intent intentSearch = new Intent(Profile.this, Home.class);
                    intentSearch.putExtra("TOKEN",token);
                    intentSearch.putExtra("CURRENT_USER_ID", patientId);
                    intentSearch.putExtra("CURRENT_USER_NAME", name);
                    intentSearch.putExtra("CURRENT_USER_LASTNAME", lastname);
                    startActivity(intentSearch);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else if (item.getItemId() == R.id.action_schedule) {
                    Intent intentProfile = new Intent(Profile.this, PendingAppointment.class);
                    intentProfile.putExtra("TOKEN",token);
                    intentProfile.putExtra("CURRENT_USER_ID", patientId);
                    startActivity(intentProfile);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else if (item.getItemId() == R.id.action_settings) {
                    return true;
                }
                return false;
            }
        });

        //Logout
        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    private void logout() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void goBack(View v){
        onBackPressed();
    }
}