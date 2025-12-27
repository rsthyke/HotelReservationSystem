package com.hotel.model;

public class DeluxeRoom extends Room {
    private boolean hasMiniBar;
    private boolean hasJacuzzi;
    private boolean hasBalcony;
    private double luxuryTax;

    public DeluxeRoom(String roomNumber, int capacity, double basePrice) {
        super(roomNumber, capacity, basePrice);
        this.hasMiniBar = true;
        this.hasJacuzzi = true;
        this.hasBalcony = true;
        this.luxuryTax = 0.20; // 20% extra
    }

    public DeluxeRoom(String roomNumber, int capacity, double basePrice, 
                      boolean hasMiniBar, boolean hasJacuzzi, boolean hasBalcony, double luxuryTax) {
        super(roomNumber, capacity, basePrice);
        this.hasMiniBar = hasMiniBar;
        this.hasJacuzzi = hasJacuzzi;
        this.hasBalcony = hasBalcony;
        this.luxuryTax = luxuryTax;
    }

    @Override
    public double calculatePrice() {
        return getBasePrice() * (1 + luxuryTax);
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
