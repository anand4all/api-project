package com.example.gaurav.inventory.entities;

public class remove_item {
    private String name;
    private int id;
    private int qty;


    public remove_item(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
