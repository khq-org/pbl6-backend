package com.backend.pbl6schoolsystem.common;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class CommonUtils {
    private static final Pattern patternNumeric = Pattern.compile("-?(0|[1-9]\\d*)");
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");

    public static Boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return patternNumeric.matcher(strNum).matches();
    }

    public static double roundScore(double input) {
        return Double.valueOf(decimalFormat.format(input));
    }
}
