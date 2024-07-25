package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Read.Doctor;
import com.example.enfoquesalud.Model.Read.Medical;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicList extends AppCompatActivity {
    private RecyclerView recyclerViewM;
    private TextView txtArrow;
    private int patientId;
    private String token, name, lastname, specialty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_list);

        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
        patientId = intent.getIntExtra("PATIENTE_ID",patientId);
        name = intent.getStringExtra("CURRENT_USER_NAME");
        lastname = intent.getStringExtra("CURRENT_USER_LASTNAME");
        if (token == null || patientId == -1) {
            Toast.makeText(this, "Error de servidor. Por favor, inicie sesión nuevamente.",
                    Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        specialty = intent.getStringExtra("SPECIALTY");
        showOptionsView(specialty);
        searchDoctor();
    }
    // Search Doctor
    private void searchDoctor(){
        TextInputEditText txtSearch = findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDoctorResult(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void searchDoctorResult(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        if (query.isEmpty()) {
            showOptionsView(specialty);
            return;
        }
        Call<List<Doctor>> call = enfoqueSaludApi.searchDoctor(authorization, query);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Doctor> doctorsList = response.body();
                    updateCardResults(doctorsList);
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(MedicList.this, "Error in the API call",
                                                                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateCardResults(List<Doctor> doctorsList) {
        List<Medical> medicalList = new ArrayList<>();
        for (Doctor doctor : doctorsList) {
            Medical medical = new Medical(doctor.getUser().getId(),
                    doctor.getUser().getPerson().getName() + " " +
                            doctor.getUser().getPerson().getLastname(),
                    "Especialista en " + doctor.getSpecialty(),
                    "", doctor.getRating());
            medicalList.add(medical);
        }
        RvAdapterMedical adapter = new RvAdapterMedical(medicalList);
        recyclerViewM.setAdapter(adapter);
        adapter.setOnItemClickListener(new RvAdapterMedical.OnItemClickListener() {
            @Override
            public void onItemClick(int doctorId) {
                detailsDoctorAppoinment(doctorId);
            }
        });
    }
    // Doctor items - Recycler View
    private void getDoctors(){
        recyclerViewM = findViewById(R.id.rv_medical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewM.setLayoutManager(linearLayoutManager);
        recyclerViewM.setHasFixedSize(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<List<Doctor>> call = enfoqueSaludApi.getListDoctor(authorization);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Doctor> doctorsList = response.body();
                    List<Medical> medicalList = new ArrayList<>();
                    for (Doctor doctor : doctorsList) {
                        Medical medical = new Medical(doctor.getUser().getId(),
                                doctor.getUser().getPerson().getName() + " " +
                                        doctor.getUser().getPerson().getLastname(),
                                "Especialista en " + doctor.getSpecialty()
                                        ,"",doctor.getRating());
                        medicalList.add(medical);
                    }
                    RvAdapterMedical adapter = new RvAdapterMedical(medicalList);
                    recyclerViewM.setAdapter(adapter);
                    adapter.setOnItemClickListener(new RvAdapterMedical.OnItemClickListener() {
                        @Override
                        public void onItemClick(int doctorId) {
                            detailsDoctorAppoinment(doctorId);
                        }
                    });
                } else {
                    Toast.makeText(MedicList.this, "Error in json response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(MedicList.this, "Error in the API call", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Doctor for Speciality - Recycler View
    private void getDoctorsSpecialty(String specialty){
        recyclerViewM = findViewById(R.id.rv_medical);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewM.setLayoutManager(linearLayoutManager);
        recyclerViewM.setHasFixedSize(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<List<Doctor>> call = enfoqueSaludApi.getListDoctorSpecialty(authorization, specialty);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Doctor> doctorsList = response.body();
                    List<Medical> medicalList = new ArrayList<>();
                    for (Doctor doctor : doctorsList) {
                        Medical medical = new Medical(doctor.getUser().getId(),
                                doctor.getUser().getPerson().getName() + " " +
                                        doctor.getUser().getPerson().getLastname(),
                                "Especialista en " + doctor.getSpecialty()
                                        ,"",doctor.getRating());
                        medicalList.add(medical);
                    }
                    RvAdapterMedical adapter = new RvAdapterMedical(medicalList);
                    recyclerViewM.setAdapter(adapter);
                    adapter.setOnItemClickListener(new RvAdapterMedical.OnItemClickListener() {
                        @Override
                        public void onItemClick(int doctorId) {
                            detailsDoctorAppoinment(doctorId);
                        }
                    });
                } else {
                    Toast.makeText(MedicList.this, "Error in json response",
                                                                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(MedicList.this, "Error in the API call",
                                                                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Views according to the option chosen at home
    private void showOptionsView(String specialty){
        txtArrow = findViewById(R.id.txtArrow);
        if (specialty != null && !specialty.isEmpty()) {
            String title = "Médicos de " + specialty;
            txtArrow.setText(title);
            getDoctorsSpecialty(specialty);
        } else {
            getDoctors();
        }
    }
    private void detailsDoctorAppoinment(int id) {
        Intent intent = new Intent(MedicList.this, MedicalAppointment.class);
        intent.putExtra("DOCTOR_ID", id);
        intent.putExtra("PATIENTE_ID",patientId);
        intent.putExtra("TOKEN", token);
        intent.putExtra("USER",name);
        intent.putExtra("LASTNAME",lastname);
        startActivity(intent);
    }

    public void goBack(View v){
        onBackPressed();
    }
}