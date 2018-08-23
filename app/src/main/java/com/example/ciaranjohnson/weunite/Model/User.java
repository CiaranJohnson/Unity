package com.example.ciaranjohnson.weunite.Model;

public class User {

    private String Name;
    private int Coins;
    private int NumRequests;
    private int Phone;

    public User() {
    }

    public User(String name, int coins, int numRequests, int phoneNumber) {
        Name = name;
        Coins = coins;
        NumRequests = numRequests;
        Phone = phoneNumber;
    }

    public int getPhoneNumber() {
        return Phone;
    }

    public void setPhoneNumber(int phoneNumber) {
        Phone = phoneNumber;
    }

    public int getNumRequests() {
        return NumRequests;
    }

    public void setNumRequests(int numRequests) {
        NumRequests = numRequests;
    }

    public int getCoins() {
        return Coins;
    }

    public void setCoins(int coins) {
        Coins = coins;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
