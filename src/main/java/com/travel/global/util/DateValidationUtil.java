package com.travel.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidationUtil {

  static String format = "yyyy-MM-dd";

  public static boolean isCheckInValid(LocalDate checkIn) {
    LocalDate now = LocalDate.now();
    return checkIn.isAfter(now) || checkIn.isEqual(now);
  }

  public static boolean isCheckOutValid(LocalDate checkIn, LocalDate checkOut) {
    return checkOut.isAfter(checkIn);
  }

  public static LocalDate checkInDate(String checkInDate) {
    if (checkInDate == null) {
      return LocalDate.now();
    } else {
      return convertStringToLocalDate(checkInDate, format);
    }
  }

  public static LocalDate checkOutDate(String checkInDate, String checkOutDate) {
    LocalDate checkIn = checkInDate(checkInDate);
    if (checkOutDate == null) {
      return checkIn.plusDays(1);
    } else {
      return convertStringToLocalDate(checkOutDate, format);
    }
  }

  public static LocalDate convertStringToLocalDate(String dateString, String format)
      throws DateTimeParseException {DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern(format);
    return LocalDate.parse(dateString, formatter);
  }
}
