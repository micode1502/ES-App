package com.example.enfoquesalud.Model;

import com.example.enfoquesalud.Model.Read.Patient;
public class RequestUser {
    private String message;
    private String token;
    private int id;
    private Patient current_user;

    public Patient getCurrent_user() {
        return current_user;
    }
    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
    public int getAppointmentId() {
        return id;
    }
}
