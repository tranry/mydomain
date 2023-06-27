package com.example.mydomain;

public class DataObject {
    private boolean available;
    private String days_to_expire;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDays_to_expire() {
        return days_to_expire;
    }

    public void setDays_to_expire(String days_to_expire) {
        this.days_to_expire = days_to_expire;
    }

    public DataObject(boolean status, String domain) {
        this.available = status;
        this.days_to_expire = domain;
    }


    public boolean isAvailable() {
        return available;
    }

}
