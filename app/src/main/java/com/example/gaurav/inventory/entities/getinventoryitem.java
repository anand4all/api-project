package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class getinventoryitem {

    public String pk;

    public String getProductpk() {
        return productpk;
    }

    public void setProductpk(String productpk) {
        this.productpk = productpk;
    }

    public String productpk;
    public String part_no;

    public String getDescription_1() {
        return description_1;
    }

    public void setDescription_1(String description_1) {
        this.description_1 = description_1;
    }

    public String description_1;
    public String description_2;
    public String parent;
    public String weight;
    public String price;
    public String totalVal;

    public String getTotalVal() {
        return totalVal;
    }

    public void setTotalVal(String totalVal) {
        this.totalVal = totalVal;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String totalprice;

    public String getDescription_2() {
        return description_2;
    }

    public void setDescription_2(String description_2) {
        this.description_2 = description_2;
    }

    public String getCustoms_no() {
        return customs_no;
    }

    public void setCustoms_no(String customs_no) {
        this.customs_no = customs_no;
    }

    public String customs_no;

    public String getItempk() {
        return itempk;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }

    public String itempk;



    JSONObject object;

    public getinventoryitem(JSONObject object) throws JSONException {
        this.object = object;
        this.qty = object.getString("totalqty");
        this.productpk = object.getString("productpk");
        this.part_no = object.getString("part_no");
        this.description_1 = object.getString("description_1");
        this.description_2 = object.getString("description_2");
     //   this.parent=object.getString("parent");
        this.weight = object.getString("weight");
        this.price = object.getString("price");
        this.totalprice = object.getString("totalprice");
        this.totalVal = object.getString("totalVal");
     //   this.customs_no = object.getString("customs_no");
      //  this.sheet = object.getString("sheet");
      ////  this.created = object.getString("created");
        this.Brcode = object.getString("bar_code");
      //  this.itempk = object.getString("bar_code");



    }


    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrcode() {
        return Brcode;
    }

    public void setBrcode(String brcode) {
        Brcode = brcode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String Brcode;
    public String qty;
    public String rate;



}
