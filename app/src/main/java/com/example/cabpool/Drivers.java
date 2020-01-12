package com.example.cabpool;

public class Drivers {
    private String name,phoneNumber,vehicle;

    public Drivers(String name, String phoneNumber,String vehicle) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
