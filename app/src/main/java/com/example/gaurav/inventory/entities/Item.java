package com.example.gaurav.inventory.entities;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public String pk;
    public String created;
    public String part_no;
    public String description_1;
    public String description_2;
    public String parent;
    public String weight;
    public String price;
    public String customs_no;
    public String sheet;

    public String getTotalquantity() {
        return totalquantity;
    }

    public void setTotalquantity(String totalquantity) {
        this.totalquantity = totalquantity;
    }

    public String totalquantity;
    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String Barcode;




  //  ArrayList<String> product = new ArrayList<>();
    JSONObject object;

    public Item(JSONObject object) throws JSONException {
        this.object = object;
        this.pk = object.getString("pk");
        this.part_no = object.getString("part_no");
        this.description_1 = object.getString("description_1");
        this.description_2 = object.getString("description_2");
        this.parent=object.getString("parent");
        this.weight = object.getString("weight");
        this.price = object.getString("price");
        this.customs_no = object.getString("customs_no");
        this.sheet = object.getString("sheet");
        this.created = object.getString("created");
        this.Barcode = object.getString("bar_code");
        this.totalquantity = object.getString("total_quantity");
      /*this.part_no = object.getString("name");
        this.description_1 = object.getString("password");
        this.description_2 = object.getString("contact");
        this.weight = object.getString("country");
     /* this.price = object.getString("createdby");
        this.customs_no = object.getString("publisher");
        this.sheet = object.getString("imageurl");
        this.created = object.getString("bio"); */



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

    public String getDescription_1() {
        return description_1;
    }

    public void setDescription_1(String description_1) {
        this.description_1 = description_1;
    }

    public String getDescription_2() {
        return description_2;
    }

    public void setDescription_2(String description_2) {
        this.description_2 = description_2;
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

    public String getCustoms_no() {
        return customs_no;
    }

    public void setCustoms_no(String customs_no) {
        this.customs_no = customs_no;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }



  //  public void addProductList(String productlist) {
 //       this.product.add(productlist);
  //  }
  //  public ArrayList<String> getProductList(){ return this.product; }

}
