package com.example.harryvo.cleanermates.Model;

public class Booking {

    private String IDClean;
    private String Kingofclean;
    private String Room;
    private String Price;
    private String Discount;

    public Booking() {
    }

    public Booking(String IDClean, String kingofclean, String room, String price, String discount) {
        this.IDClean = IDClean;
        Kingofclean = kingofclean;
        Room = room;
        Price = price;
        Discount = discount;
    }

    public String getIDClean() {
        return IDClean;
    }

    public void setIDClean(String IDClean) {
        this.IDClean = IDClean;
    }

    public String getKingofclean() {
        return Kingofclean;
    }

    public void setKingofclean(String kingofclean) {
        Kingofclean = kingofclean;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
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
}
