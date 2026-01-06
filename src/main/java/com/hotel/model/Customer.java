package com.hotel.model;

import java.util.ArrayList;
import java.time.LocalDateTime;

//Represents a customer of the hotel.
//Stores personal details and reservation history.
public class Customer {
    private static int customerCounter = 0;

    private final String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private final ArrayList<Reservation> reservationHistory;
    private int loyaltyPoints;
    private LocalDateTime lastBookingTime;

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.customerId = generateCustomerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservationHistory = new ArrayList<>();
        this.loyaltyPoints = 0;
    }
    //Generates a unique ID like "CUST1", "CUST2"
    private String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }
    //Adds a reservation to the customer's history.
    public void addReservation(Reservation reservation) {
        reservationHistory.add(reservation);
    }

    /**
     * Uses loyalty points for a discount.
     * @param points Points to use.
     * @return true if successful, false if not enough points.
     */
    public boolean redeemLoyaltyPoints(int points) {
        if (loyaltyPoints >= points) {
            loyaltyPoints -= points;
            return true;
        }
        return false;
    }

    // Getters and Setters

    public String getCustomerId() {
        return customerId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public ArrayList<Reservation> getReservationHistory() {
        return reservationHistory;
    }
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    public LocalDateTime getLastBookingTime() {return lastBookingTime;}
    public void setLastBookingTime(LocalDateTime lastBookingTime) {this.lastBookingTime = lastBookingTime;}

    // For loading from file
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + " (" + email + ")";
    }
}