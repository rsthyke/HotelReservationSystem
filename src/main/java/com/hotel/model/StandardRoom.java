package com.hotel.model;

public class StandardRoom extends Room {
    private boolean hasWifi;
    private boolean hasTV;

    public StandardRoom(String roomNumber, int capacity, double basePrice) {
        super(roomNumber, capacity, basePrice);
        this.hasWifi = true;
        this.hasTV = true;
    }

    public StandardRoom(String roomNumber, int capacity, double basePrice, boolean hasWifi, boolean hasTV) {
        super(roomNumber, capacity, basePrice);
        this.hasWifi = hasWifi;
        this.hasTV = hasTV;
    }

    @Override
    public double calculatePrice() {
        return getBasePrice();
    }

    @Override
    public String getRoomType() {
        return "Standard Room";
    }

    public boolean hasWifi() {
        return hasWifi;
    }

    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public boolean hasTV() {
        return hasTV;
    }

    public void setHasTV(boolean hasTV) {
        this.hasTV = hasTV;
    }
}
