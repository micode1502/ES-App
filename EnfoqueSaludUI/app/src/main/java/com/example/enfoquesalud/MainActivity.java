package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.enfoquesalud.Interface.EnfoqueSaludApi;
import com.example.enfoquesalud.Model.Read.Patient;
import com.example.enfoquesalud.Model.RequestUser;
import com.example.enfoquesalud.Model.Read.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView btnRecoverPassword, btnRecord;
    private Button btnLogin;
    private TextInputEditText txtUsername, txtPassword;
    private TextInputLayout usernameInputLayout, passwordInputLayout;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInputLayout = findViewById(R.id.layoutUsername);
        passwordInputLayout = findViewById(R.id.layoutPassword);
        btnRecoverPassword = findViewById(R.id.btnRecoverPassword);
        btnRecord = findViewById(R.id.btnRecord);
        btnLogin = findViewById(R.id.btnLogin);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecoverPassword.class);
                startActivity(intent);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordUser.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String validationError = validationLogin();
                if (validationError != null) {
                    usernameInputLayout.setError(validationError.contains("usuario") ?
                                                validationError : null);
                    passwordInputLayout.setError(validationError.contains("contraseña") ?
                                                validationError : null);
                    return;
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://enfoquesalud.onrender.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EnfoqueSaludApi enfoqueSaludApi = retrofit.create(EnfoqueSaludApi.class);
                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                byte[] hash = digest.digest(txtPassword.getText().toString().getBytes(StandardCharsets.UTF_8));
                String strSHA256Password = bytesToHex(hash);
                User user = new User();
                user.setUsername(txtUsername.getText().toString().trim());
                user.setPassword(strSHA256Password);
                Call<RequestUser> call = enfoqueSaludApi.getToken(user);
                call.enqueue(new Callback<RequestUser>() {
                    @Override
                    public void onResponse(Call<RequestUser> call, Response<RequestUser> response) {
                        if (!response.isSuccessful()) {
                            passwordInputLayout.setError("Usuario o Contraseña incorrecta");
                            usernameInputLayout.setError(null);
                            return;
                        }
                        RequestUser requestUser = response.body();
                        token = requestUser.getToken();
                        Patient dataUser = requestUser.getCurrent_user();
                        int id = dataUser.getPatient_id();
                        String name = dataUser.getUser().getPerson().getName();
                        String lastName = dataUser.getUser().getPerson().getLastname();
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("TOKEN",token);
                        intent.putExtra("CURRENT_USER_ID", id);
                        intent.putExtra("CURRENT_USER_NAME", name);
                        intent.putExtra("CURRENT_USER_LASTNAME", lastName);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(Call<RequestUser> call, Throwable t) {
                    }
                });
            }
            private void viewMessage(String message) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
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
    private String validationLogin() {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        return username.isEmpty() ? "Ingrese su nombre de usuario" :
                password.isEmpty() ? "Ingrese su contraseña" : null;
    }
}