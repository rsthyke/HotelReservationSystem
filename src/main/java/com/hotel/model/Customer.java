package com.hotel.model;

import java.util.ArrayList;

public class Customer {
    private static int customerCounter = 0;

    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ArrayList<Reservation> reservationHistory;
    private int loyaltyPoints;

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.customerId = generateCustomerId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservationHistory = new ArrayList<>();
        this.loyaltyPoints = 0;
    }

    private String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public void addReservation(Reservation reservation) {
        reservationHistory.add(reservation);
        // Add 10 points for each reservation
        loyaltyPoints += 10;
    }

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

    // For loading from file
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + " (" + email + ")";
    }
}