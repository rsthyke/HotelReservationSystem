package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;

public class Hotel {
    private String name;
    private String address;
    private ArrayList<Room> rooms;
    private ArrayList<Customer> customers;
    private ArrayList<Reservation> reservations;

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

    public void bookRoom(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (customer.getLastBookingTime() != null &&
                Duration.between(customer.getLastBookingTime(), LocalDateTime.now()).getSeconds() < 10) {
            System.out.println("Fraud alert! You are booking too fast. Please wait a moment.");
            return;
        }

        if (!room.isClean()) {
            System.out.println("Room " + room.getRoomNumber() + " is not available.");

            if (room instanceof StandardRoom) {
                System.out.println("Checking for a Deluxe upgrade...");
                Room deluxeUpgrade = searchAvailableRooms(checkIn, checkOut).stream()
                        .filter(r -> r instanceof DeluxeRoom)
                        .findFirst()
                        .orElse(null);

                if (deluxeUpgrade != null) {
                    System.out.println("Good news! You've been upgraded to a Deluxe Room for the price of a Standard Room!");
                    room = deluxeUpgrade;
                } else {
                    System.out.println("Sorry, no upgrades available.");
                    return;
                }
            } else {
                return;
            }
        }

        Reservation res = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(res);
        room.addReservation(res);
        customer.addReservation(res);
        customer.setLastBookingTime(LocalDateTime.now());
        System.out.println("Reservation successful! ID: " + res.getReservationId() + " in Room: " + room.getRoomNumber());
    }

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
        for (Room r : rooms) {
            System.out.println(r.getRoomNumber() + " - " + r.getRoomType() + " - $" + r.calculatePrice(LocalDate.now()));
        }
    }

    public void displayReservations(String email) {
        System.out.println("\n--- Reservations for " + email + " ---");
        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getCustomer().getEmail().equals(email)) {
                System.out.println(r.getReservationId() + " - Room: " + r.getRoom().getRoomNumber() +
                        " (" + r.getCheckInDate() + " to " + r.getCheckOutDate() + ")");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reservations found for this email.");
        }
    }

    public double calculateRevenue() {
        double total = 0;
        for (Reservation r : reservations) {
            total += r.calculateTotalAmount();
        }
        return total;
    }

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

    // Getters
    public String getName() {return name;}
    public String getAddress() {return address;}
    public ArrayList<Room> getRooms() { return rooms; }
    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Reservation> getReservations() { return reservations; }
    public int getTotalRooms() { return rooms.size(); }
}
