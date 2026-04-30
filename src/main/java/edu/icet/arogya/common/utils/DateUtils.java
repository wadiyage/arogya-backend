package edu.icet.arogya.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // "yyyy-MM-dd"

    public static String format(LocalDate date) {
        return date != null ? date.format(ISO_FORMATTER) : null;
    }
}
