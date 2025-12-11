package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TimeZoneDataProvider {
    private static final String ZONE_FILE = "zone.csv";
    private static final String TIMEZONE_FILE = "timezone.csv";

    private TimeZoneDataProvider() {
    }

    static TimeZoneDataProvider fromClasspath() {
        return new TimeZoneDataProvider();
    }

    /**
     * Creates a provider for testing that loads from specified input streams.
     *
     * @param zoneStream     the zone.csv input stream
     * @param timezoneStream the timezone.csv input stream
     * @return a time zone data provider
     */
    static TimeZoneDataProvider fromStreams(InputStream zoneStream, InputStream timezoneStream) {
        return new TimeZoneDataProvider() {
            @Override
            List<CityTimeZone> loadTimeZones() {
                try {
                    Map<Integer, CityTimeZone> timeZoneMap = new HashMap<>();

                    if (zoneStream != null) {
                        parseZones(zoneStream, timeZoneMap);
                    }

                    if (timezoneStream != null) {
                        parseTimeZoneOffsets(timezoneStream, timeZoneMap);
                    }

                    return new ArrayList<>(timeZoneMap.values());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load time zones", e);
                }
            }
        };
    }

    List<CityTimeZone> loadTimeZones() {
        try {
            Map<Integer, CityTimeZone> timeZoneMap = new HashMap<>();

            // Load zones first
            try (InputStream zoneStream = getClass().getClassLoader().getResourceAsStream(ZONE_FILE)) {
                if (zoneStream == null) {
                    throw new IllegalArgumentException("Resource not found: " + ZONE_FILE);
                }
                parseZones(zoneStream, timeZoneMap);
            }

            // Then load timezone offsets
            try (InputStream timezoneStream = getClass().getClassLoader().getResourceAsStream(TIMEZONE_FILE)) {
                if (timezoneStream == null) {
                    throw new IllegalArgumentException("Resource not found: " + TIMEZONE_FILE);
                }
                parseTimeZoneOffsets(timezoneStream, timeZoneMap);
            }

            return new ArrayList<>(timeZoneMap.values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load time zones", e);
        }
    }

    void parseZones(InputStream stream, Map<Integer, CityTimeZone> timeZoneMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] tokens = line.replace("\"", "").split(",");
                    if (tokens.length >= 3) {
                        int index = Integer.parseInt(tokens[0]);
                        String zoneName = tokens[2].replace("_", " ").trim();
                        String cityName = extractCityName(zoneName);
                        timeZoneMap.put(index, new CityTimeZone(cityName, 0));
                    }
                } catch (NumberFormatException e) {
                    // Skip malformed lines
                }
            }
        }
    }

    void parseTimeZoneOffsets(InputStream stream, Map<Integer, CityTimeZone> timeZoneMap) throws IOException {
        long currentTimeSeconds = System.currentTimeMillis() / 1000L;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            String previousLine = null;
            int currentIndex = 1;

            while ((line = reader.readLine()) != null) {
                try {
                    line = line.replace("\"", "").replace("_", " ");
                    String[] tokens = line.split(",");
                    if (tokens.length >= 4) {
                        int index = Integer.parseInt(tokens[0]);
                        long startTime = Long.parseLong(tokens[2]);

                        if (index == currentIndex + 1 && previousLine != null) {
                            updateTimeZoneOffset(previousLine, timeZoneMap);
                            previousLine = null;
                            currentIndex++;
                        }

                        if (index == currentIndex) {
                            if (startTime > currentTimeSeconds && previousLine != null) {
                                updateTimeZoneOffset(previousLine, timeZoneMap);
                                previousLine = null;
                                currentIndex++;
                            } else {
                                previousLine = line;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip malformed lines
                }
            }

            // Process any remaining line
            if (previousLine != null) {
                updateTimeZoneOffset(previousLine, timeZoneMap);
            }
        }
    }

    private void updateTimeZoneOffset(String line, Map<Integer, CityTimeZone> timeZoneMap) {
        String[] tokens = line.split(",");
        if (tokens.length >= 4) {
            try {
                int index = Integer.parseInt(tokens[0]);
                int offset = Integer.parseInt(tokens[3]);

                CityTimeZone existing = timeZoneMap.get(index);
                if (existing != null) {
                    timeZoneMap.put(index, new CityTimeZone(existing.cityName(), offset));
                }
            } catch (NumberFormatException e) {
                // Skip malformed data
            }
        }
    }

    private String extractCityName(String zoneName) {
        int lastSlash = zoneName.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < zoneName.length() - 1) {
            return zoneName.substring(lastSlash + 1).trim();
        }
        return zoneName.trim();
    }
}
