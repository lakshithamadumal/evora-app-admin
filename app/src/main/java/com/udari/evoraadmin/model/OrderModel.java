package com.udari.evoraadmin.model;
import java.util.List;

public class OrderModel {
    public String userName, status, date, phone, city, address, productsSummary;
    public double total;

    public OrderModel(String userName, String status, String date, String phone, String city, String address, String productsSummary, double total) {
        this.userName = userName;
        this.status = status;
        this.date = date;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.productsSummary = productsSummary;
        this.total = total;
    }
}