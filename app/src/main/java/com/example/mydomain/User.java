package com.example.mydomain;

public class User {
    private Long id;
    private Long money;
    private Long phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User(Long id, Long money, Long phoneNumber) {
        this.id = id;
        this.money = money;
        this.phoneNumber = phoneNumber;
    }
    public User() {
    }
}
