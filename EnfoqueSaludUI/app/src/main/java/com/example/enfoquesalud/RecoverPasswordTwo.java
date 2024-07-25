package com.example.enfoquesalud;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class RecoverPasswordTwo extends AppCompatActivity {
    private Button btnVerify;
    private EditText txtCod, txtCodtwo, txtCodthree, txtCodfour, txtCodfive, txtCodsix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password_two);

        btnVerify = findViewById(R.id.btnVerify);
        txtCod = findViewById(R.id.txtCod);
        txtCodtwo = findViewById(R.id.txtCodtwo);
        txtCodthree = findViewById(R.id.txtCodthree);
        txtCodfour = findViewById(R.id.txtCodfour);
        txtCodfive = findViewById(R.id.txtCodfive);
        txtCodsix = findViewById(R.id.txtCodsix);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecoverPasswordTwo.this, RecoverPasswordThree.class);
                startActivity(intent);
            }
        });

        setEditTextWatcher(txtCod, txtCodtwo);
        setEditTextWatcher(txtCodtwo, txtCodthree);
        setEditTextWatcher(txtCodthree, txtCodfour);
        setEditTextWatcher(txtCodfour, txtCodfive);
        setEditTextWatcher(txtCodfive, txtCodsix);
        setEditTextWatcher(txtCodsix, null);

        // Ocultar teclado al tocar fuera de un EditText
        setupUI(findViewById(R.id.constraintLayout));
    }

    private void setEditTextWatcher(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No es necesario implementar este método
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    // Si hay más de un dígito, deja solo el primer dígito
                    editable.delete(1, editable.length());
                    // Si estamos en el último EditText, ocultar el teclado
                    if (nextEditText == null) {
                        hideSoftKeyboard();
                    } else {
                        // Si hay un dígito, pasa al siguiente EditText
                        nextEditText.requestFocus();
                    }
                }
            }
        });
    }

    private void setupUI(View view) {
        // Configurar el evento onTouch para ocultar el teclado al tocar fuera de un EditText
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        // Recorrer todos los Edit text de la vista y aplicar la configuración
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void goBack(View v) {
        onBackPressed();
    }
}
