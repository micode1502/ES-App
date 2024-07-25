package com.example.enfoquesalud.Model.Read;

public class Doctor {
    private String med_license;
    private String specialty;
    private Double rating;
    private Double salary_hour;
    private User user;

    public Doctor(String med_license, String specialty,Double rating, User user) {
        this.med_license = med_license;
        this.specialty = specialty;
        this.rating = rating;
        this.user = user;
    }
    public Double getRating() {
        return rating;
    }
    public Double getSalaryHour() {
        return salary_hour;
    }
    public String getMedLicense() {
        return med_license;
    }
    public String getSpecialty() {
        return specialty;
    }
    public User getUser() {
        return user;
    }
}
