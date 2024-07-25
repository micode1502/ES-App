package com.example.enfoquesalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.CalendarView;

import android.widget.Toast;

import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Read.Appointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PendingAppointment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int patientId;
    private String token, name, lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_appointments);

        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
        patientId = intent.getIntExtra("CURRENT_USER_ID", patientId);
        name = intent.getStringExtra("CURRENT_USER_NAME");
        lastname = intent.getStringExtra("CURRENT_USER_LASTNAME");
        fetchPendingAppointments(patientId);

        // Recycler View
        recyclerView = findViewById(R.id.rvAppointmentTime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Set menu icon selected
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.action_schedule);
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    Intent intentSearch = new Intent(PendingAppointment.this, Home.class);
                    intentSearch.putExtra("TOKEN",token);
                    intentSearch.putExtra("CURRENT_USER_ID", patientId);
                    intentSearch.putExtra("CURRENT_USER_NAME", name);
                    intentSearch.putExtra("CURRENT_USER_LASTNAME", lastname);
                    startActivity(intentSearch);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else if (item.getItemId() == R.id.action_schedule) {
                    return true;
                } else if (item.getItemId() == R.id.action_settings) {
                    Intent intentProfile = new Intent(PendingAppointment.this, Profile.class);
                    intentProfile.putExtra("TOKEN",token);
                    intentProfile.putExtra("CURRENT_USER_ID", patientId);
                    intentProfile.putExtra("CURRENT_USER_NAME", name);
                    intentProfile.putExtra("CURRENT_USER_LASTNAME", lastname);
                    startActivity(intentProfile);
                    // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                return false;
            }
        });
    }
    // Date appointments view
    private void fetchPendingAppointments(int patientId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<List<Appointment>> call = enfoqueSaludApi.getPatientAppointments(authorization,patientId);
        call.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Appointment> appointments = response.body();
                    List<String> appointmentDates = new ArrayList<>();
                    for (Appointment appointment : appointments) {
                        appointmentDates.add(appointment.getDate());
                    }
                    markDatesOnCalendar(appointmentDates);
                    RvAdapterAvailableHours adapter = new RvAdapterAvailableHours(appointmentDates);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(PendingAppointment.this, "Error in json response",
                                                                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(PendingAppointment.this, "Error in the API call",
                                                                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void markDatesOnCalendar(List<String> dates) {
        CalendarView calendarView = findViewById(R.id.calendarView);
        Set<Long> selectedDates = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        for (String date : dates) {
            try {
                Date parsedDate = sdf.parse(date);
                calendar.setTime(parsedDate);

                long selectedDateInMillis = calendar.getTimeInMillis();
                selectedDates.add(selectedDateInMillis);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (long selectedDate : selectedDates) {
            calendarView.setDate(selectedDate, true, false);
        }
    }

    public void goBack(View v){
        onBackPressed();
    }
}