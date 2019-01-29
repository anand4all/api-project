package com.example.gaurav.inventory.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Grn_item_detail {
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String pk;

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String partno;
    public String qty;

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String description1;



    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String barcode;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String Price;
    public Grn_item_detail(String partno,String qty,String pk,String barcode,String description1,String price) {
        this.qty = qty;
        this.barcode = barcode;
        this.partno=partno;
        this.description1=description1;
        this.pk=pk;

        this.Price=price;


    }



}





  /*  public String pkproductbom;
    public String pkprojectbom;
    public String title;
    public String bompk;

    public String getBompk() {
        return bompk;
    }

    public void setBompk(String bompk) {
        this.bompk = bompk;
    }

    public String getBomqty() {
        return bomqty;
    }

    public void setBomqty(String bomqty) {
        this.bomqty = bomqty;
    }

    public JSONArray getItemArray() {
        return itemArray;
    }

    public void setItemArray(JSONArray itemArray) {
        this.itemArray = itemArray;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ArrayList<Item> getParents() {
        return parents;
    }

    public void setParents(ArrayList<Item> parents) {
        this.parents = parents;
    }

    public String bomqty;
    public String partno;
    public JSONArray itemArray;


   // public Grn_item_detail(String title, String mobile) {
     //this.title = title;
     //this.mobile=mobile;
    Item item;

    ArrayList<Item> parents = new ArrayList<>();
   JSONObject object;

    public Grn_item_detail(JSONObject object) throws JSONException {
        this.object = object;
        this.bompk=object.getString("pk");
        this.bomqty=object.getString("quantity2");



        JSONObject product = object.getJSONObject("products");
        this.pkproductbom = product.getString("pk");
        this.partno = product.getString("part_no");

        this.item = new Item(product);
        this.parents.add(item);

        this.itemArray = object.getJSONArray("project");
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject jsonObj = itemArray.getJSONObject(i);
            this.pkprojectbom = jsonObj.getString("pk");

        }


        //this.title = object.getString("part_no");
        //this.pkprojectbom = object.getString("pk");

    }

  //  public String getPk() {
  //      return pk;
  //  }

    //public void setPk(String pk) {
    //    this.pk = pk;
 //   }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartno() {
       return partno;
   }

   public void setPartno(String partno) {
        this.partno = partno;
   }
    public String getPkproductbom() {
        return pkproductbom;
    }

    public void setPkproductbom(String pkproductbom) {
        this.pkproductbom = pkproductbom;
    }

    public String getPkprojectbom() {
        return pkprojectbom;
    }

    public void setPkprojectbom(String pkprojectbom) {
        this.pkprojectbom = pkprojectbom;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }


} */



