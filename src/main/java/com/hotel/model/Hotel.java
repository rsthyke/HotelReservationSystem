package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;

//The main manager class for the Hotel system.
//It manages rooms, customers, and reservations.
public class Hotel {
    private final String name;
    private final String address;
    private final ArrayList<Room> rooms;
    private final ArrayList<Customer> customers;
    private final ArrayList<Reservation> reservations;

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void registerCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Handles the booking process.
     * Includes fraud check, discounts, upgrades, and point calculation.
     */
    public void bookRoom(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut, boolean usePoints, boolean isFreeUpgrade) {
        //Security Check: Prevent booking too fast (within 60 seconds)
        if (customer.getLastBookingTime() != null &&
                Duration.between(customer.getLastBookingTime(), LocalDateTime.now()).getSeconds() < 60) {
            System.out.println("Fraud alert! You are booking too fast. Please wait a moment.");
            return;
        }
        //Handle Upgrades: If upgraded, use Standard Room pricing temporarily
        Room pricingRoom = room;
        if (isFreeUpgrade) {
            pricingRoom = new StandardRoom("TEMP", 2, 100.0, true, true);
        }
        //Create a temporary reservation to calculate cost
        Reservation res = new Reservation(customer, pricingRoom, checkIn, checkOut);
        double totalAmount = res.calculateTotalAmount();
        //Handle Loyalty Points (Discount)
        double discount = 0;
        if (usePoints) {
            int points = customer.getLoyaltyPoints();
            if (points > 0) {// 10 points = $1 discount
                double maxDiscount = points / 10.0;
                if (maxDiscount >= totalAmount) {
                    discount = totalAmount;// Full cover
                    int pointsUsed = (int)(totalAmount * 10);
                    customer.redeemLoyaltyPoints(pointsUsed);
                    System.out.println("Loyalty Points Used: " + pointsUsed + " (-$" + totalAmount + ")");
                } else {
                    discount = maxDiscount;
                    customer.redeemLoyaltyPoints(points);
                    System.out.println("Loyalty Points Used: " + points + " (-$" + discount + ")");
                }
            } else {
                System.out.println("No loyalty points available to use.");
            }
        }
        //Calculate Final Price
        double finalPrice = totalAmount - discount;
        System.out.println("Total Price: $" + totalAmount);
        if (discount > 0) {
            System.out.println("Discount Applied: -$" + discount);
            System.out.println("Final Price to Pay: $" + finalPrice);
        }
        //Earn new points (5% of payment)
        int pointsEarned = (int)(finalPrice * 0.05);
        customer.addLoyaltyPoints(pointsEarned);
        res.setRoom(room);
        reservations.add(res);
        room.addReservation(res);
        customer.addReservation(res);

        room.setClean(false);
        customer.setLastBookingTime(LocalDateTime.now());

        System.out.println("Reservation successful! ID: " + res.getReservationId() + " in Room: " + room.getRoomNumber());
        System.out.println("You earned " + pointsEarned + " Loyalty Points! Total Points: " + customer.getLoyaltyPoints());
    }

