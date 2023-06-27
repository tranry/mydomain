package com.example.mydomain;

public class Domain {
    private String tenMien;
    private int gia;

    public String getTenMien() {
        return tenMien;
    }

    public void setTenMien(String tenMien) {
        this.tenMien = tenMien;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Domain(String tenMien, int gia) {
        this.tenMien = tenMien;
        this.gia = gia;
    }
    public int getGia(String domain)
    {
        gia= domain.equals("com")?200000:domain.equals("net")?100000:0;
        return gia;
    }
}
