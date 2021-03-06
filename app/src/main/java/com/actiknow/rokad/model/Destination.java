package com.actiknow.rokad.model;

public class Destination {
    int id, party_id;
    String name, address;
    double rate;
    
    public Destination (int id, int party_id, String name, String address, double rate) {
        this.id = id;
        this.party_id = party_id;
        this.name = name;
        this.address = address;
        this.rate = rate;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getParty_id () {
        return party_id;
    }
    
    public void setParty_id (int party_id) {
        this.party_id = party_id;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getAddress () {
        return address;
    }
    
    public void setAddress (String address) {
        this.address = address;
    }
    
    public double getRate () {
        return rate;
    }
    
    public void setRate (double rate) {
        this.rate = rate;
    }
}
