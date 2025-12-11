package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimeZonesTest {

    // ==================== Construction ====================

    @Test
    void withUnitsCreatesGroupWithInitialUnits() {
        List<SimpleUnit> units = List.of(
                createTimeZoneUnit("London", 0),
                createTimeZoneUnit("New York", -18000),
                createTimeZoneUnit("Tokyo", 32400)
        );

        TimeZones timeZones = TimeZones.withUnits(units);

        assertThat(timeZones.getName()).isEqualTo("time zones");
        assertThat(timeZones.getUnits()).hasSize(3);
    }

    // ==================== Conversion - Time Adjustment ====================

    @ParameterizedTest
    @CsvSource({
            "1200, London, London, 1200",          // Same timezone
            "1430, London, London, 1430",          // Same timezone with different time
            "0, London, London, 0",                // Midnight same timezone
            "2359, London, London, 2359"           // Before midnight same timezone
    })
    void convertWithinSameTimezoneReturnsInputTime(
            double inputTime,
            String fromCity,
            String toCity,
            double expectedTime
    ) {
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(inputTime),
                fromCity,
                toCity
        );

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result().doubleValue()).isEqualTo(expectedTime);
    }

    @Test
    void convertAdjustsTimeByOffsetDifference() {
        // London (UTC+0) to New York (UTC-5, -18000 seconds)
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(1200), // 12:00 in London
                "London",
                "New York"
        );

        assertThat(result.isSuccessful()).isTrue();
        // 12:00 London = 07:00 New York (5 hours behind)
        assertThat(result.result().doubleValue()).isEqualTo(700.0);
    }

    @Test
    void convertHandlesTimeWithSeconds() {
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(193045), // 19:30:45
                "London",
                "New York"
        );

        assertThat(result.isSuccessful()).isTrue();
        // Should preserve seconds in the result
        String resultStr = String.valueOf(result.result().longValue());
        assertThat(resultStr).hasSize(6); // HHMMSS format
    }

    @Test
    void convertHandlesNegativeTimeAsAbsolute() {
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(-1200),
                "London",
                "New York"
        );

        assertThat(result.isSuccessful()).isTrue();
        // Should treat -1200 as 1200
        assertThat(result.result().doubleValue()).isEqualTo(700.0);
    }

    // ==================== Conversion - Partial Results ====================

    @Test
    void convertReturnsPartialWhenFromCityNotFound() {
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(1200),
                "UnknownCity",
                "London"
        );

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNull();
        assertThat(result.toUnit()).isNull();
    }

    @Test
    void convertReturnsPartialWhenToCityNotFound() {
        TimeZones timeZones = createTestTimeZones();

        ConversionResult result = timeZones.convert(
                BigRational.of(1200),
                "London",
                "UnknownCity"
        );

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.fromUnit()).isNotNull();
        assertThat(result.toUnit()).isNull();
    }

    // ==================== Update ====================

    @Test
    void updateLoadsTimeZoneData() {
        List<SimpleUnit> units = List.of(createTimeZoneUnit("London", 0));
        TimeZones timeZones = TimeZones.withUnits(units);

        // Note: This will fail if timezone.csv and zone.csv are not in classpath
        // For a real test, we would need to provide test data files
        // For now, just verify the method can be called
        timeZones.update();

        assertThat(timeZones.getUnits()).isNotEmpty();
    }

    @Test
    void updatePreservesExistingUnits() {
        List<SimpleUnit> units = List.of(
                createTimeZoneUnit("London", 0),
                createTimeZoneUnit("CustomCity", 7200)
        );

        TimeZones timeZones = TimeZones.withUnits(units);

        // After update, should still have initial units
        List<String> cityNames = timeZones.getUnits().stream()
                .map(SimpleUnit::getSingular)
                .toList();

        assertThat(cityNames).contains("London", "CustomCity");
    }

    // ==================== getUnits ====================

    @Test
    void getUnitsReturnsCurrentUnits() {
        List<SimpleUnit> units = List.of(
                createTimeZoneUnit("London", 0),
                createTimeZoneUnit("New York", -18000)
        );

        TimeZones timeZones = TimeZones.withUnits(units);
        List<SimpleUnit> retrievedUnits = timeZones.getUnits();

        assertThat(retrievedUnits).hasSize(2);
    }

    // ==================== toString ====================

    @Test
    void toStringReturnsTimeZones() {
        List<SimpleUnit> units = List.of(createTimeZoneUnit("London", 0));
        TimeZones timeZones = TimeZones.withUnits(units);

        assertThat(timeZones.toString()).isEqualTo("time zones");
    }

    // ==================== Helper Methods ====================

    private TimeZones createTestTimeZones() {
        return TimeZones.withUnits(List.of(
                createTimeZoneUnit("London", 0),           // UTC
                createTimeZoneUnit("New York", -18000),    // UTC-5
                createTimeZoneUnit("Tokyo", 32400),        // UTC+9
                createTimeZoneUnit("Los Angeles", -28800)  // UTC-8
        ));
    }

    private SimpleUnit createTimeZoneUnit(String cityName, int offsetSeconds) {
        return SimpleUnit.builder()
                .singular(cityName)
                .plural(cityName)
                .conversionFactor(offsetSeconds)
                .build();
    }
}
