package com.example.enfoquesalud.Model.Read;

public class Patient {
    private int id;
    private int patient_id;
    private String username;
    private User user;

    public int getId() {
        return id;
    }
    public int getPatient_id() {
        return patient_id;
    }
    public String getUsername() {
        return username;
    }
    public User getUser() {
        return user;
    }

    public Patient(int id,int patient_id, String username,User user) {
        this.id = id;
        this.patient_id = patient_id;
        this.username = username;
        this.user = user;
    }
}
