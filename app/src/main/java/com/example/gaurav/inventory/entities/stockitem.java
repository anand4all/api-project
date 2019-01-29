package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class stockitem {

    public String getStockpk() {
        return stockpk;
    }

    public void setStockpk(String stockpk) {
        this.stockpk = stockpk;
    }

    public String productpk;
    public String stockpk;

    public String getProductpk() {
        return productpk;
    }

    public void setProductpk(String productpk) {
        this.productpk = productpk;
    }

    public String getPartnoumber() {
        return partnoumber;
    }

    public void setPartnoumber(String partnoumber) {
        this.partnoumber = partnoumber;
    }

    public String partnoumber;

    public String getStockitempk() {
        return stockitempk;
    }

    public void setStockitempk(String stockitempk) {
        this.stockitempk = stockitempk;
    }

    public String stockitempk;

    public String getStockitemqty() {
        return stockitemqty;
    }

    public void setStockitemqty(String stockitemqty) {
        this.stockitemqty = stockitemqty;
    }

    public String stockitemqty;



    JSONObject object;

    public stockitem(JSONObject object) throws JSONException {
        this.object = object;
        this.stockitempk = object.getString("pk");
        this.productpk = object.getString("productpk");
        this.partnoumber = object.getString("part_nopro");
        this.stockpk = object.getString("stockpk");
        this.stockitemqty = object.getString("qty");




    }


}
