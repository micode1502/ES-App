package com.example.enfoquesalud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Read.Doctor;
import com.example.enfoquesalud.Model.Register.AppointmentRegister;
import com.example.enfoquesalud.Model.RequestUser;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicalAppointment extends AppCompatActivity {
    private Button btnTimeTable, btnPaymentAppointment;
    private MaterialButton viewDayHour;
    private int doctorId;
    private String token,dateAppoinment,hourAppoinment, name, lastname;
    private TextView txtPayment;
    private int patienteId,durationAppoinment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment);
        txtPayment = findViewById(R.id.txtPaymentMount);

        viewDayHour = findViewById(R.id.viewDay);
        btnTimeTable = findViewById(R.id.btnTimeTable);
        doctorId = getIntent().getIntExtra("DOCTOR_ID", -1);
        token = getIntent().getStringExtra("TOKEN");
        name = getIntent().getStringExtra("USER");
        lastname = getIntent().getStringExtra("LASTNAME");
        patienteId = getIntent().getIntExtra("PATIENTE_ID", patienteId);
        durationAppoinment = getIntent().getIntExtra("DURATION", durationAppoinment);
        dateAppoinment = getIntent().getStringExtra("DATE");
        hourAppoinment = getIntent().getStringExtra("HOUR");
        if (durationAppoinment != 0 && dateAppoinment != null && hourAppoinment != null) {
            viewDayHour.setVisibility(View.VISIBLE);
            viewDayHour.setText("Dia: " + dateAppoinment + " a las " + hourAppoinment + "AM");
            btnTimeTable.setText("Corregir Horario");
        }
        getDoctorAppoinment(doctorId);

        btnTimeTable=findViewById(R.id.btnTimeTable);
        btnPaymentAppointment = findViewById(R.id.btnPaymentAppointment);

        btnTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalAppointment.this, SelectDayAndHour.class);
                intent.putExtra("DOCTOR_ID", doctorId);
                intent.putExtra("TOKEN", token);
                intent.putExtra("PATIENTE_ID",patienteId);
                intent.putExtra("USER",name);
                intent.putExtra("LASTNAME",lastname);
                startActivity(intent);
            }
        });

        btnPaymentAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateAppoinment != null && hourAppoinment != null) {
                    showConfirmationDialog();
                } else {
                    Toast.makeText(MedicalAppointment.this, "Seleccione un horario primero",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView imgView = findViewById(R.id.imgNovelPaz);
        Glide.with(this)
                .load(R.drawable.experience)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView);

        ImageView imgView1 = findViewById(R.id.imgPulitzer);
        Glide.with(this)
                .load(R.drawable.mejor_medico)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView1);

        ImageView imgView2 = findViewById(R.id.imgQuimica);
        Glide.with(this)
                .load(R.drawable.top_awards)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView2);
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Está seguro de que desea realizar el registro de la cita?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savePatientAppoiment(patienteId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MedicalAppointment.this, "Registro de cita no realizado",
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
    private void savePatientAppoiment(int patienteId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        AppointmentRegister appointmentRegister = new AppointmentRegister();
        appointmentRegister.setDoctor_id(doctorId);
        appointmentRegister.setDate(dateAppoinment+" "+hourAppoinment);
        appointmentRegister.setDuration(durationAppoinment);
        appointmentRegister.setStatus(1);
        Call<RequestUser> call = enfoqueSaludApi.saveAppointment(authorization,appointmentRegister, patienteId);
        call.enqueue(new Callback<RequestUser>() {
            @Override
            public void onResponse(Call<RequestUser> call, Response<RequestUser> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MedicalAppointment.this, "Dia ocupado. Seleccione otro dia y hora. ",
                            Toast.LENGTH_SHORT).show();
                }
                RequestUser responseApi = response.body();
                int newAppointmentId = response.body().getAppointmentId();
                String paymentAmount = txtPayment.getText().toString();
                Toast.makeText(MedicalAppointment.this, "se guardo con exito",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedicalAppointment.this, Payment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("APPOINTMENT_ID", newAppointmentId);
                intent.putExtra("PATIENTD_ID", patienteId);
                intent.putExtra("TOKEN", token);
                intent.putExtra("PAYMENT_AMOUNT", paymentAmount);
                intent.putExtra("USER",name);
                intent.putExtra("LASTNAME",lastname);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RequestUser> call, Throwable t) {
                Toast.makeText(MedicalAppointment.this, "Error "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getDoctorAppoinment(int doctorId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<Doctor> call = enfoqueSaludApi.getDoctorDetailsId(authorization, doctorId);
        call.enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    Doctor doctor = response.body();
                    showFirstDoctor(doctor);
                } else {
                    Toast.makeText(MedicalAppointment.this, "Error in json response",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Toast.makeText(MedicalAppointment.this, "Error in the API call",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFirstDoctor(Doctor doctor) {
        TextView txtMedic = findViewById(R.id.txtMedic);
        TextView txtSpecialty = findViewById(R.id.txtSpecialti);
        Glide.with(MedicalAppointment.this).load(R.drawable.doctor).circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade());
        txtMedic.setText("Dr."+doctor.getUser().getPerson().getName() + " " +
                doctor.getUser().getPerson().getLastname());
        txtSpecialty.setText("Especialista en " + doctor.getSpecialty());
        txtPayment.setText(doctor.getSalaryHour().toString());
    }

    public void goBack(View v){
        onBackPressed();
    }
}