package com.example.enfoquesalud.Model.Register.Appoinment;

public class Appointment {
    private String date;
    private int doctor_id;
    private int duration;
    private int patient_id;
    private int status;

    public Appointment(String date, int doctor_id, int duration, int patient_id, int status) {
        this.date = date;
        this.doctor_id = doctor_id;
        this.duration = duration;
        this.patient_id = patient_id;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
