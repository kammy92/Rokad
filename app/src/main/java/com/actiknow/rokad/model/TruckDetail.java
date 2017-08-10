package com.actiknow.rokad.model;

public class TruckDetail {
    int id;
    String truck_number, owner_name, owner_mobile, owner_pan_card;
    
    public TruckDetail (int id, String truck_number, String owner_name, String owner_mobile, String owner_pan_card) {
        this.id = id;
        this.truck_number = truck_number;
        this.owner_name = owner_name;
        this.owner_mobile = owner_mobile;
        this.owner_pan_card = owner_pan_card;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getTruck_number () {
        return truck_number;
    }
    
    public void setTruck_number (String truck_number) {
        this.truck_number = truck_number;
    }
    
    public String getOwner_name () {
        return owner_name;
    }
    
    public void setOwner_name (String owner_name) {
        this.owner_name = owner_name;
    }
    
    public String getOwner_mobile () {
        return owner_mobile;
    }
    
    public void setOwner_mobile (String owner_mobile) {
        this.owner_mobile = owner_mobile;
    }
    
    public String getOwner_pan_card () {
        return owner_pan_card;
    }
    
    public void setOwner_pan_card (String owner_pan_card) {
        this.owner_pan_card = owner_pan_card;
    }
}
