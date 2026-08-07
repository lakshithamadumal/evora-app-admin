package com.udari.evoraadmin.model;

public class UserModel {
    private String name, phoneNumber, city;
    private int orderCount;

    // 1. Firebase එකට අනිවාර්යයෙන්ම empty constructor එකක් ඕනේ
    public UserModel() {
    }

    public UserModel(String name, String phoneNumber, String city, int orderCount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.orderCount = orderCount;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}