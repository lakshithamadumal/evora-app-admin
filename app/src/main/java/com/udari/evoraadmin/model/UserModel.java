package com.udari.evoraadmin.model;

public class UserModel {
    private String name, mobile, city;
    private int orderCount;


    public UserModel() {
    }

    public UserModel(String name, String mobile, String city, int orderCount) {
        this.name = name;
        this.mobile = mobile;
        this.city = city;
        this.orderCount = orderCount;
    }

    // Getters
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getCity() { return city; }
    public int getOrderCount() { return orderCount; }


    public void setName(String name) { this.name = name; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setCity(String city) { this.city = city; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
}