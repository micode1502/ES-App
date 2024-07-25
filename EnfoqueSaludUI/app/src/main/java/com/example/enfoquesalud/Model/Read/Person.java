package com.example.enfoquesalud.Model.Read;

import java.util.Date;

public class Person {
    private String document_number;
    private int document_type;
    private int gender;
    private String lastname;
    private String name;
    private  Date born_date;
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getBorn_date() {
        return born_date;
    }

    public void setBorn_date(Date born_date) {
        this.born_date = born_date;
    }

    public String getDocumentNumber() {
        return document_number;
    }
    public int getDocumentType() {
        return document_type;
    }
    public int getGender() {
        return gender;
    }
    public String getLastname() {
        return lastname;
    }
    public String getName() {
        return name;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public void setDocument_type(int document_type) {
        this.document_type = document_type;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setName(String name) {
        this.name = name;
    }
}
