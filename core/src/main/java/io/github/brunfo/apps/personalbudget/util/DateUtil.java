package io.github.brunfo.apps.personalbudget.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Auxiliary methods class.
 */
public class DateUtil {


    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Formats a date to string
     *
     * @param date The date to be returned as String
     * @return String formatted
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a string into date
     *
     * @param dateString date as string.
     * @return .date object
     */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checks if string is valid as a date.
     *
     * @param dateString date as string
     * @return true if valid.
     */
    public static boolean isValidDate(String dateString) {
        return DateUtil.parse(dateString) == null;
    }

    public static String getDateFormat() {
        return DATE_PATTERN;
    }
}