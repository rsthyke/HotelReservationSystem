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
