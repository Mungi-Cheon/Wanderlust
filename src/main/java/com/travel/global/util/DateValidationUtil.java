package com.travel.global.util;

import java.time.LocalDate;

public class DateValidationUtil {

  public static boolean isCheckInValid(LocalDate checkIn) {
    return checkIn.isAfter(LocalDate.now()) || checkIn.isEqual(LocalDate.now());
  }

  public static boolean isCheckOutValid(LocalDate checkIn, LocalDate checkOut) {
    return checkOut.isAfter(checkIn);
  }
}
