package com.udari.evoraadmin.model;

import com.google.firebase.Timestamp; // මේක අනිවාර්යයෙන්ම ඕනේ
import java.util.List;
import java.util.Map;

public class OrderModel {
    private String orderId, customerName, customerPhone, deliveryAddress, deliveryCity, status;
    private double totalAmount;
    private List<Map<String, Object>> items;

    // String වෙනුවට Timestamp පාවිච්චි කරන්න
    private Timestamp timestamp;

    public OrderModel() { }

    // Getter එක මෙහෙම හදන්න
    public Timestamp getTimestamp() { return timestamp; }

    // අපිට පෙන්වන්න ඕනේ String එකක් නිසා මෙහෙම Getter එකක් අලුතින් හදමු
    public String getFormattedDate() {
        if (timestamp != null) {
            // Timestamp එක Date එකක් කරලා String එකක් විදිහට දෙනවා
            return timestamp.toDate().toString();
        }
        return "No Date";
    }

    // අනිත් Getters ටික කලින් වගේම තියන්න...
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getDeliveryCity() { return deliveryCity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public List<Map<String, Object>> getItems() { return items; }
}