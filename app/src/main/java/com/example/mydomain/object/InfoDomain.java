package com.example.mydomain.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class InfoDomain implements Parcelable {
    private String uid;
    private String namedomain;
    private int imgdomain;
    private int pricedomain;
    private int history;
    private String key;

    public InfoDomain() {

    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InfoDomain(String uid, String namedomain, int imgdomain, int pricedomain, int history) {
        this.uid = uid;
        this.namedomain = namedomain;
        this.imgdomain = imgdomain;
        this.pricedomain = pricedomain;
        this.history = history;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamedomain() {
        return namedomain;
    }

    public void setNamedomain(String namedomain) {
        this.namedomain = namedomain;
    }

    public int getImgdomain() {
        return imgdomain;
    }

    public void setImgdomain(int imgdomain) {
        this.imgdomain = imgdomain;
    }

    public int getPricedomain() {
        return pricedomain;
    }

    public void setPricedomain(int pricedomain) {
        this.pricedomain = pricedomain;
    }

    public InfoDomain(Parcel in) {
    }

    ;

    public Map<String, Object> toMap() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("uid", getUid());
        data.put("namedomain", getNamedomain());
        data.put("imgdomain", getImgdomain());
        data.put("pricedomain", getPricedomain());
        data.put("history", getHistory());

        return data;
    }

    public Map<String, Object> toMapKey() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("uid", getUid());
        data.put("namedomain", getNamedomain());
        data.put("imgdomain", getImgdomain());
        data.put("pricedomain", getPricedomain());
        data.put("history", getHistory());
        data.put("key", getKey());

        return data;
    }

    public static final Creator<InfoDomain> CREATOR = new Creator<InfoDomain>() {
        @Override
        public InfoDomain createFromParcel(Parcel in) {
            return new InfoDomain(in);
        }

        @Override
        public InfoDomain[] newArray(int size) {
            return new InfoDomain[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
