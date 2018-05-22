package com.example.mary.utotal.dataReturn;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mary on 23/04/2018.
 */

public class DataReturn implements Parcelable {


    private String barcode;
    private String productName;
    private String weight;
    private double price;

    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getWeight(){
        return weight;
    }
    public void setWeight(String weight){
        this.weight=weight;
    }
    public String getBarcode(){
        return barcode;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.barcode);
        dest.writeString(this.productName);
        dest.writeString(this.weight);
        dest.writeDouble(this.price);
    }

    public DataReturn() {
    }

    protected DataReturn(Parcel in) {
        this.barcode = in.readString();
        this.productName = in.readString();
        this.weight = in.readString();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<DataReturn> CREATOR = new Parcelable.Creator<DataReturn>() {
        @Override
        public DataReturn createFromParcel(Parcel source) {
            return new DataReturn(source);
        }

        @Override
        public DataReturn[] newArray(int size) {
            return new DataReturn[size];
        }
    };



}//close DataReturn