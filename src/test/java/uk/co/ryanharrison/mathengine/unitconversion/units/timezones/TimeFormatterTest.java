package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeFormatterTest {

    // ==================== parseTime ====================

    @ParameterizedTest
    @CsvSource({
            "1200, 12, 0, 0",           // Noon
            "1430, 14, 30, 0",          // 2:30 PM
            "0, 0, 0, 0",               // Midnight
            "2359, 23, 59, 0",          // One minute before midnight
            "143045, 14, 30, 45",       // 2:30:45 PM with seconds
            "1, 1, 0, 0",               // 01:00
            "12, 12, 0, 0",             // 12:00
            "123, 1, 23, 0",            // 01:23
            "1234, 12, 34, 0",          // 12:34
            "12345, 1, 23, 45"          // 01:23:45
    })
    void parseTimeExtractsHourMinuteSecond(double timeValue, int expectedHour, int expectedMinute, int expectedSecond) {
        TimeFormatter.TimeComponents time = TimeFormatter.parseTime(timeValue);

        assertThat(time.hour()).isEqualTo(expectedHour);
        assertThat(time.minute()).isEqualTo(expectedMinute);
        assertThat(time.second()).isEqualTo(expectedSecond);
    }

    @Test
    void parseTimeHandlesNegativeValuesAsAbsolute() {
        TimeFormatter.TimeComponents time = TimeFormatter.parseTime(-1430);

        assertThat(time.hour()).isEqualTo(14);
        assertThat(time.minute()).isEqualTo(30);
        assertThat(time.second()).isEqualTo(0);
    }

    @Test
    void parseTimeRejectsMoreThanSixDigits() {
        assertThatThrownBy(() -> TimeFormatter.parseTime(1234567))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected 24-hour time format")
                .hasMessageContaining("max 6 digits");
    }

    // ==================== formatTime ====================

    @Test
    void formatTimeWithoutSecondsFormatsCorrectly() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 45);

        String formatted = TimeFormatter.formatTime(calendar, false);

        assertThat(formatted).isEqualTo("1430");
    }

    @Test
    void formatTimeWithSecondsFormatsCorrectly() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 45);

        String formatted = TimeFormatter.formatTime(calendar, true);

        assertThat(formatted).isEqualTo("143045");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, false, 0000",
            "1, 2, false, 0102",
            "9, 9, false, 0909",
            "12, 0, false, 1200",
            "23, 59, false, 2359",
            "14, 30, true, 143000"
    })
    void formatTimeHandlesVariousTimes(int hour, int minute, boolean includeSeconds, String expected) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        String formatted = TimeFormatter.formatTime(calendar, includeSeconds);

        assertThat(formatted).isEqualTo(expected);
    }

    // ==================== createCalendar ====================

    @Test
    void createCalendarSetsTimeComponents() {
        TimeFormatter.TimeComponents time = new TimeFormatter.TimeComponents(14, 30, 45);

        Calendar calendar = TimeFormatter.createCalendar(time);

        assertThat(calendar.get(Calendar.HOUR_OF_DAY)).isEqualTo(14);
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(30);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(45);
    }

    @Test
    void createCalendarUsesUTCTimeZone() {
        TimeFormatter.TimeComponents time = new TimeFormatter.TimeComponents(12, 0, 0);

        Calendar calendar = TimeFormatter.createCalendar(time);

        assertThat(calendar.getTimeZone().getID()).isEqualTo("UTC");
    }

    @Test
    void createCalendarUsesCurrentReferenceDate() {
        TimeFormatter.TimeComponents time = new TimeFormatter.TimeComponents(12, 0, 0);

        Calendar calendar = TimeFormatter.createCalendar(time);
        var now = LocalDateTime.now();

        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(now.getYear());
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(now.getMonth().getValue() - 1);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(now.getDayOfMonth());
    }

    // ==================== TimeComponents ====================

    @Test
    void timeComponentsHasSecondsReturnsTrueWhenNonZero() {
        TimeFormatter.TimeComponents time = new TimeFormatter.TimeComponents(14, 30, 45);

        assertThat(time.hasSeconds()).isTrue();
    }

    @Test
    void timeComponentsHasSecondsReturnsFalseWhenZero() {
        TimeFormatter.TimeComponents time = new TimeFormatter.TimeComponents(14, 30, 0);

        assertThat(time.hasSeconds()).isFalse();
    }

    @Test
    void timeComponentsEqualsAndHashCodeWork() {
        TimeFormatter.TimeComponents time1 = new TimeFormatter.TimeComponents(14, 30, 45);
        TimeFormatter.TimeComponents time2 = new TimeFormatter.TimeComponents(14, 30, 45);

        assertThat(time1).isEqualTo(time2);
        assertThat(time1.hashCode()).isEqualTo(time2.hashCode());
    }

    // ==================== Round-trip Conversion ====================

    @ParameterizedTest
    @CsvSource({
            "1200",
            "1430",
            "143045",
            "0",
            "2359",
            "235959"
    })
    void parseAndFormatRoundTripsCorrectly(String originalTime) {
        double timeValue = Double.parseDouble(originalTime);
        TimeFormatter.TimeComponents parsed = TimeFormatter.parseTime(timeValue);
        Calendar calendar = TimeFormatter.createCalendar(parsed);
        String formatted = TimeFormatter.formatTime(calendar, parsed.hasSeconds());

        // The formatted output should normalize to the same logical time
        TimeFormatter.TimeComponents reparsed = TimeFormatter.parseTime(Double.parseDouble(formatted));
        assertThat(reparsed).isEqualTo(parsed);
    }
}
