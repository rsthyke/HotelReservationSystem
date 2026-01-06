package com.hotel.test;

import com.hotel.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class HotelTest {

    private Hotel hotel;
    private Customer customer;
    private StandardRoom stdRoom;
    private DeluxeRoom dlxRoom;

    @Before
    public void setUp() {
        // Setup fresh environment before each test
        hotel = new Hotel("Test Hotel", "Istanbul");
        customer = new Customer("Ali", "Veli", "ali@test.com", "1234567890");
        hotel.registerCustomer(customer);

        stdRoom = new StandardRoom("101", 2, 100.0, true, true);
        dlxRoom = new DeluxeRoom("201", 4, 200.0, true, true, true, 0.20);

        hotel.addRoom(stdRoom);
        hotel.addRoom(dlxRoom);
    }

    @Test
    public void StandardRoomCreation() {
        assertNotNull(stdRoom);
        assertEquals("101", stdRoom.getRoomNumber());
        assertEquals("Standard Room", stdRoom.getRoomType());
    }

    @Test
    public void DeluxeRoomCreation() {
        assertNotNull(dlxRoom);
        assertEquals("201", dlxRoom.getRoomNumber());
        assertEquals("Deluxe Room", dlxRoom.getRoomType());
    }

    @Test
    public void RoomAvailability() {
        assertTrue("New room should be clean/available", stdRoom.isClean());
    }

    @Test
    public void StandardRoomExtras() {
        // Check if Standard Room has Wifi and TV
        assertTrue(stdRoom.hasWifi());
        assertTrue(stdRoom.hasTV());
    }

    @Test
    public void DeluxeRoomExtras() {
        // Check if Deluxe Room has Jacuzzi
        assertTrue(dlxRoom.hasJacuzzi());
    }

    @Test
    public void StandardRoomPriceWeekday() {
        LocalDate weekday = LocalDate.of(2026, 1, 6); // Wednesday
        assertEquals(100.0, stdRoom.calculatePrice(weekday), 0.01);
    }

    @Test
    public void StandardRoomPriceWeekend() {
        LocalDate weekend = LocalDate.of(2026, 1, 10); // Saturday
        // Weekend 20% increase: 100 * 1.20 = 120
        assertEquals(120.0, stdRoom.calculatePrice(weekend), 0.01);
    }

    @Test
    public void DeluxeRoomPriceWeekday() {
        LocalDate weekday = LocalDate.of(2026, 1, 6); // Wednesday
        //200 + Tax: 20 = 220
        assertEquals(240.0, dlxRoom.calculatePrice(weekday), 0.01);
    }

    @Test
    public void DeluxeRoomPriceWeekend() {
        LocalDate weekend = LocalDate.of(2026, 1, 10); // Saturday
        //200 * 1.30 (Weekend) = 260 + Tax: 20 = 280
        assertEquals(312, dlxRoom.calculatePrice(weekend), 0.01);
    }

    @Test
    public void CustomerCreation() {
        assertEquals("Ali", customer.getFirstName());
        assertEquals("ali@test.com", customer.getEmail());
    }

    @Test
    public void InitialLoyaltyPoints() {
        assertEquals("Initial points should be 0", 0, customer.getLoyaltyPoints());
    }

    @Test
    public void CustomerFullName() {
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        assertEquals("Ali Veli", fullName);
    }

    @Test
    public void MakeReservation() {
        LocalDate in = LocalDate.now().plusDays(1);
        LocalDate out = LocalDate.now().plusDays(2);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        assertEquals(1, customer.getReservationHistory().size());
        assertFalse("Room should be occupied after booking", stdRoom.isClean());
    }

    @Test
    public void ReservationTotalAmount() {
        LocalDate in = LocalDate.of(2026, 1, 6); // Wednesday
        LocalDate out = LocalDate.of(2026, 1, 8); // Friday (2 Nights)
        // 2 Nights * 100 = 200

        Reservation res = new Reservation(customer, stdRoom, in, out);
        assertEquals(200.0, res.calculateTotalAmount(), 0.01);
    }

    @Test
    public void InvalidDateRange() {
        LocalDate in = LocalDate.of(2026, 10, 6);
        LocalDate out = LocalDate.of(2026, 10, 4);

        Reservation res = new Reservation(customer, stdRoom, in, out);
        // If check-in is after check-out, the loop doesn't run, so the price should be 0.
        assertEquals(0.0, res.calculateTotalAmount(), 0.01);
    }

    @Test
    public void EarnLoyaltyPoints() {
        LocalDate in = LocalDate.of(2026, 1, 6);
        LocalDate out = LocalDate.of(2026, 1, 7);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        // 5% of 100 = 5 Points
        assertEquals(5, customer.getLoyaltyPoints());
    }

    @Test
    public void RedeemLoyaltyPoints() {
        customer.addLoyaltyPoints(100); // 100 Points = 10$ Discount

        LocalDate in = LocalDate.of(2026, 1, 6);
        LocalDate out = LocalDate.of(2026, 1, 7);
        hotel.bookRoom(customer, stdRoom, in, out, true, false);
        // Pay 100 - 10 = 90$
        // Earn 5% of 90 = 4.5 -> 4 Points
        // Remaining 0 + 4 = 4 Points
        assertEquals(4, customer.getLoyaltyPoints());
    }

    @Test
    public void PartialPointRedemption() {
        // Customer has sufficient points to cover the entire amount
        customer.addLoyaltyPoints(1000);

        LocalDate in = LocalDate.of(2026, 1, 6);
        LocalDate out = LocalDate.of(2026, 1, 7);
        // Points cover the entire bill ($100), so remaining points should be 0.
        hotel.bookRoom(customer, stdRoom, in, out, true, false);

        // Earn: 5% of 0 (Free stay) = 0 Points.
        assertEquals(0, customer.getLoyaltyPoints());
    }

    @Test
    public void LowAmountNoPoints() {
        // Test point calculation for low amounts where points earned would be less than 1.
        // Example: 10$ * 0.05 = 0.5 -> Should round down to 0 points.

        int points = (int)(10.0 * 0.05);
        assertEquals(0, points);
    }

    @Test
    public void NotEnoughPoints() {
        customer.addLoyaltyPoints(0);
        boolean result = customer.redeemLoyaltyPoints(10);
        assertFalse("Should fail if not enough points", result);
    }

    @Test
    public void FreeUpgrade() {
        LocalDate in = LocalDate.of(2026, 1, 6);
        LocalDate out = LocalDate.of(2026, 1, 7);
        // Book Deluxe with Free Upgrade
        hotel.bookRoom(customer, dlxRoom, in, out, false, true);
        Reservation res = customer.getReservationHistory().getFirst();
        assertTrue("Room should be Deluxe", res.getRoom() instanceof DeluxeRoom);
    }

    @Test
    public void VIPCustomerStatus() {
        // Make 3 reservations
        LocalDate in = LocalDate.now();
        LocalDate out = LocalDate.now().plusDays(1);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        customer.setLastBookingTime(null); // Bypass spam check
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        customer.setLastBookingTime(null);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        assertTrue("Customer should be VIP (3+ bookings)", customer.getReservationHistory().size() >= 3);
    }

    @Test
    public void OccupancyRate() {
        // 2 rooms total. Book 1.
        LocalDate in = LocalDate.now();
        LocalDate out = LocalDate.now().plusDays(1);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        assertEquals(50.0, hotel.calculateOccupancyRate(), 0.01);
    }

    @Test
    public void RevenueCalculation() {
        LocalDate in = LocalDate.of(2026, 1, 6);
        LocalDate out = LocalDate.of(2026, 1, 7);
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        assertEquals(100.0, hotel.calculateRevenue(), 0.01);
    }

    @Test
    public void SearchAvailableRooms() {
        // stdRoom is booked (dirty), dlxRoom is free (clean)
        stdRoom.setClean(false);
        ArrayList<Room> available = hotel.searchAvailableRooms(LocalDate.now(), LocalDate.now().plusDays(1));
        // Should find only dlxRoom
        assertEquals(1, available.size());
        assertEquals("201", available.getFirst().getRoomNumber());
    }

    @Test
    public void RoomRecommendation() {
        // Customer books Deluxe 2 times
        LocalDate in = LocalDate.now();
        LocalDate out = LocalDate.now().plusDays(1);

        hotel.bookRoom(customer, dlxRoom, in, out, false, false);
        customer.setLastBookingTime(null);
        hotel.bookRoom(customer, dlxRoom, in, out, false, false);

        // Recommendation should be a Deluxe room
        Room recommended = hotel.recommendRoom(customer.getEmail());
        DeluxeRoom dlx2 = new DeluxeRoom("202", 4, 200.0, true, true, true, 0.20);
        hotel.addRoom(dlx2);
        Room rec = hotel.recommendRoom(customer.getEmail());
        assertNotNull(rec);
        assertTrue("Should recommend Deluxe", rec instanceof DeluxeRoom);
    }

    @Test
    public void FindCustomerByEmail() {
        Customer found = hotel.findCustomerByEmail("ali@test.com");
        assertNotNull(found);
        assertEquals("Ali", found.getFirstName());
    }

    @Test
    public void SpamProtection() {
        LocalDate in = LocalDate.now().plusDays(1);
        LocalDate out = LocalDate.now().plusDays(2);
        // 1st Booking
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        // 2nd Booking immediately
        int initialCount = customer.getReservationHistory().size();
        hotel.bookRoom(customer, stdRoom, in, out, false, false);
        int finalCount = customer.getReservationHistory().size();

        assertEquals("Spam protection should block 2nd booking", initialCount, finalCount);
    }
}
