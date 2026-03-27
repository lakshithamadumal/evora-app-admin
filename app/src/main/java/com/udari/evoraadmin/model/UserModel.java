package com.udari.evoraadmin.model;

public class UserModel {
    String name, mobile, city;
    int orderCount;

    public UserModel(String name, String mobile, String city, int orderCount) {
        this.name = name;
        this.mobile = mobile;
        this.city = city;
        this.orderCount = orderCount;
    }

    // Getters (Adapter එකට දත්ත ගන්න ඕනේ නිසා)
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getCity() { return city; }
    public int getOrderCount() { return orderCount; }
}