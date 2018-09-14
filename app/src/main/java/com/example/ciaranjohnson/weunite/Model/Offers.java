package com.example.ciaranjohnson.weunite.Model;


public class Offers {
    private String offersID;
    private String username;

    public Offers(String offersID, String username){
        this.offersID = offersID;
        this.username = username;
    }

    public Offers() {
    }

    public String getOffersID() {
        return offersID;
    }

    public void setOffersID(String offersID) {
        this.offersID = offersID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
