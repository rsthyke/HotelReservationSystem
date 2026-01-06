package com.hotel.model;

import java.time.LocalDate;

public class Reservation {
    private static int reservationCounter = 0;

    private final String reservationId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status; // PENDING, CONFIRMED, CANCELLED

    public Reservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = generateId();
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "PENDING";
    }

    private String generateId() {
        reservationCounter++;
        return "RES" + reservationCounter;
    }

    public double calculateTotalAmount() {
        double totalAmount = 0;
        LocalDate date = checkInDate;
        while (date.isBefore(checkOutDate)) {
            totalAmount += room.calculatePrice(date);
            date = date.plusDays(1);
        }
        return totalAmount;
    }

    // Getters and Setters
    public String getReservationId() { return reservationId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void displayInfo() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Dates: " + checkInDate + " to " + checkOutDate);
        System.out.println("Total: $" + calculateTotalAmount());
        System.out.println("Status: " + status);
    }
}