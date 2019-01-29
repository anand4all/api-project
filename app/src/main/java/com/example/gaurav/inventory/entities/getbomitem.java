package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class getbomitem {

    JSONObject object;

    public getbomitem(JSONObject object) throws JSONException {
        this.object = object;
      //  this.pkprojectbom = object.getString("pk");
        //this.pkprojectbom = object.getString("pk");
          this.bompk = object.getString("pk");




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

    public String pkproductbom;
    public String pkprojectbom;
    public String bompk;
}
