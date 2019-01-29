package com.example.gaurav.inventory.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class remove_item implements Parcelable {
    public String barcode;
    public String partno;
    public String qty;
    public String custom_no;
    public String pk;
    public String description1;
    public String productpk;
    public String Price;
    public String description_2;
    public String weight;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

;

    public String getCustom_no() {
        return custom_no;
    }

    public void setCustom_no(String custom_no) {
        this.custom_no = custom_no;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription_2() {
        return description_2;
    }

    public void setDescription_2(String description_2) {
        this.description_2 = description_2;
    }




    protected remove_item(Parcel in) {
        barcode = in.readString();
        partno = in.readString();
        qty = in.readString();
        pk = in.readString();
        productpk = in.readString();
        Price = in.readString();
        description1 = in.readString();
        custom_no = in.readString();
        description_2 = in.readString();
        weight = in.readString();

    }

    public static final Creator<remove_item> CREATOR = new Creator<remove_item>() {
        @Override
        public remove_item createFromParcel(Parcel in) {
            return new remove_item(in);
        }

        @Override
        public remove_item[] newArray(int size) {
            return new remove_item[size];
        }
    };




    public String getProductpk() {
        return productpk;
    }

    public void setProductpk(String productpk) {
        this.productpk = productpk;
    }



    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

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

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }



    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public remove_item(String partno, String qty, String pk, String barcode, String description1, String productpk,String price,String description_2,String custom_no,String weight) {
        this.qty = qty;
        this.barcode = barcode;
        this.partno = partno;
        this.description1 = description1;
        this.productpk = productpk;
        this.Price=price;
        this.description_2 = description_2;
        this.custom_no = custom_no;
        this.weight = weight;
        this.pk = pk;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeString(partno);
        dest.writeString(qty);
        dest.writeString(pk);
        dest.writeString(productpk);
        dest.writeString(Price);
        dest.writeString(description1);
        dest.writeString(description_2);
        dest.writeString(custom_no);
        dest.writeString(weight);
    }



    }
