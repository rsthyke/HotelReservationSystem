package com.hotel.model;

import java.time.LocalDate;

/**
 * Interface for objects that can be reserved.
 * It forces classes to have a price calculation method.
 */

public interface Reservable {

    /**
     * Calculates the price for a specific date.
     * @param date The date to check the price.
     * @return The price for that day.
     */

    double calculatePrice(LocalDate date);

    /**
     * Returns the type of the room.
     * @return String representing room type.
     */

    String getRoomType();
}
