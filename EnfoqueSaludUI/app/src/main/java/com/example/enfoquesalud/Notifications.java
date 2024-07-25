package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Notification;
import com.example.enfoquesalud.Model.Register.NotificationUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Notifications extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String token;
    private int patientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
        patientId = intent.getIntExtra("CURRENT_USER_ID", -1);
        showNotificationPatient(patientId);
    }
    private void showNotificationPatient(int patientId){
        recyclerView = findViewById(R.id.rv_notification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<List<NotificationUser>> call = enfoqueSaludApi.getPatientNotification(authorization,patientId);
        call.enqueue(new Callback<List<NotificationUser>>() {
            @Override
            public void onResponse(Call<List<NotificationUser>> call, Response<List<NotificationUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationUser> notificationList = response.body();
                    List<Notification> formattedNotifications = formatNotifications(notificationList);
                    updateRecyclerView(formattedNotifications);
                } else {
                    Toast.makeText(Notifications.this, "Error in json response",
                                    Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationUser>> call, Throwable t) {
                Toast.makeText(Notifications.this, "Error in the API call", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private List<Notification> formatNotifications(List<NotificationUser> notificationUserList) {
        List<Notification> formattedNotifications = new ArrayList<>();
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        for (NotificationUser notificationUser : notificationUserList) {
            int appointmentId = notificationUser.getAppointment_id();
            String appointmentDateString = notificationUser.getAppointment().getDate();
            try {
                Date appointmentDate = apiDateFormat.parse(appointmentDateString);
                String formattedDate = displayDateFormat.format(appointmentDate);
                Notification formattedNotification = new Notification("Cita #" + appointmentId,
                        "Estado: Recibido", formattedDate);
                formattedNotifications.add(formattedNotification);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return formattedNotifications;
    }
    private void updateRecyclerView(List<Notification> notifications) {
        RvAdapterNotification adapter = new RvAdapterNotification(notifications);
        recyclerView.setAdapter(adapter);
    }
    public void goBack(View v){
        onBackPressed();
    }
}