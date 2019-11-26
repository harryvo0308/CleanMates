package com.example.harryvo.cleanermates.Model;

public class Category {

    private String Name;
    private String Image;
    private float Rating;

    public Category() {
    }

    public Category(String name, String image, float rating) {
        Name = name;
        Image = image;
        Rating = rating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }
}
