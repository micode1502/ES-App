package com.example.enfoquesalud.Model.Read;

public class Medical {
    private String name;
    private int id;
    private String description;
    private String url;
    private Double rating;

    public Medical() {
    }

    public Medical(int id,String name, String description, String url, Double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    public int getId() {
        return id;
    }
}