    /**
     * Finds rooms that are clean.
     * In a real app, this should also check dates.
     */
    public ArrayList<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            // Simplified availability logic: just check if room is clean for now
            if (room.isClean()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Customer findCustomerByEmail(String email) {
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }

    public Room findRoom(String roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNumber)) {
                return r;
            }
        }
        return null;
    }

    public void displayInfo() {
        System.out.println("Hotel: " + name);
        System.out.println("Address: " + address);
        System.out.println("Total Rooms: " + rooms.size());
    }

    public void displayAllRooms() {
        System.out.println("\n-------------------------------------------------------------");
        System.out.printf("%-10s | %-15s | %-10s | %-10s%n", "Room No", "Type", "Price", "Status");
        System.out.println("-------------------------------------------------------------");
        for (Room r : rooms) {
            String status = r.isClean() ? "Available" : "Occupied";
            System.out.printf("%-10s | %-15s | $%-9.2f | %-10s%n",
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.calculatePrice(LocalDate.now()),
                    status);
        }
        System.out.println("-------------------------------------------------------------");
    }

    public void displayReservations(String email) {
        System.out.println("\n--- Reservations for " + email + " ---");
        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getCustomer().getEmail().equals(email)) {
                System.out.println(r.getReservationId() + " - Room: " + r.getRoom().getRoomNumber() +
                        " (" + r.getCheckInDate() + " to " + r.getCheckOutDate() + ")" + " | Total: $" + r.calculateTotalAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reservations found for this email.");
        }
    }
    //Calculates total money earned from all reservations.
    public double calculateRevenue() {
        double total = 0;
        for (Reservation r : reservations) {
            total += r.calculateTotalAmount();
        }
        return total;
    }
    //Finds the room that has been booked the most.
    public void displayMostPopularRoom() {
        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Room popular = null;
        int maxCount = -1;

        for (Room r : rooms) {
            int count = 0;
            for (Reservation res : reservations) {
                if (res.getRoom().getRoomNumber().equals(r.getRoomNumber())) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                popular = r;
            }
        }

        if (popular != null) {
            System.out.println("Most Popular Room: " + popular.getRoomNumber() + " (" + maxCount + " bookings)");
        }
    }

    public void listAllCustomers() {
        System.out.println("\n--- All Customers ---");
        for (Customer c : customers) {
            System.out.println("ID: " + c.getCustomerId() + " | " + c.getFirstName() + " " + c.getLastName() + " | " + c.getEmail());
        }
    }
    //Calculates the percentage of rooms occupied right now.
    public double calculateOccupancyRate() {
        if (rooms.isEmpty()) return 0.0;
        int occupiedCount = 0;
        LocalDate today = LocalDate.now();

        for (Room room : rooms) {
            boolean isOccupied = false;
            for (Reservation res : reservations) {
                if (res.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                    if (!today.isBefore(res.getCheckInDate()) && !today.isAfter(res.getCheckOutDate())) {
                        isOccupied = true;
                        break;
                    }
                }
            }
            if (isOccupied) occupiedCount++;
        }
        return (double) occupiedCount / rooms.size() * 100;
    }
    // Lists customers with more than 3 bookings
    public void listVIPCustomers() {
        System.out.println("\n--- VIP Customers (3+ Bookings) ---");
        boolean found = false;
        for (Customer c : customers) {
            if (c.getReservationHistory().size() >= 3) {
                System.out.println("VIP: " + c.getFirstName() + " " + c.getLastName() + " (" + c.getReservationHistory().size() + " bookings)");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No VIP customers yet.");
        }
    }
    /**
     * Smart Feature: Recommends a room based on customer history.
     * If they usually book Deluxe, suggest Deluxe.
     */
    public Room recommendRoom(String email) {
        Customer customer = null;

        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                customer = c;
                break;
            }
        }

        if (customer == null) {
            System.out.println("Customer not found!");
            return null;
        }

        System.out.println("Analyzing history for: " + customer.getFirstName());

        int deluxeCount = 0;
        int standardCount = 0;

        for (Reservation res : customer.getReservationHistory()) {
            if (res.getRoom() instanceof DeluxeRoom) {
                deluxeCount++;
            } else {
                standardCount++;
            }
        }

        System.out.println("Stats: " + deluxeCount + " Deluxe bookings vs " + standardCount + " Standard bookings.");

        if (deluxeCount > standardCount) {
            for (Room r : rooms) {
                if (r instanceof DeluxeRoom && r.isClean()) {
                    System.out.println(">>> Based on your history, we recommend Deluxe Room: " + r.getRoomNumber());
                    return r;
                }
            }
        }

        for (Room r : rooms) {
            if (r instanceof StandardRoom && r.isClean()) {
                System.out.println(">>> We recommend Standard Room: " + r.getRoomNumber());
                return r;
            }
        }

        System.out.println("Sorry, no suitable room found.");
        return null;
    }
    //Previews the invoice (bill) without saving the reservation.
    public void printInvoicePreview(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut, boolean usePoints, boolean isFreeUpgrade) {

        Room pricingRoom = room;
        if (isFreeUpgrade) {
            pricingRoom = new StandardRoom("TEMP", 2, 100.0, true, true);
        }

        Reservation tempRes = new Reservation(customer, pricingRoom, checkIn, checkOut);
        double totalAmount = tempRes.calculateTotalAmount();

        double discount = 0;
        if (usePoints && customer.getLoyaltyPoints() > 0) {
            double maxDiscount = customer.getLoyaltyPoints() / 10.0;
            if (maxDiscount >= totalAmount) {
                discount = totalAmount;
            } else {
                discount = maxDiscount;
            }
        }

        double finalPrice = totalAmount - discount;

        System.out.println("\n========================================");
        System.out.println("         PAYMENT CONFIRMATION");
        System.out.println("========================================");
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Room: " + room.getRoomNumber() + " (" + room.getRoomType() + ")");
        if(isFreeUpgrade) {
            System.out.println("*** FREE UPGRADE APPLIED (Standard Price) ***");
        }
        System.out.println("Check-in:  " + checkIn);
        System.out.println("Check-out: " + checkOut);
        System.out.println("----------------------------------------");
        System.out.printf("Total Amount:      $%.2f%n", totalAmount);
        if (discount > 0) {
            System.out.printf("Loyalty Discount: -$%.2f%n", discount);
        }
        System.out.printf("FINAL TO PAY:      $%.2f%n", finalPrice);
        System.out.println("========================================");
    }

    public Room findAvailableDeluxeRoom() {
        for (Room r : rooms) {
            if (r instanceof DeluxeRoom && r.isClean()) {
                return r;
            }
        }
        return null;
    }

    // Getters
    public String getName() {return name;}
    public String getAddress() {return address;}
    public ArrayList<Room> getRooms() { return rooms; }
    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Reservation> getReservations() { return reservations; }
    public int getTotalRooms() { return rooms.size(); }
}
