package com.example.gaurav.inventory.entities;

public class materialissueitem {

    public String partno;
    public String quantity;
    public String price;

    public String getPartno() {
        return partno;
    }

    public materialissueitem(String partno, String quantity, String price) {
        this.partno = partno;
        this.quantity = quantity;
        this.price = price;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
