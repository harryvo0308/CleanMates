package com.example.harryvo.cleanermates.Model;

public class Clean {

    private String Name, Image, Decription, Price, Discount, CleanID;

    public Clean() {

    }

    public Clean(String name, String image, String decription, String price, String discount, String cleanID) {
        Name = name;
        Image = image;
        Decription = decription;
        Price = price;
        Discount = discount;
        CleanID = cleanID;
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

    public String getDecription() {
        return Decription;
    }

    public void setDecription(String decription) {
        Decription = decription;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCleanID() {
        return CleanID;
    }

    public void setCleanID(String cleanID) {
        CleanID = cleanID;
    }
}
