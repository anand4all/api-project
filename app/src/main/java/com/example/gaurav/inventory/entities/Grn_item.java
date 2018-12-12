package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Grn_item {
    public String pk;
    public String title;
    public String date;
    public String mobile;
    public String status;
    public String name;


    JSONObject object;

    public Grn_item(JSONObject object) throws JSONException {
        this.object = object;
        this.pk = object.getString("pk");
        this.title = object.getString("title");
        this.date = object.getString("date");
        this.mobile=object.getString("mobile");
        this.status=object.getString("status");
        this.name=object.getString("name");
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

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



