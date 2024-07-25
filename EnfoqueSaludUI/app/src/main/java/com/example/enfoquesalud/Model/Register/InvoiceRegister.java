package com.example.enfoquesalud.Model.Register;

public class InvoiceRegister {
    private int appointment_id;
    private Double amount;

    public InvoiceRegister(int appointment_id, Double amount) {
        this.appointment_id = appointment_id;
        this.amount = amount;
    }

    public InvoiceRegister() {
    }

    public void setAppointmentId(int appointment_id) {
        this.appointment_id = appointment_id;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
