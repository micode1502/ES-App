package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Register.UserRegister;
import com.example.enfoquesalud.Model.RequestUser;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordUser extends AppCompatActivity {
    private TextView btnMain;
    private AutoCompleteTextView txtTypeDocument;
    private AutoCompleteTextView autoCompleteGender;
    private TextInputEditText txtBirthday, txtName,txtLastName,txtNumDocument, txtUsername,
                            txtPassword, txtPhone;
    private TextInputLayout layoutNameCompleted, layoutLastNameCompleted, layoutTypeDocument,
                            layoutNumDocument, layoutGender, layoutDateOfBirth, layoutPhone,
                            layoutUsernameRecord, layoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_user);

        btnMain = findViewById(R.id.btnMain);
        layoutNameCompleted = findViewById(R.id.layoutNameCompleted);
        layoutLastNameCompleted = findViewById(R.id.layoutLastNameCompleted);
        layoutTypeDocument = findViewById(R.id.layoutTypeDocument);
        layoutNumDocument = findViewById(R.id.layoutNumDocument);
        layoutGender = findViewById(R.id.layoutGender);
        layoutDateOfBirth = findViewById(R.id.layoutDateOfBirth);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutUsernameRecord = findViewById(R.id.layoutUsernameRecord);
        layoutPassword = findViewById(R.id.layoutPassword);
        // Document Type
        txtTypeDocument = findViewById(R.id.txtTypeDocument);
        String[] documentOptions = {"DNI", "C. Extrangería"};
        ArrayAdapter<String> adapterD = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, documentOptions);
        txtTypeDocument.setAdapter(adapterD);
        // Gender
        autoCompleteGender = findViewById(R.id.autoCompleteGender);
        String[] genderOptions = {"Masculino", "Femenino"};
        ArrayAdapter<String> adapterG = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, genderOptions);
        autoCompleteGender.setAdapter(adapterG);
        // Birdday
        txtBirthday = findViewById(R.id.txtBirthday);
        txtBirthday.setInputType(InputType.TYPE_NULL);
        txtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // El elemento está enfocado
                    MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                    builder.setTitleText("Fecha de Nacimiento");
                    builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
                    MaterialDatePicker datePicker = builder.build();
                    datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
                    txtBirthday.setHint("01/01/2000");
                    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(Object selection) {
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
                            calendar.setTimeInMillis((Long) selection);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String date = dateFormat.format(calendar.getTime());
                            txtBirthday.setText(date);
                        }
                    });
                } else {
                    txtBirthday.setHint("");
                }
            }
        });


    }

    public void registerUser(View view) {
        if (validateFields()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://enfoquesalud.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
            txtName = findViewById(R.id.txtNameCompleted);
            txtLastName = findViewById(R.id.txtLastNameCompleted);
            txtNumDocument = findViewById(R.id.txtNumDocument);
            txtUsername = findViewById(R.id.txtUsernameRecord);
            txtPassword = findViewById(R.id.txtPassword);
            txtBirthday = findViewById(R.id.txtBirthday);
            txtPhone = findViewById(R.id.txtPhone);
            String name = txtName.getText().toString();
            String lastName = txtLastName.getText().toString();
            String numDocument = txtNumDocument.getText().toString();
            String fechNacimient = txtBirthday.getText().toString();

            String username = txtUsername.getText().toString();
            String phone = txtPhone.getText().toString();
            String selectedDocument = txtTypeDocument.getText().toString();
            String selectedGender = autoCompleteGender.getAdapter().toString();

            int positionD = 0, positionG = 0;
            String[] documentOptions = {"DNI", "C. Extrangería"};
            String[] genderOptions = {"Masculino", "Femenino"};
            for (int i = 0; i < documentOptions.length; i++) {
                if (documentOptions[i].equals(selectedDocument)) {
                    positionD = i;
                    break;
                }
            }
            for (int i = 0; i < genderOptions.length; i++) {
                if (genderOptions[i].equals(selectedGender)) {
                    positionG = i;
                    break;
                }
            }
            int tipeDocument = positionD + 1;
            int gender = positionG + 1;
            String password = txtPassword.getText().toString();
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String strSHA256Password = bytesToHex(hash);
            UserRegister userRegister = new UserRegister();
            userRegister.setUsername(username);
            userRegister.setPassword(strSHA256Password);
            userRegister.setName(name);
            userRegister.setLastname(lastName);
            userRegister.setDocument_type(tipeDocument);
            userRegister.setDocument_number(numDocument);
            userRegister.setGender(gender);
            userRegister.setBorn_date(fechNacimient);
            userRegister.setPhone_number(phone);
            Call<RequestUser> call = enfoqueSaludApi.saveUser(userRegister);
            call.enqueue(new Callback<RequestUser>() {
                @Override
                public void onResponse(Call<RequestUser> call, retrofit2.Response<RequestUser> response) {
                    if (!response.isSuccessful()) {
                        viewMessage("Código: " + response.code());
                        return;
                    }
                    RequestUser responseApi = response.body();
                    viewMessage(responseApi.getMessage());
                    Intent intent = new Intent(RecordUser.this, MainActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<RequestUser> call, Throwable t) {
                    viewMessage("Error en la solicitud: " + t.getMessage());
                }
                private void viewMessage(String message) {
                    runOnUiThread(() -> Toast.makeText(RecordUser.this, message, Toast.LENGTH_SHORT).show());
                }
            });
        }
    }
    private boolean validateFields() {
        boolean isValid = true;

        String name = layoutNameCompleted.getEditText().getText().toString().trim();
        layoutNameCompleted.setError(name.isEmpty() ? "Ingrese su nombre" : null);
        isValid &= name.isEmpty() ? false : true;

        String lastName = layoutLastNameCompleted.getEditText().getText().toString().trim();
        layoutLastNameCompleted.setError(lastName.isEmpty() ? "Ingrese sus apellidos" : null);
        isValid &= lastName.isEmpty() ? false : true;

        String typeDocument = layoutTypeDocument.getEditText().getText().toString().trim();
        layoutTypeDocument.setError(typeDocument.isEmpty() ? "Seleccione el tipo de documento" : null);
        isValid &= typeDocument.isEmpty() ? false : true;

        String numDocument = layoutNumDocument.getEditText().getText().toString().trim();
        layoutNumDocument.setError(numDocument.isEmpty() ? "Ingrese su número de documento" : null);
        isValid &= numDocument.isEmpty() ? false : true;

        String gender = layoutGender.getEditText().getText().toString().trim();
        layoutGender.setError(gender.isEmpty() ? "Seleccione el género" : null);
        isValid &= gender.isEmpty() ? false : true;

        String dateOfBirth = layoutDateOfBirth.getEditText().getText().toString().trim();
        layoutDateOfBirth.setError(dateOfBirth.isEmpty() ? "Ingrese su fecha de nacimiento" : null);
        isValid &= dateOfBirth.isEmpty() ? false : true;

        String phone = layoutPhone.getEditText().getText().toString().trim();
        layoutPhone.setError(phone.isEmpty() ? "Ingrese su número de teléfono" : null);
        isValid &= phone.isEmpty() ? false : true;

        String username = layoutUsernameRecord.getEditText().getText().toString().trim();
        layoutUsernameRecord.setError(username.isEmpty() ? "Ingrese su nombre de usuario" : null);
        isValid &= username.isEmpty() ? false : true;

        String password = layoutPassword.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            layoutPassword.setError("Debe ingresar su contraseña");
            isValid = false;
        } else if (password.length() < 8) {
            layoutPassword.setError("La contraseña debe tener al menos 8 caracteres");
            isValid = false;
        } else {
            layoutPassword.setError(null);
        }

        return isValid;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}