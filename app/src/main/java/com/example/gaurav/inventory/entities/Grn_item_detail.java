package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Grn_item_detail {
    public String pk;
    public String title;
    public String mobile;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

   // public Grn_item_detail(String title, String mobile) {
     //this.title = title;
     //this.mobile=mobile;






   JSONObject object;

    public Grn_item_detail(JSONObject object) throws JSONException {
        this.object = object;
        this.pk = object.getString("pk");
        this.title = object.getString("title");
        this.mobile = object.getString("date");



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

    public String getMobile() {
       return mobile;
   }

   public void setMobile(String mobile) {
        this.mobile = mobile;
   }


}



