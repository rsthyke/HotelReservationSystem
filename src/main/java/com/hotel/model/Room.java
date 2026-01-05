package com.hotel.model;

import java.util.ArrayList;
import java.time.LocalDate;

// Abstract class for Room
public abstract class Room implements Reservable {
    private String roomNumber;
    private int capacity;
    private double basePrice;
    private boolean isClean;
    private ArrayList<Reservation> reservations;

    public Room(String roomNumber, int capacity, double basePrice) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.isClean = true;
        this.reservations = new ArrayList<>();
    }

    // Abstract method for polymorphism
    public abstract double calculatePrice(LocalDate date);

    public void addReservation(Reservation res) {
        reservations.add(res);
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }
}
