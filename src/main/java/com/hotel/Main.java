package com.hotel;

import com.hotel.model.*;
import com.hotel.service.DataService;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Ensure data directory exists to prevent IO errors
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
            System.out.println("System: 'data' directory created.");
        }

        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel("Grand Hotel", "123 Main St");
        DataService dataService = new DataService();

        // Load existing data from CSV files
        dataService.loadData(hotel);

        // If no rooms loaded, add some defaults
        if (hotel.getTotalRooms() == 0) {
            // StandardRoom: (Num, Cap, Price, Wifi, TV)
            hotel.addRoom(new StandardRoom("101", 2, 100.0, true, true));
            hotel.addRoom(new StandardRoom("102", 2, 100.0, true, true));
            hotel.addRoom(new StandardRoom("103", 2, 100.0, true, true));
            hotel.addRoom(new StandardRoom("104", 2, 100.0, true, true));
            // DeluxeRoom: (Num, Cap, Price, Minibar, Jacuzzi, Balcony, Tax)
            hotel.addRoom(new DeluxeRoom("201", 4, 200.0, true, true, true, 20.0));
            hotel.addRoom(new DeluxeRoom("202", 4, 200.0, true, true, true, 20.0));
            hotel.addRoom(new DeluxeRoom("203", 4, 200.0, true, true, true, 20.0));
        }

        while (true) {
            System.out.println("\n--- HOTEL SYSTEM ---");
            System.out.println("1. List Rooms");
            System.out.println("2. Register Customer");
            System.out.println("3. Make Reservation");
            System.out.println("4. Save & Exit");
            System.out.print("Choice: ");

            int choice = 0;
            // Prevent crash if user types text instead of number (Fixed Crash Bug)
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 1) {
                hotel.displayAllRooms();
            } else if (choice == 2) {
                System.out.print("First Name: ");
                String fn = scanner.nextLine();
                System.out.print("Last Name: ");
                String ln = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Phone: ");
                String phone = scanner.nextLine();

                hotel.registerCustomer(new Customer(fn, ln, email, phone));
                System.out.println("Customer registered!");
            } else if (choice == 3) {
                System.out.print("Customer Email: ");
                String email = scanner.nextLine();
                Customer c = hotel.findCustomerByEmail(email);

                if (c == null) {
                    System.out.println("Customer not found!");
                    continue;
                }

                System.out.print("Room Number: ");
                String roomNum = scanner.nextLine();
                Room r = hotel.findRoom(roomNum);

                if (r == null) {
                    System.out.println("Room not found!");
                    continue;
                }

                // Loop until valid date format is entered (Fixed Crash Bug)
                System.out.print("Check-in (YYYY-MM-DD): ");
                LocalDate in = null;
                while (in == null) {
                    try {
                        in = LocalDate.parse(scanner.nextLine());
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
                    }
                }

                System.out.print("Check-out (YYYY-MM-DD): ");
                LocalDate out = null;
                while (out == null) {
                    try {
                        out = LocalDate.parse(scanner.nextLine());
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
                    }
                }

                hotel.bookRoom(c, r, in, out);
            } else if (choice == 4) {
                dataService.saveData(hotel);
                System.out.println("Goodbye!");
                break;
            }
        }
        scanner.close();
    }
}