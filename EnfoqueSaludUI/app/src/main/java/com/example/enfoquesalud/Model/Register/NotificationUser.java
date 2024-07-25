package com.example.enfoquesalud.Model.Register;

import com.example.enfoquesalud.Model.Read.Appointment;

public class NotificationUser {
    private int appointment_id;
    private int patient_id;
    private int status;
    private Appointment appointment;

    public NotificationUser(int appointment_id, int patient_id, int status, Appointment appointment) {
        this.appointment_id = appointment_id;
        this.patient_id = patient_id;
        this.status = status;
        this.appointment = appointment;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public int getStatus() {
        return status;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
