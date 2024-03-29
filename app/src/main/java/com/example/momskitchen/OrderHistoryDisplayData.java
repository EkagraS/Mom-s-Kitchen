package com.example.momskitchen;

import java.util.ArrayList;

public class OrderHistoryDisplayData {

    String id, date, receiverName, receiverAddress;
    ArrayList<OrderHistoryCartData> orderHistoryCartDataArrayList;

    public OrderHistoryDisplayData() {}

    public OrderHistoryDisplayData(String id, String date, String receiverName, String receiverAddress) {
        this.id = id;
        this.date = date;
        this.receiverName = receiverName;
        this.receiverAddress=receiverAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

}
