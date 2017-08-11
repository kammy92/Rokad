package com.actiknow.rokad.model;

public class Destination {
    int id, party_id;
    String name, address;
    
    public Destination (int id, int party_id, String name, String address) {
        this.id = id;
        this.party_id = party_id;
        this.name = name;
        this.address = address;
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
}
