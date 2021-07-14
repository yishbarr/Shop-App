package com.example.shopmanager.models;

public class Product {
    public String id;
    public String name;
    public int quantity;
    public int shelf;

    public Product(String id, String name, int quantity, int shelf) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.shelf = shelf;
    }
}
