package com.udari.evoraadmin.model;

public class ProductModel {
    private String title;
    private double price;
    private int stock;
    private String imageUrl;
    private String productId; // පසුව Edit/Delete කිරීමට අවශ්‍ය වේ

    // හිස් Constructor එකක් Firestore සඳහා අනිවාර්යයි
    public ProductModel() {}

    public ProductModel(String title, double price, int stock, String imageUrl) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters සහ Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}