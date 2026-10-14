package com.udari.evoraadmin.model;

public class UserModel {
    private String id;
    private String name;
    private String phoneNumber;
    private String city;
    private int orderCount;

    public UserModel() {
    }

    public UserModel(String name, String phoneNumber, String city, int orderCount) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.orderCount = orderCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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