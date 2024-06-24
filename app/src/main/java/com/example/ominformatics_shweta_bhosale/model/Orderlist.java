package com.example.ominformatics_shweta_bhosale.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Orderlist implements Serializable {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("order_no")
    private String orderNo;
    @SerializedName("customer_name")
    private String customerName;
    // Add other fields as necessary

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
