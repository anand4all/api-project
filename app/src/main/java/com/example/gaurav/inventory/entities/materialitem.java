package com.example.gaurav.inventory.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class materialitem {
    public String titlemat;
    public String qtymat;

    public String getComm_nr() {
        return comm_nr;
    }

    public void setComm_nr(String comm_nr) {
        this.comm_nr = comm_nr;
    }

    public String comm_nr;

    public String getDateofisuue() {
        return dateofisuue;
    }

    public void setDateofisuue(String dateofisuue) {
        this.dateofisuue = dateofisuue;
    }

    public String dateofisuue;



    JSONObject object;

    public materialitem(JSONObject object) throws JSONException {
        this.object = object;
       // this.pk = object.getString("pk");
        this.titlemat = object.getString("title");
        this.partno = object.getString("part_no");
        this.qtymat=object.getString("qty");
        this.pricemat=object.getString("price");
        this.usernam=object.getString("user");
        this.dateofisuue=object.getString("date");
        this.comm_nr=object.getString("comm_nr");
    }

    public String getTitlemat() {
        return titlemat;
    }

    public void setTitlemat(String titlemat) {
        this.titlemat = titlemat;
    }

    public String getQtymat() {
        return qtymat;
    }

    public void setQtymat(String qtymat) {
        this.qtymat = qtymat;
    }

    public String getPricemat() {
        return pricemat;
    }

    public void setPricemat(String pricemat) {
        this.pricemat = pricemat;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getUsernam() {
        return usernam;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public String pricemat;
    public String partno;
    public String usernam;

}
