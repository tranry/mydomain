package com.example.mydomain;

import java.util.HashMap;
import java.util.Map;

public class User {
    private Long username;
    private Long money;
    private Long phoneNumber;

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }

    public User(Long username, Long money, Long phoneNumber) {
        this.username = username;
        this.money = money;
        this.phoneNumber = phoneNumber;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public User() {

    }
    public Map<String,Object> toMap()
    {
        HashMap<String,Object> map=new HashMap<>();
        map.put("username",this.getUsername());
        map.put("money",this.getMoney());
        map.put("phoneNumber",this.getPhoneNumber());
        return map;
    }
}
