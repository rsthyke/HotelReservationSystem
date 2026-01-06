package com.hotel.model;

import java.util.ArrayList;
import java.time.LocalDate;

 //Abstract class representing a general Room in the hotel.
 //It holds common attributes like room number, capacity, and base price.

public abstract class Room implements Reservable {
    private String roomNumber;
    private int capacity;
    private double basePrice;
    private boolean isClean;
    private final ArrayList<Reservation> reservations;

    /**
     * Constructor to initialize a Room.
     * @param roomNumber The unique number of the room.
     * @param capacity How many people can sleep here.
     * @param basePrice The starting price of the room.
     * Rooms are clean by default when created
     */

    public Room(String roomNumber, int capacity, double basePrice) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.isClean = true;
        this.reservations = new ArrayList<>();
    }

    // Abstract method: Child classes (Standard, Deluxe) must implement their own pricing logic.
    public abstract double calculatePrice(LocalDate date);

    /**
     * Adds a new reservation to this room's history.
     * @param res The reservation object.
     */
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
