package com.hotel.model;

import java.time.LocalDate;

//Represents a Deluxe Room.
//It has extra luxury features and a luxury tax.
public class DeluxeRoom extends Room {
    private boolean hasMiniBar;
    private boolean hasJacuzzi;
    private boolean hasBalcony;
    private double luxuryTax;

    /**
     * Default constructor for Deluxe Room.
     * Includes all amenities and 20% luxury tax.
     */
    public DeluxeRoom(String roomNumber, int capacity, double basePrice) {
        super(roomNumber, capacity, basePrice);
        this.hasMiniBar = true;
        this.hasJacuzzi = true;
        this.hasBalcony = true;
        this.luxuryTax = 0.20; // 20% extra
    }

    //Custom constructor to set specific features.
    public DeluxeRoom(String roomNumber, int capacity, double basePrice, 
                      boolean hasMiniBar, boolean hasJacuzzi, boolean hasBalcony, double luxuryTax) {
        super(roomNumber, capacity, basePrice);
        this.hasMiniBar = hasMiniBar;
        this.hasJacuzzi = hasJacuzzi;
        this.hasBalcony = hasBalcony;
        this.luxuryTax = luxuryTax;
    }

    /**
     * Calculates price for Deluxe Room
     * Adds luxury tax and increases price by 30% on weekends.
     */
    @Override
    public double calculatePrice(LocalDate date) {
        double currentPrice = getBasePrice() * (1 + luxuryTax);// First apply the luxury tax to the base price
        if (date.getDayOfWeek().getValue() >= 5) {
            currentPrice *= 1.30;
        }
        return currentPrice;
    }

    @Override
    public String getRoomType() {
        return "Deluxe Room";
    }

    // Getters and Setters
    public boolean hasMiniBar() { return hasMiniBar; }
    public void setHasMiniBar(boolean hasMiniBar) { this.hasMiniBar = hasMiniBar; }
    
    public boolean hasJacuzzi() { return hasJacuzzi; }
    public void setHasJacuzzi(boolean hasJacuzzi) { this.hasJacuzzi = hasJacuzzi; }
    
    public boolean hasBalcony() { return hasBalcony; }
    public void setHasBalcony(boolean hasBalcony) { this.hasBalcony = hasBalcony; }
    
    public double getLuxuryTax() { return luxuryTax; }
    public void setLuxuryTax(double luxuryTax) { this.luxuryTax = luxuryTax; }
}
