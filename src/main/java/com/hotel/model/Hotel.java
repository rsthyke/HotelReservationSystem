package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;

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
        if (room.isClean()) { // Simple check
            Reservation res = new Reservation(customer, room, checkIn, checkOut);
            reservations.add(res);
            room.addReservation(res);
            customer.addReservation(res);
            System.out.println("Reservation successful! ID: " + res.getReservationId());
        } else {
            System.out.println("Room is not available.");
        }
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
            System.out.println(r.getRoomNumber() + " - " + r.getRoomType() + " - $" + r.calculatePrice());
        }
    }

    // Getters
    public String getName() {return name;}
    public String getAddress() {return address;}
    public ArrayList<Room> getRooms() { return rooms; }
    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Reservation> getReservations() { return reservations; }
    public int getTotalRooms() { return rooms.size(); }
}
