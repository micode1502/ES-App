package com.example.enfoquesalud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Read.Doctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String token, name, lastname;
    private int patientId;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
        patientId = intent.getIntExtra("CURRENT_USER_ID", -1);
        name = intent.getStringExtra("CURRENT_USER_NAME");
        lastname = intent.getStringExtra("CURRENT_USER_LASTNAME");
        if (token == null || patientId == -1 || name == null || lastname == null) {
            Toast.makeText(this, "Error de servidor. Por favor, inicie sesión nuevamente.",
                            Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        showDataUser(name,lastname);
        // Notifications
        ImageView imgNotification = findViewById(R.id.imgNotification);
        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Notifications.class);
                intent.putExtra("TOKEN",token);
                intent.putExtra("CURRENT_USER_ID",patientId);
                startActivity(intent);
            }
        });
        // Imagen del doctor
        imgView = findViewById(R.id.imgUser);
        Glide.with(this)
                .load(R.drawable.profile_default)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView);
        // Slider
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_1,ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.slide_2,ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.slide_3,ScaleTypes.CENTER_INSIDE));
        imageSlider.setImageList(slideModels);
        //Specialties - Recycler view
        listSpecialties();
        //First Doctor - Card view
        getDoctorShowFirst();
        // See more Medical
        MaterialTextView tvSeeMoreM = findViewById(R.id.tvSeeMoreM);
        tvSeeMoreM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MedicList.class);
                intent.putExtra("TOKEN",token);
                intent.putExtra("PATIENTE_ID",patientId);
                intent.putExtra("CURRENT_USER_NAME",name);
                intent.putExtra("CURRENT_USER_LASTNAME",lastname);
                startActivity(intent);
            }
        });
        // Navidation
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.action_home);
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_home) {
                    return true;
                } else if (item.getItemId() == R.id.action_schedule) {
                    Intent intentSearch = new Intent(Home.this, PendingAppointment.class);
                    intentSearch.putExtra("TOKEN",token);
                    intentSearch.putExtra("CURRENT_USER_ID", patientId);
                    intentSearch.putExtra("CURRENT_USER_NAME", name);
                    intentSearch.putExtra("CURRENT_USER_LASTNAME", lastname);
                    startActivity(intentSearch);
                } else if (item.getItemId() == R.id.action_settings) {
                    Intent intentProfile = new Intent(Home.this, Profile.class);
                    intentProfile.putExtra("TOKEN",token);
                    intentProfile.putExtra("CURRENT_USER_ID", patientId);
                    intentProfile.putExtra("CURRENT_USER_NAME", name);
                    intentProfile.putExtra("CURRENT_USER_LASTNAME", lastname);
                    startActivity(intentProfile);
                }
                return false;
            }
        });
    }
    // Patient data - home
    private void showDataUser(String name, String lastname){
        TextView txtNameComplete = findViewById(R.id.txtUser);
        txtNameComplete.setText(name + " " + lastname);
    }
    // Specialty items - Recycler View
    private void listSpecialties() {
        recyclerView = findViewById(R.id.rv_specialty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        Call<List<String>> call = enfoqueSaludApi.getListSpecialties(authorization);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> specialtyList = response.body();
                    RvAdapterHome adapter = new RvAdapterHome(specialtyList);
                    adapter.setOnItemClickListener(new RvAdapterHome.OnItemClickListener() {
                        @Override
                        public void onItemClick(String specialty) {
                            Intent intent = new Intent(Home.this, MedicList.class);
                            intent.putExtra("TOKEN", token);
                            intent.putExtra("PATIENTE_ID",patientId);
                            intent.putExtra("SPECIALTY", specialty);
                            intent.putExtra("CURRENT_USER_NAME", name);
                            intent.putExtra("CURRENT_USER_LASTNAME", lastname);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Home.this, "Error in json response", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(Home.this, "Error in the API call", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // First Doctor - Card view
    private void getDoctorShowFirst() {
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
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Doctor firstDoctor = response.body().get(0);
                    showFirstDoctor(firstDoctor);
                    MaterialButton mbSeeMedical;
                    mbSeeMedical = findViewById(R.id.mbSeeMedicalHome);
                    mbSeeMedical.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int id = firstDoctor.getUser().getId();
                            Intent intent = new Intent(Home.this, MedicalAppointment.class);
                            intent.putExtra("DOCTOR_ID", id);
                            intent.putExtra("TOKEN", token);
                            intent.putExtra("PATIENTE_ID",patientId);
                            intent.putExtra("USER", name);
                            intent.putExtra("LASTNAME", lastname);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(Home.this, "Error in json response", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(Home.this, "Error in the API call", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showFirstDoctor(Doctor doctor) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvdescription);
        TextView tvRating = findViewById(R.id.tvMinutesAgo);
        if (!isDestroyed() && !isFinishing()) {
            loadImageWithGlide(imgView);
            tvTitle.setText(doctor.getUser().getPerson().getName() + " " +
                    doctor.getUser().getPerson().getLastname());
            tvDescription.setText("Especialista en " + doctor.getSpecialty() +
                    " con más de 10 años de experiencia");
            tvRating.setText(doctor.getRating().toString());
        }
    }
    private void loadImageWithGlide(ImageView imageView) {
        if (!isDestroyed() && !isFinishing()) {
            Glide.with(this)
                    .load(R.drawable.profile_default)
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        }
    }
}