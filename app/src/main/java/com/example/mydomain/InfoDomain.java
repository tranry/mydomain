package com.example.mydomain;

public class InfoDomain {
    private String uid;
    private String nameDomain;
    private int imgDomain;
    private int priceDomain;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameDomain() {
        return nameDomain;
    }

    public void setNameDomain(String nameDomain) {
        this.nameDomain = nameDomain;
    }

    public int getImgDomain() {
        return imgDomain;
    }

    public void setImgDomain(int imgDomain) {
        this.imgDomain = imgDomain;
    }

    public int getPriceDomain() {
        return priceDomain;
    }

    public void setPriceDomain(int priceDomain) {
        this.priceDomain = priceDomain;
    }

    public InfoDomain(String uid, String nameDomain, int imgDomain, int priceDomain) {
        this.uid = uid;
        this.nameDomain = nameDomain;
        this.imgDomain = imgDomain;
        this.priceDomain = priceDomain;
    }
}
