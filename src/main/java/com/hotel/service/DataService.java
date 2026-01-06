package com.hotel.service;

import com.hotel.model.*;
import com.hotel.util.FileOps;
import java.time.LocalDate;
import java.util.ArrayList;

//This class acts as a bridge between the Hotel logic and the File system.
//It converts objects (Room, Customer) into text (CSV format) and back.
public class DataService {

    // File paths where data will be stored
    private static final String ROOMS_FILE = "data/rooms.csv";
    private static final String CUSTOMERS_FILE = "data/customers.csv";
    private static final String RESERVATIONS_FILE = "data/reservations.csv";

    //Saves all hotel data (Rooms, Customers, Reservations) to files.
    public void saveData(Hotel hotel) {
        saveRooms(hotel.getRooms());
        saveCustomers(hotel.getCustomers());
        saveReservations(hotel.getReservations());
        System.out.println("Data saved.");
    }
    //Loads all data from files into the Hotel object.
    public void loadData(Hotel hotel) {
        loadRooms(hotel);
        loadCustomers(hotel);
        loadReservations(hotel);
        System.out.println("Data loaded.");
    }

    private void saveRooms(ArrayList<Room> rooms) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Type,RoomNumber,Capacity,BasePrice,Extras");

        for (Room room : rooms) {
            String line = "";
            if (room instanceof StandardRoom std) {
                line = "STANDARD," + room.getRoomNumber() + "," + room.getCapacity() + "," +
                       room.getBasePrice() + "," + std.hasWifi() + ";" + std.hasTV();
            } else if (room instanceof DeluxeRoom dlx) {
                line = "DELUXE," + room.getRoomNumber() + "," + room.getCapacity() + "," +
                       room.getBasePrice() + "," + dlx.hasMiniBar() + ";" + dlx.hasJacuzzi() + ";" +
                       dlx.hasBalcony() + ";" + dlx.getLuxuryTax();
            }
            lines.add(line);
        }
        FileOps.writeFile(ROOMS_FILE, lines);
    }

    private void loadRooms(Hotel hotel) {
        ArrayList<String> lines = FileOps.readFile(ROOMS_FILE);
        for (int i = 1; i < lines.size(); i++) { // Skip header

            // Skip corrupted lines to prevent system crash
            try {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 4) continue;// Skip if data is missing

                String type = parts[0];
                String number = parts[1];
                int capacity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);

                if (type.equals("STANDARD")) {
                    String[] extras = parts[4].split(";");
                    boolean wifi = Boolean.parseBoolean(extras[0]);
                    boolean tv = Boolean.parseBoolean(extras[1]);
                    hotel.addRoom(new StandardRoom(number, capacity, price, wifi, tv));
                } else if (type.equals("DELUXE")) {
                    String[] extras = parts[4].split(";");
                    boolean minibar = Boolean.parseBoolean(extras[0]);
                    boolean jacuzzi = Boolean.parseBoolean(extras[1]);
                    boolean balcony = Boolean.parseBoolean(extras[2]);
                    double tax = Double.parseDouble(extras[3]);
                    hotel.addRoom(new DeluxeRoom(number, capacity, price, minibar, jacuzzi, balcony, tax));
                }
            } catch (Exception e) {
                System.out.println("Skipping corrupted room data at line: " + (i + 1));
            }
        }
    }

    private void saveCustomers(ArrayList<Customer> customers) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("ID,FirstName,LastName,Email,Phone,Points");

        for (Customer c : customers) {
            lines.add(c.getCustomerId() + "," + c.getFirstName() + "," + c.getLastName() + "," +
                    c.getEmail() + "," + c.getPhoneNumber() + "," + c.getLoyaltyPoints());
        }
        FileOps.writeFile(CUSTOMERS_FILE, lines);
    }

    private void loadCustomers(Hotel hotel) {
        ArrayList<String> lines = FileOps.readFile(CUSTOMERS_FILE);
        for (int i = 1; i < lines.size(); i++) {

            // Skip corrupted lines to prevent system crash
            try {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 6) continue;

                // Create customer and restore points
                Customer c = new Customer(parts[1], parts[2], parts[3], parts[4]);
                c.addLoyaltyPoints(Integer.parseInt(parts[5]));
                hotel.registerCustomer(c);
            } catch (Exception e) {
                System.out.println("Skipping corrupted customer data at line: " + (i + 1));
            }
        }
    }

    private void saveReservations(ArrayList<Reservation> reservations) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("ID,CustomerEmail,RoomNumber,CheckIn,CheckOut,Status");

        // We save Email and RoomNumber to link them back later
        for (Reservation r : reservations) {
            lines.add(r.getReservationId() + "," + r.getCustomer().getEmail() + "," +
                      r.getRoom().getRoomNumber() + "," + r.getCheckInDate() + "," +
                      r.getCheckOutDate() + "," + r.getStatus());
        }
        FileOps.writeFile(RESERVATIONS_FILE, lines);
    }

    private void loadReservations(Hotel hotel) {
        ArrayList<String> lines = FileOps.readFile(RESERVATIONS_FILE);
        for (int i = 1; i < lines.size(); i++) {

            // Skip corrupted lines to prevent system crash
            try {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 6) continue;
                // We need to find the real objects using Email and Room Number
                Customer customer = hotel.findCustomerByEmail(parts[1]);
                Room room = hotel.findRoom(parts[2]);

                if (customer != null && room != null) {
                    Reservation res = new Reservation(customer, room, LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
                    res.setStatus(parts[5]);

                    // Link the reservation to everything
                    hotel.getReservations().add(res);
                    room.addReservation(res);
                    customer.addReservation(res);
                }
            } catch (Exception e) {
                System.out.println("Skipping corrupted reservation data at line: " + (i + 1));
            }
        }
    }
}
