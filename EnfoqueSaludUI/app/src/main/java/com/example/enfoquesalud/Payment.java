package com.example.enfoquesalud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Register.InvoiceRegister;
import com.example.enfoquesalud.Model.RequestUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Payment extends AppCompatActivity {
    FragmentTransaction transaction;
    Fragment paymentByCardFragment, digitalWalletFragment;
    MaterialButton btnPaymentAppointment, bmDigitalWallet;
    MaterialTextView txtAmount;
    private int patientdId, appoinmentId;
    private boolean paymentButtonClicked = false;
    private String amount, token, name, lastname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        txtAmount = findViewById(R.id.txtAmount);
        bmDigitalWallet = findViewById(R.id.bmDigitalWallet);
        Intent intent = getIntent();
        patientdId = intent.getIntExtra("PATIENTD_ID", patientdId);
        appoinmentId = intent.getIntExtra("APPOINTMENT_ID", patientdId);
        amount = intent.getStringExtra("PAYMENT_AMOUNT");
        token = getIntent().getStringExtra("TOKEN");
        name = getIntent().getStringExtra("USER");
        lastname = getIntent().getStringExtra("LASTNAME");

        txtAmount.setText("S/"+amount);
        if(patientdId == 0 && token == null){
            Toast.makeText(this, "Error de servidor. Por favor, inicie sesión nuevamente.",
                    Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        // Fragment
        paymentByCardFragment = new PaymentByCardFragment();
        digitalWalletFragment = new DigitalWalletFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.flContainerFragment,paymentByCardFragment).commit();
        // Button
        btnPaymentAppointment = findViewById(R.id.btnPaymentAppointment);
        btnPaymentAppointment.bringToFront();
        btnPaymentAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPaymentAppointment.setEnabled(false);
                paymentButtonClicked = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Desea realizar el pago?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContainerFragment);
                        if (currentFragment instanceof PaymentByCardFragment) {
                            PaymentByCardFragment paymentByCardFragment = (PaymentByCardFragment) currentFragment;
                            if (paymentByCardFragment.validateFields()) {
                                registerPayment();
                            }else{
                                btnPaymentAppointment.setEnabled(true);
                            }
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        btnPaymentAppointment.setEnabled(true);
                        btnPaymentAppointment.setEnabled(true);
                        paymentButtonClicked = false;
                    }
                });
                builder.create().show();
            }
        });
    }
    public void onClick(View view){
        transaction = getSupportFragmentManager().beginTransaction();
        if (view.getId() == R.id.mbPaymentByCard){
            transaction.replace(R.id.flContainerFragment, paymentByCardFragment).commit();
        } else if (view.getId() == R.id.bmDigitalWallet) {
            transaction.replace(R.id.flContainerFragment, digitalWalletFragment).commit();
            btnPaymentAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnPaymentAppointment.setEnabled(false);
                    paymentButtonClicked = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
                    builder.setTitle("Confirmación");
                    builder.setMessage("¿Desea realizar el pago?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerPayment();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            btnPaymentAppointment.setEnabled(true);
                            paymentButtonClicked = false;
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }
    //Register payment
    private void registerPayment(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://enfoquesalud.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
        String authorization = "Bearer " + token;
        InvoiceRegister invoiceRegister = new InvoiceRegister();
        invoiceRegister.setAppointmentId(appoinmentId);
        invoiceRegister.setAmount(Double.parseDouble(amount));
        Call<RequestUser> call = enfoqueSaludApi.savePayment(authorization,invoiceRegister,patientdId);
        call.enqueue(new Callback<RequestUser>() {
            @Override
            public void onResponse(Call<RequestUser> call, Response<RequestUser> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(Payment.this, "No se registro el pago",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Payment.this, "Pago registrado",
                        Toast.LENGTH_SHORT).show();
                Intent intentSearch = new Intent(Payment.this, SatisfactoryPayment.class);
                intentSearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentSearch.putExtra("TOKEN",token);
                intentSearch.putExtra("CURRENT_USER_ID", patientdId);
                intentSearch.putExtra("CURRENT_USER_NAME", name);
                intentSearch.putExtra("CURRENT_USER_LASTNAME", lastname);
                startActivity(intentSearch);
            }

            @Override
            public void onFailure(Call<RequestUser> call, Throwable t) {
                Toast.makeText(Payment.this, "Error "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (paymentButtonClicked) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Por favor, complete el pago antes de regresar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View v){
        onBackPressed();
    }
}