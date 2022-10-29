package com.backend.pbl6schoolsystem.util;

import java.time.LocalDate;

public class DateUtils {
    public static LocalDate convertString2LocalDate(String date) {
        return LocalDate.parse(date);
    }
}
