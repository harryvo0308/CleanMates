package com.example.harryvo.cleanermates.Model;

import java.util.List;

public class Request {

    private  String phone;
    private String address;
    private String name;
    private String total;
    private String status;
    private List<Booking> cleans;

    public Request() {
    }

    public Request(String phone, String address, String name, String total, List<Booking> cleans) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.total = total;
        this.cleans = cleans;
        this.status="0"; // Default is 0, 0: Booked, 1: Confirming, 2: Confirmed
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Booking> getCleans() {
        return cleans;
    }

    public void setCleans(List<Booking> cleans) {
        this.cleans = cleans;
    }
}
