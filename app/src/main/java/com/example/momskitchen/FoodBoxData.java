package com.example.momskitchen;

public class FoodBoxData {

    String Name, Favourites, Cart, Image;
    int Price;
    public FoodBoxData(String name, int price, String favourites, String cart, String image) {
        Name = name;
        Price = price;
        Favourites = favourites;
        Cart = cart;
        Image = image;
    }

    public FoodBoxData(){}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getFavourites() {
        return Favourites;
    }

    public void setFavourites(String favourites) {
        Favourites = favourites;
    }

    public String getCart() {
        return Cart;
    }

    public void setCart(String cart) {
        Cart = cart;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
