package com.udari.evoraadmin.model;

public class ProductModel {
    // Firestore Keys වලට 100% ක් ගැලපෙන නම් පාවිච්චි කරන්න
    private String productId;
    private String title;
    private String description;
    private String imageUrl;
    private double price;
    private int stockCount;
    private boolean status; // පින්තූරය අනුව මේක true/false (boolean)

    // Firebase එකට අනිවාර්යයෙන්ම හිස් Constructor එකක් ඕනේ
    public ProductModel() {
    }

    public ProductModel(String productId, String title, String description, String imageUrl, double price, int stockCount, boolean status) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockCount = stockCount;
        this.status = status;
    }

    // Getters සහ Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockCount() { return stockCount; }
    public void setStockCount(int stockCount) { this.stockCount = stockCount; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}