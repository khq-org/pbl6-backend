package com.backend.pbl6schoolsystem.common;

import java.util.regex.Pattern;

public class CommonUtils {
    private static Pattern patternNumeric = Pattern.compile("-?(0|[1-9]\\d*)");

    public static Boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return patternNumeric.matcher(strNum).matches();
    }
}
