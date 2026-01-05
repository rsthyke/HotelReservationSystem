package com.hotel.model;

import java.time.LocalDate;

public interface Reservable {
    double calculatePrice(LocalDate date);
    String getRoomType();
}
