package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utility class for formatting and parsing time values in timezone conversions.
 * <p>
 * Time values are represented as integers in 24-hour format:
 * </p>
 * <ul>
 *     <li>12 → 12:00 (noon)</li>
 *     <li>1430 → 14:30 (2:30 PM)</li>
 *     <li>143045 → 14:30:45 (2:30:45 PM)</li>
 * </ul>
 */
final class TimeFormatter {
    private static final int HOURS_DIVISOR = 10000;
    private static final int MINUTES_DIVISOR = 100;
    private static final int MAX_TIME_DIGITS = 6;

    private TimeFormatter() {
        throw new AssertionError("Utility class");
    }

    /**
     * Parses a time value and extracts hour, minute, and second components.
     *
     * @param timeValue the time value to parse (e.g., 143045 for 14:30:45)
     * @return a TimeComponents object containing extracted hour, minute, second
     * @throws IllegalArgumentException if timeValue has more than 6 digits
     */
    static TimeComponents parseTime(double timeValue) {
        String timeString = Long.toString((long) Math.abs(timeValue));

        if (timeString.length() > MAX_TIME_DIGITS) {
            throw new IllegalArgumentException(
                    "Expected 24-hour time format (max 6 digits), got: " + timeString
            );
        }

        double normalized = normalizeTime(timeString);

        int hour = (int) (normalized / HOURS_DIVISOR);
        int minute = (int) ((normalized % HOURS_DIVISOR) / MINUTES_DIVISOR);
        int second = (int) (normalized % MINUTES_DIVISOR);

        return new TimeComponents(hour, minute, second);
    }

    /**
     * Normalizes a time string by padding with zeros to ensure proper parsing.
     * <p>
     * Examples:
     * </p>
     * <ul>
     *     <li>"12" → "1200" (pad to 4 digits)</li>
     *     <li>"1430" → "1430" (already 4 digits)</li>
     *     <li>"14305" → "143050" (pad to 6 digits)</li>
     * </ul>
     *
     * @param time the time string to normalize
     * @return normalized time value
     */
    private static double normalizeTime(String time) {
        StringBuilder normalized = new StringBuilder(time);
        int targetLength = time.length() % 2 == 0 ? 6 : 5;

        while (normalized.length() < targetLength) {
            normalized.append("0");
        }

        return Double.parseDouble(normalized.toString());
    }

    /**
     * Formats a calendar time back to a time value.
     *
     * @param calendar       the calendar containing the time
     * @param includeSeconds whether to include seconds in the output
     * @return formatted time string (e.g., "1430" or "143045")
     */
    static String formatTime(Calendar calendar, boolean includeSeconds) {
        String hour = formatTwoDigits(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = formatTwoDigits(calendar.get(Calendar.MINUTE));

        if (includeSeconds) {
            String second = formatTwoDigits(calendar.get(Calendar.SECOND));
            return hour + minute + second;
        }

        return hour + minute;
    }

    /**
     * Formats an integer as a two-digit string with leading zero if needed.
     *
     * @param value the value to format (0-59)
     * @return two-digit string representation
     */
    private static String formatTwoDigits(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    /**
     * Creates a calendar for timezone conversion calculations.
     *
     * @param time the time components (hour, minute, second)
     * @return a UTC calendar set to the reference date with the given time
     */
    static Calendar createCalendar(TimeComponents time) {
        var today = new GregorianCalendar();
        Calendar calendar = new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                time.hour(),
                time.minute(),
                time.second()
        );
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return calendar;
    }

    /**
     * Immutable record representing time components.
     *
     * @param hour   the hour (0-23)
     * @param minute the minute (0-59)
     * @param second the second (0-59)
     */
    record TimeComponents(int hour, int minute, int second) {
        boolean hasSeconds() {
            return second != 0;
        }
    }
}
