package com.example.enfoquesalud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PaymentByCardFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    private TextInputLayout numberCard, dateExpiry, cvv, nameComplete;
    private TextInputEditText txtNumberCard, txtDateExpiry, txtCvv, txtNameComplete;

    public PaymentByCardFragment() {
    }

    public static PaymentByCardFragment newInstance() {
        return new PaymentByCardFragment();
    }

    public static PaymentByCardFragment newInstance(String param1, String param2) {
        PaymentByCardFragment fragment = new PaymentByCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_by_card, container, false);
        txtNumberCard = view.findViewById(R.id.txtNumberCard);
        txtDateExpiry = view.findViewById(R.id.txtDateExpiry);
        txtCvv = view.findViewById(R.id.txtCvv);
        txtNameComplete = view.findViewById(R.id.txtNameComplete);
        numberCard = view.findViewById(R.id.tilCardNumber);
        dateExpiry = view.findViewById(R.id.tilExpiryDate);
        cvv = view.findViewById(R.id.tilCVV);
        nameComplete = view.findViewById(R.id.tilNameComplete);
        return view;
    }

    public String getCardNumber() {
        return txtNumberCard.getText().toString();
    }

    public String getDateExpiry() {
        return txtDateExpiry.getText().toString();
    }

    public String getCvv() {
        return txtCvv.getText().toString();
    }

    public String getNameComplete() {
        return txtNameComplete.getText().toString();
    }

    public boolean validateFields() {
        boolean isValid = true;

        String cardNumber = getCardNumber();
        String date = getDateExpiry();
        String cvvv = getCvv();
        String name = getNameComplete();

        if (TextUtils.isEmpty(cardNumber) || cardNumber.length() < 16) {
            numberCard.setError("Ingrese un número de tarjeta válido");
            isValid = false;
        } else {
            numberCard.setError(null);
        }
        if (TextUtils.isEmpty(date)) {
            dateExpiry.setError("Ingrese una fecha de caducidad válida (mm/yy)");
            isValid = false;
        } else {
            dateExpiry.setError(null);
        }
        if (TextUtils.isEmpty(cvvv) || cvvv.length() != 3) {
            cvv.setError("Ingrese un CVV válido (3 dígitos)");
            isValid = false;
        } else {
            cvv.setError(null);
        }
        if (TextUtils.isEmpty(name)) {
            nameComplete.setError("Ingrese el nombre completo");
            isValid = false;
        } else {
            nameComplete.setError(null);
        }

        return isValid;
    }
}