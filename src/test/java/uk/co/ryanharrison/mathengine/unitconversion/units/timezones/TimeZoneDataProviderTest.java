package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimeZoneDataProviderTest {

    // ==================== Parsing - Success ====================

    @Test
    void loadTimeZonesParsesZonesAndOffsets() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                2,"ZONE_2","America/New_York"
                3,"ZONE_3","Asia/Tokyo"
                """;

        String timezoneData = """
                1,0,0,0
                2,0,0,-18000
                3,0,0,32400
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, timezoneData);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        assertThat(timeZones).hasSize(3);

        CityTimeZone london = findByCity(timeZones, "London");
        assertThat(london.offsetSeconds()).isEqualTo(0);

        CityTimeZone newYork = findByCity(timeZones, "New York");
        assertThat(newYork.offsetSeconds()).isEqualTo(-18000);

        CityTimeZone tokyo = findByCity(timeZones, "Tokyo");
        assertThat(tokyo.offsetSeconds()).isEqualTo(32400);
    }

    @Test
    void parseZonesExtractsCityNameFromZonePath() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                2,"ZONE_2","America/New_York"
                3,"ZONE_3","Asia/Tokyo"
                4,"ZONE_4","Pacific/Auckland"
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        List<String> cityNames = timeZones.stream()
                .map(CityTimeZone::cityName)
                .toList();

        assertThat(cityNames).contains("London", "New York", "Tokyo", "Auckland");
    }

    @Test
    void parseZonesReplacesUnderscoresWithSpaces() {
        String zoneData = """
                1,"ZONE_1","America/New_York"
                2,"ZONE_2","America/Los_Angeles"
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        List<String> cityNames = timeZones.stream()
                .map(CityTimeZone::cityName)
                .toList();

        assertThat(cityNames).contains("New York", "Los Angeles");
    }

    @Test
    void parseTimeZoneOffsetsUpdatesExistingZones() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                2,"ZONE_2","America/New_York"
                """;

        String timezoneData = """
                1,0,0,0
                2,0,0,-18000
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, timezoneData);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        CityTimeZone london = findByCity(timeZones, "London");
        CityTimeZone newYork = findByCity(timeZones, "New York");

        assertThat(london.offsetSeconds()).isEqualTo(0);
        assertThat(newYork.offsetSeconds()).isEqualTo(-18000);
    }

    @Test
    void parseIgnoresMalformedLines() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                invalid line
                not,enough,fields
                2,"ZONE_2","America/New_York"
                """;

        String timezoneData = """
                1,0,0,0
                invalid
                2,0,0,-18000
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, timezoneData);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        // Should still parse the valid lines
        assertThat(timeZones).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void parseHandlesEmptyFiles() {
        TimeZoneDataProvider provider = createProvider("", "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        assertThat(timeZones).isEmpty();
    }

    @Test
    void parseHandlesZonesWithoutOffsets() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                2,"ZONE_2","America/New_York"
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        // All offsets should default to 0
        assertThat(timeZones).allMatch(tz -> tz.offsetSeconds() == 0);
    }

    @Test
    void parseHandlesOffsetsWithoutZones() {
        String timezoneData = """
                1,0,0,3600
                2,0,0,7200
                """;

        TimeZoneDataProvider provider = createProvider("", timezoneData);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        // Should not create any time zones if there are no zone definitions
        assertThat(timeZones).isEmpty();
    }

    @Test
    void parseZonesHandlesSimpleZoneName() {
        String zoneData = """
                1,"ZONE_1","UTC"
                2,"ZONE_2","GMT"
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        List<String> cityNames = timeZones.stream()
                .map(CityTimeZone::cityName)
                .toList();

        // Should use the zone name as-is when there's no slash
        assertThat(cityNames).contains("UTC", "GMT");
    }

    @Test
    void parseZonesTrimsWhitespace() {
        String zoneData = """
                1,"ZONE_1","  Europe/London  "
                2,"ZONE_2","  America/New_York  "
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        List<String> cityNames = timeZones.stream()
                .map(CityTimeZone::cityName)
                .toList();

        assertThat(cityNames).contains("London", "New York");
    }

    @Test
    void parseZonesRemovesQuotes() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                2,"ZONE_2","America/New_York"
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, "");
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        // Should parse correctly with quotes in CSV
        assertThat(timeZones).hasSize(2);
    }

    // ==================== Factory Methods ====================

    @Test
    void fromClasspathCreatesProvider() {
        TimeZoneDataProvider provider = TimeZoneDataProvider.fromClasspath();

        assertThat(provider).isNotNull();
    }

    @Test
    void fromStreamsCreatesProviderThatParsesStreams() {
        String zoneData = """
                1,"ZONE_1","Europe/London"
                """;

        String timezoneData = """
                1,0,0,0
                """;

        TimeZoneDataProvider provider = createProvider(zoneData, timezoneData);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        assertThat(timeZones).hasSize(1);
        assertThat(timeZones.getFirst().cityName()).isEqualTo("London");
    }

    @Test
    void fromStreamsHandlesNullStreams() {
        TimeZoneDataProvider provider = TimeZoneDataProvider.fromStreams(null, null);
        List<CityTimeZone> timeZones = provider.loadTimeZones();

        assertThat(timeZones).isEmpty();
    }

    // ==================== Helper Methods ====================

    private TimeZoneDataProvider createProvider(String zoneData, String timezoneData) {
        InputStream zoneStream = new ByteArrayInputStream(zoneData.getBytes(StandardCharsets.UTF_8));
        InputStream timezoneStream = new ByteArrayInputStream(timezoneData.getBytes(StandardCharsets.UTF_8));
        return TimeZoneDataProvider.fromStreams(zoneStream, timezoneStream);
    }

    private CityTimeZone findByCity(List<CityTimeZone> timeZones, String cityName) {
        return timeZones.stream()
                .filter(tz -> tz.cityName().equals(cityName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("City not found: " + cityName));
    }
}
