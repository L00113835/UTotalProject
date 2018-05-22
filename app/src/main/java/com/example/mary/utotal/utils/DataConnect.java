package com.example.mary.utotal.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mary on 23/04/2018.
 */

public class DataConnect implements Parcelable {

    private String endPoint;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public String getEncodeParams() {
        StringBuilder stbu = new StringBuilder();
        for (String key : params.keySet()) {
            String value = null;

            try { //setting the key value and value value
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (stbu.length() > 0) {
                stbu.append("?");//adding the ? to the end of the url php file
            } //binding the key and value together with the = sign
            stbu.append(key).append("=").append(value);

            Log.d("getEncodeParams", stbu.toString());
        }
        return stbu.toString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endPoint);
        dest.writeString(this.method);
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public DataConnect() {
    }

    protected DataConnect(Parcel in) {
        this.endPoint = in.readString();
        this.method = in.readString();
        int paramsSize = in.readInt();
        this.params = new HashMap<String, String>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
    }

    public static final Parcelable.Creator<DataConnect> CREATOR = new Parcelable.Creator<DataConnect>() {
        @Override
        public DataConnect createFromParcel(Parcel source) {
            return new DataConnect(source);
        }

        @Override
        public DataConnect[] newArray(int size) {
            return new DataConnect[size];
        }
    };
}//close dataconnect
