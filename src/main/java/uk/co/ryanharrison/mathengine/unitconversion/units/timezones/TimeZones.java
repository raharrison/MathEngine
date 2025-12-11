package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnit;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnitGroup;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A {@link SimpleUnitGroup} for timezone conversions based on city time offsets.
 * <p>
 * TimeZones converts time values between different cities by adjusting for their
 * UTC offset differences. Time values are represented as integers in 24-hour format:
 * </p>
 * <ul>
 *     <li>1200 = 12:00 (noon)</li>
 *     <li>1430 = 14:30</li>
 *     <li>143045 = 14:30:45</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * TimeZones timeZones = TimeZones.withUnits(units);
 * timeZones.update(); // Load timezone data
 *
 * // Convert 14:30 from London to New York
 * ConversionResult result = timeZones.convert(
 *     BigRational.of(1430),
 *     "London",
 *     "New York"
 * );
 * }</pre>
 *
 * <p>
 * This class is thread-safe. Read operations can proceed concurrently,
 * while update operations have exclusive access.
 * </p>
 *
 * @see TimeZoneDataProvider
 * @see SimpleUnitGroup
 */
public final class TimeZones extends SimpleUnitGroup {
    private final TimeZoneDataProvider provider;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile List<SimpleUnit> currentUnits;

    private TimeZones(List<SimpleUnit> initialUnits, TimeZoneDataProvider provider) {
        super("time zones", initialUnits);
        this.provider = provider;
        this.currentUnits = List.copyOf(initialUnits);
    }

    /**
     * Creates a timezone group with predefined units.
     *
     * @param units the initial timezone units
     * @return a new timezone group
     * @throws NullPointerException if units is null
     */
    public static TimeZones withUnits(List<SimpleUnit> units) {
        Objects.requireNonNull(units, "Units list cannot be null");
        return new TimeZones(units, TimeZoneDataProvider.fromClasspath());
    }

    @Override
    public List<SimpleUnit> getUnits() {
        lock.readLock().lock();
        try {
            return currentUnits;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ConversionResult convert(BigRational amount, String from, String to) {
        SimpleUnit fromUnit = findUnit(from);
        if (fromUnit == null) {
            return ConversionResult.partial(null, null, amount);
        }

        SimpleUnit toUnit = findUnit(to);
        if (toUnit == null) {
            return ConversionResult.partial(fromUnit, null, amount);
        }

        // Parse time value into components
        double timeValue = Math.round(Math.abs(amount.doubleValue()));
        TimeFormatter.TimeComponents time = TimeFormatter.parseTime(timeValue);

        // Create calendar with the time
        Calendar calendar = TimeFormatter.createCalendar(time);

        // Apply timezone offset difference
        int offsetDiff = (int) (toUnit.getConversionFactor().doubleValue()
                - fromUnit.getConversionFactor().doubleValue());
        calendar.add(Calendar.SECOND, offsetDiff);

        // Format result
        String resultTime = TimeFormatter.formatTime(calendar, time.hasSeconds());
        BigRational result = BigRational.of(resultTime);

        return ConversionResult.success(fromUnit, toUnit, amount, result, getName());
    }

    /**
     * Updates timezone data from the configured provider.
     * <p>
     * Loads city timezone information and updates the available units.
     * Existing units are preserved and new or updated cities are merged.
     * This operation acquires an exclusive write lock.
     * </p>
     */
    @Override
    public void update() {
        lock.writeLock().lock();
        try {
            List<CityTimeZone> cityTimeZones = provider.loadTimeZones();

            // Start with existing units in a map (keyed by lowercase singular name)
            Map<String, SimpleUnit> unitMap = new LinkedHashMap<>();
            for (SimpleUnit unit : currentUnits) {
                unitMap.put(unit.getSingular().toLowerCase(), unit);
            }

            // Add or replace units from the provider
            for (CityTimeZone cityTimeZone : cityTimeZones) {
                SimpleUnit unit = SimpleUnit.builder()
                        .singular(cityTimeZone.cityName())
                        .plural(cityTimeZone.cityName())
                        .conversionFactor(cityTimeZone.offsetSeconds())
                        .build();
                unitMap.put(cityTimeZone.cityName().toLowerCase(), unit);
            }

            this.currentUnits = List.copyOf(unitMap.values());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        return "time zones";
    }
}
