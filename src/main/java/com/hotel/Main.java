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
        Hotel hotel = new Hotel("Ocean View Hotel", "1020 Ocean Drive, Vice City");
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
            hotel.addRoom(new DeluxeRoom("201", 4, 200.0, true, true, true, 0.20));
            hotel.addRoom(new DeluxeRoom("202", 4, 200.0, true, true, true, 0.20));
            hotel.addRoom(new DeluxeRoom("203", 4, 200.0, true, true, true, 0.20));
        }

        while (true) {
            System.out.println("\n========================================");
            System.out.println("           OCEAN VIEV HOTEL");
            System.out.println("========================================");
            System.out.println("  [1] List All Rooms");
            System.out.println("  [2] Register New Customer");
            System.out.println("  [3] Make a Reservation");
            System.out.println("  [4] Get Room Recommendation");
            System.out.println("  [5] View My Reservations");
            System.out.println("  [6] Save Data & Exit");
            System.out.println("========================================");
            System.out.print(">> Enter your choice: ");

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
                String email;
                while (true) {
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    if (email.contains("@") && email.contains(".")) {
                        break;
                    }
                    System.out.println("Error: Invalid email! Please try again.");
                }
                String phone;
                while (true) {
                    System.out.print("Phone: ");
                    phone = scanner.nextLine();
                    if (phone.matches("[0-9]+")) {
                        break;
                    }
                    System.out.println("Error: Phone number must contain only digits! Please try again.");
                }
                Customer newCustomer = new Customer(fn, ln, email, phone);
                hotel.registerCustomer(newCustomer);
                System.out.println("Customer registered! Your ID is: " + newCustomer.getCustomerId());
            } else if (choice == 3) {
                System.out.print("Customer Email: ");
                String email = scanner.nextLine();
                Customer c = hotel.findCustomerByEmail(email);

                if (c == null) {
                    System.out.println("Customer not found!");
                    continue;
                }

                Room selectedRoom = null;
                while (selectedRoom == null) {
                    System.out.print("Room Number: ");
                    String roomNum = scanner.nextLine();
                    selectedRoom = hotel.findRoom(roomNum);
                    if (selectedRoom == null) {
                        System.out.println("Error: Room not found! Please try again.");
                    }
                }

                // Loop until valid date format is entered (Fixed Crash Bug)
                System.out.print("Check-in (YYYY-MM-DD): ");
                LocalDate in = null;
                while (in == null) {
                    try {
                        in = LocalDate.parse(scanner.nextLine());
                        if (in.isBefore(LocalDate.now())) {
                            System.out.print("Error: Date cannot be in the past! Try again: ");
                            in = null;
                        }
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
                    }
                }

                System.out.print("Check-out (YYYY-MM-DD): ");
                LocalDate out = null;
                while (out == null) {
                    try {
                        out = LocalDate.parse(scanner.nextLine());
                        if (out.isBefore(in) || out.equals(in)) {
                            System.out.print("Error: Check-out must be after Check-in! Try again: ");
                            out = null;
                        }
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
                    }
                }

                boolean usePoints = false;
                if (c.getLoyaltyPoints() > 0) {
                    System.out.println("You have " + c.getLoyaltyPoints() + " Loyalty Points.");
                    System.out.print("Do you want to use your points for a discount? (Y/N): ");
                    String response = scanner.nextLine().trim().toUpperCase();
                    if (response.equals("Y")) {
                        usePoints = true;
                    }
                }

                hotel.printInvoicePreview(c, selectedRoom, in, out, usePoints);
                System.out.print("Confirm Payment and Reservation? (Y/N): ");
                String confirm = scanner.nextLine().trim().toUpperCase();
                if (confirm.equals("Y")) {
                    hotel.bookRoom(c, selectedRoom, in, out, usePoints);
                } else {
                    System.out.println("Transaction cancelled by user.");
                }

            } else if (choice == 4) {
                System.out.print("Enter Customer Email for Recommendation: ");
                String email = scanner.nextLine();
                hotel.recommendRoom(email);

            } else if (choice == 5) {
                System.out.print("Enter your email: ");
                String email = scanner.nextLine();
                hotel.displayReservations(email);

            } else if (choice == 6) {
                dataService.saveData(hotel);
                System.out.println("Goodbye!");
                break;

            } else if (choice == 66) {
                System.out.print("Enter Admin Password: ");
                String pass = scanner.nextLine();
                if (pass.equals("yozgat66")) {
                    System.out.println("\n*** ADMIN PANEL ***");
                    System.out.println("Total Revenue: $" + hotel.calculateRevenue());
                    System.out.println("Occupancy Rate: " + String.format("%.1f", hotel.calculateOccupancyRate()) + "%");
                    hotel.displayMostPopularRoom();
                    hotel.listVIPCustomers();
                    hotel.listAllCustomers();
                    System.out.println("*******************");
                } else {
                    System.out.println("Access Denied!");
                }
            }

        }
        scanner.close();
    }
}