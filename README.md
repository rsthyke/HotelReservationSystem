# Hotel Reservation System 🏨

A Java-based console application designed to manage hotel room reservations. This project was developed to demonstrate core **Object-Oriented Programming (OOP)** principles as required by the course curriculum.

## ✅ Core Requirements (Implemented)
The following features requested in the project assignment have been fully implemented:

* **OOP Architecture:**
    * **Inheritance:** `StandardRoom` and `DeluxeRoom` classes inherit from the abstract `Room` class.
    * **Polymorphism:** Price calculation logic differs between room types (overridden methods).
    * **Interface:** `Reservable` interface is implemented to enforce booking contracts.
    * **Classes:** `Hotel`, `Customer`, `Reservation`, `Room`, and `Payment` models are included.
* **Essential Features:**
    * Search for available rooms by date.
    * Make a reservation for a specific customer.
    * View booking details and history.

## 🚀 Extra Features (Bonus Implementation)
Beyond the minimum requirements, the following advanced features were added to the system:

* **💾 Data Persistence (File I/O):**
    * The system does not lose data when closed. All records (Rooms, Customers, Reservations) are saved to custom **CSV files** (`DataService`) and loaded automatically on startup.
* **💎 Loyalty Point System:**
    * (Optional Feature Implemented) Customers earn points for every stay and can redeem them for discounts.
* **📅 Dynamic Pricing (Seasonal):**
    * (Optional Feature Implemented) The system automatically calculates higher prices for **Weekends** versus Weekdays.
* **🛡️ Reliability:**
    * **Spam Protection:** Prevents duplicate bookings from the same user instantly.
    * **Validation:** Input checks for logical date ranges (Check-in < Check-out).
* **📊 Admin Dashboard:**
    * View hotel occupancy rates and total revenue statistics.

## 🛠️ Tech Stack
* **Language:** Java (JDK 21)
* **Storage:** Custom CSV File System
* **Testing:** JUnit Integration Tests
