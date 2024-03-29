package com.example.momskitchen;

public class OrderData {

    String id, name, image, total_price;
    int quantity, price;
    public OrderData(String id, String name, int price, String image, int quantity, String total_price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.total_price=total_price;
    }

    public OrderData(String id, String name, int price, String image, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public OrderData(){}

//    public OrderData(String id, String name, String price, String image) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.image = image;
//    }
//
//    public OrderData(String name, String price, String image) {
//        this.name = name;
//        this.price = price;
//        this.image = image;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
