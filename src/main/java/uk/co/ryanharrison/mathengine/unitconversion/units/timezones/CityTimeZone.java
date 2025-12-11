package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import java.util.Objects;

/**
 * Immutable value object representing a city's timezone offset from UTC.
 * <p>
 * Used by {@link TimeZoneDataProvider} to load timezone data.
 * </p>
 *
 * @see TimeZones
 * @see TimeZoneDataProvider
 */
record CityTimeZone(String cityName, int offsetSeconds) {
    CityTimeZone {
        Objects.requireNonNull(cityName, "City name cannot be null");
    }
}
