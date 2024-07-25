package com.example.enfoquesalud.Model.Read;

import com.example.enfoquesalud.Model.Read.Person;

public class User {
    private int id;
    private String username;
    private String password;
    private Person person;



    public User(){
        this.username = null;
        this.password = null;
        this.person= new Person();
    }
    public User(int id, String username, String password, Person person) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.person = person;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUsername() {
        return username;
    }
}
