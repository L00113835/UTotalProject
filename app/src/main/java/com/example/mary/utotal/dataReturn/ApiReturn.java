package com.example.mary.utotal.dataReturn;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mary on 28/04/2018.
 */

public class ApiReturn implements Parcelable {

    private String gtin;
    private String description;
    private String name;
    private double price;

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gtin);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
    }

    public ApiReturn() {
    }

    protected ApiReturn(Parcel in) {
        this.gtin = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.price = in.readDouble();
    }

    public static final Parcelable.Creator<ApiReturn> CREATOR = new Parcelable.Creator<ApiReturn>() {
        @Override
        public ApiReturn createFromParcel(Parcel source) {
            return new ApiReturn(source);
        }

        @Override
        public ApiReturn[] newArray(int size) {
            return new ApiReturn[size];
        }
    };
}