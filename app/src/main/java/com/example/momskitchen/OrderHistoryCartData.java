package com.example.momskitchen;

public class OrderHistoryCartData {

    String id, img, name;
    int quantity, price, totprice;

    public OrderHistoryCartData(String id, String img, String name, int quantity, int price, int totprice) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totprice = totprice;
    }

    public OrderHistoryCartData() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotprice() {
        return totprice;
    }

    public void setTotprice(int totprice) {
        this.totprice = totprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
