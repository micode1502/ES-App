package com.example.enfoquesalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Locale;

public class SelectDayAndHour extends AppCompatActivity {
    private Button btnSelect;
    private String selectedDate,token, name, lastname;
    private int durationAppointment,doctorId,patienteId;
    private TextInputEditText editTextTime;
    private AutoCompleteTextView autoCompleteGender;
    private long minDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day_and_hour);
        doctorId = getIntent().getIntExtra("DOCTOR_ID", -1);
        token = getIntent().getStringExtra("TOKEN");
        name = getIntent().getStringExtra("USER");
        lastname = getIntent().getStringExtra("LASTNAME");

        if (token == null || doctorId == -1) {
            Toast.makeText(this, "Error de servidor. Por favor, inicie sesión nuevamente.",
                    Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        patienteId = getIntent().getIntExtra("PATIENTE_ID",patienteId);
        CalendarView calendarView = findViewById(R.id.calendarView);
        autoCompleteGender = findViewById(R.id.txtTypeDocument);
        btnSelect = findViewById(R.id.btnSelect);
        // Duration select
        String[] genderOptions = {"1h", "2h"};
        ArrayAdapter<String> adapterG = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, genderOptions);
        autoCompleteGender.setAdapter(adapterG);
        //CalendarView
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        minDate = calendar.getTimeInMillis();
        calendarView.setMinDate(minDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                if (selectedCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Toast.makeText(SelectDayAndHour.this, "No se permiten citas los domingos",
                            Toast.LENGTH_SHORT).show();
                    view.setDate(minDate);
                } else if (selectedCalendar.before(calendar)) {
                    Toast.makeText(SelectDayAndHour.this, "No se permiten citas en días anteriores",
                            Toast.LENGTH_SHORT).show();
                    view.setDate(minDate);
                } else {
                    selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                }
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate == null) {
                    Toast.makeText(SelectDayAndHour.this, "Seleccione una fecha primero",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextTime == null || editTextTime.getText().toString().isEmpty()) {
                    Toast.makeText(SelectDayAndHour.this, "Seleccione una hora primero",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String selectedDuration = autoCompleteGender.getText().toString();
                if (selectedDuration.isEmpty()) {
                    Toast.makeText(SelectDayAndHour.this, "Seleccione una duración primero",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] genderOptions = {"1h","2h"};
                int[] durationOptions = {60, 120};
                for (int i = 0; i < genderOptions.length; i++) {
                    if (selectedDuration.equals(genderOptions[i])) {
                        durationAppointment = durationOptions[i];
                        break;
                    }
                }
                Intent intent = new Intent(SelectDayAndHour.this, MedicalAppointment.class);
                intent.putExtra("DATE",selectedDate);
                intent.putExtra("DURATION",durationAppointment);
                intent.putExtra("HOUR",editTextTime.getText().toString());
                intent.putExtra("DOCTOR_ID", doctorId);
                intent.putExtra("TOKEN", token);
                intent.putExtra("PATIENTE_ID",patienteId);
                intent.putExtra("USER",name);
                intent.putExtra("LASTNAME",lastname);
                startActivity(intent);
                finish();
            }
        });
        TextInputEditText inputTime = findViewById(R.id.inputTime);
        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
                builder.setHour(12)
                        .setMinute(0)
                        .setTimeFormat(TimeFormat.CLOCK_12H);
                MaterialTimePicker materialTimePicker = builder.build();
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = materialTimePicker.getHour();
                        int minute = materialTimePicker.getMinute();
                        if (hour >= 8 && hour < 12) {
                            int hour12Format = (hour % 12 == 0) ? 12 : hour % 12;
                            String time = String.format(Locale.getDefault(),
                                                "%02d:%02d", hour12Format, minute);
                            editTextTime = findViewById(R.id.inputTime);
                            editTextTime.setText(time);
                        } else {
                            Toast.makeText(getApplicationContext(), "Seleccione una hora entre " +
                                            "8 AM y 12 PM", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");
            }
        });
    }
    public void goBack(View v){
        onBackPressed();
    }
}