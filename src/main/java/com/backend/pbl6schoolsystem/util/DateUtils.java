package com.backend.pbl6schoolsystem.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtils {
    public static LocalDate convertString2LocalDate(String date) {
        return LocalDate.parse(date);
    }

    public static LocalTime convertString2LocalTime(String time) {
        return LocalTime.parse(time);
    }
}
