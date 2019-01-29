package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class arraymatitem {
    public String getPartn() {
        return partn;
    }

    public void setPartn(String partn) {
        this.partn = partn;
    }

    public String getQtyn() {
        return qtyn;
    }

    public void setQtyn(String qtyn) {
        this.qtyn = qtyn;
    }

    public String getPricen() {
        return pricen;
    }

    public void setPricen(String pricen) {
        this.pricen = pricen;
    }

    public String partn;
    public String qtyn;
    public String pricen;



    JSONObject object;

    public arraymatitem(JSONObject object) throws JSONException {
        this.object = object;
        this.partn = object.getString("part_no");
        this.qtyn = object.getString("qty");
        this.pricen = object.getString("price");

    }

}
