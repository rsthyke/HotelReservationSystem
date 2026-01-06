package com.hotel.model;

import java.time.LocalDate;

 //Represents a Standard Room.
 //It has basic features like Wifi and TV.

public class StandardRoom extends Room {
    private boolean hasWifi;
    private boolean hasTV;

    //Constructor with default amenities (Wifi and TV are true).
    public StandardRoom(String roomNumber, int capacity, double basePrice) {
        super(roomNumber, capacity, basePrice);
        this.hasWifi = true;
        this.hasTV = true;
    }

    //Constructor where we can choose amenities.
    public StandardRoom(String roomNumber, int capacity, double basePrice, boolean hasWifi, boolean hasTV) {
        super(roomNumber, capacity, basePrice);
        this.hasWifi = hasWifi;
        this.hasTV = hasTV;
    }

    /**
     * Calculates price for Standard Room.
     * If it is the weekend (Friday, Saturday, Sunday), price increases by 20%.
     */
    @Override
    public double calculatePrice(LocalDate date) {
        double currentPrice = getBasePrice();
        if (date.getDayOfWeek().getValue() >= 5) {// 5 = Friday, 6 = Saturday, 7 = Sunday
            currentPrice *= 1.20;
        }
        return currentPrice;
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
