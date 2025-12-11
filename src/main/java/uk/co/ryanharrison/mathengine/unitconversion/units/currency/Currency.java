package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnit;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnitGroup;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A {@link SimpleUnitGroup} for currency conversions with dynamically updated exchange rates.
 * <p>
 * Currency rates are fetched from external providers (e.g., OpenExchangeRates, European Central Bank)
 * and can be refreshed by calling {@link #update()}. The class supports multiple providers
 * with automatic fallback if the primary provider fails.
 * </p>
 * <p>
 * This class is thread-safe. Read operations (conversions, queries) can proceed concurrently,
 * while write operations (updates) have exclusive access during rate updates.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create with default providers
 * Currency currency = Currency.withUnits(units);
 *
 * // Update rates from external providers
 * currency.update();
 *
 * // Perform conversion
 * ConversionResult result = currency.convert(
 *     BigRational.of(100),
 *     "USD",
 *     "EUR"
 * );
 * }</pre>
 *
 * @see CurrencyRateProvider
 * @see SimpleUnitGroup
 */
public final class Currency extends SimpleUnitGroup {
    private static final String DEFAULT_PRIMARY_APP_ID = "05a4f48b8a8b4ac394d2ea8db7f1a0c6";

    private final List<CurrencyRateProvider> providers;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile Instant lastUpdated;
    private volatile List<SimpleUnit> currentUnits;

    private Currency(List<SimpleUnit> initialUnits, List<CurrencyRateProvider> providers) {
        super("currency", initialUnits);
        this.providers = List.copyOf(providers);
        this.lastUpdated = Instant.EPOCH;
        this.currentUnits = List.copyOf(initialUnits);
    }

    /**
     * Creates a currency group with predefined units and default providers.
     *
     * @param units the initial currency units
     * @return a new currency group
     * @throws IllegalArgumentException if units is null
     */
    public static Currency withUnits(List<SimpleUnit> units) {
        if (units == null) {
            throw new IllegalArgumentException("Units list cannot be null");
        }
        List<CurrencyRateProvider> defaultProviders = Arrays.asList(
                JsonCurrencyRateProvider.openExchangeRates(DEFAULT_PRIMARY_APP_ID),
                XmlCurrencyRateProvider.europeanCentralBank()
        );
        return new Currency(units, defaultProviders);
    }

    /**
     * Creates a currency group with predefined units and custom providers (for testing).
     *
     * @param units     the initial currency units
     * @param providers the currency rate providers
     * @return a new currency group
     */
    static Currency withUnitsAndProviders(List<SimpleUnit> units, List<CurrencyRateProvider> providers) {
        return new Currency(units, providers);
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

        BigRational result = amount
                .multiply(fromUnit.getConversionFactor())
                .divide(toUnit.getConversionFactor());
        return ConversionResult.success(fromUnit, toUnit, amount, result, getName());
    }

    /**
     * Updates currency exchange rates from configured providers.
     * <p>
     * Providers are tried in order until one succeeds. If all providers fail,
     * a RuntimeException is thrown. This operation acquires an exclusive write lock.
     * </p>
     *
     * @throws RuntimeException if all configured providers fail to fetch rates
     */
    @Override
    public void update() {
        lock.writeLock().lock();
        try {
            for (CurrencyRateProvider provider : providers) {
                try {
                    CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();
                    applyRates(data);
                    return;
                } catch (Exception e) {
                    // Try next provider
                }
            }
            throw new RuntimeException("All currency providers failed to update rates");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void applyRates(CurrencyRateProvider.CurrencyRateData data) {
        // Start with existing units in a map (keyed by uppercase currency code)
        Map<String, SimpleUnit> unitMap = new LinkedHashMap<>();
        for (SimpleUnit unit : currentUnits) {
            unitMap.put(unit.getSingular().toUpperCase(), unit);
        }

        // Update existing units with new rates or add new currencies
        for (CurrencyRate rate : data.rates()) {
            String currencyCode = rate.currencyCode().toUpperCase();
            SimpleUnit existingUnit = unitMap.get(currencyCode);

            if (existingUnit != null) {
                // Update existing unit with new rate
                unitMap.put(currencyCode, existingUnit.withConversionFactor(rate.rate()));
            } else {
                // Add new currency unit
                SimpleUnit newUnit = SimpleUnit.builder()
                        .singular(currencyCode)
                        .plural(currencyCode)
                        .conversionFactor(rate.rate())
                        .build();
                unitMap.put(currencyCode, newUnit);
            }
        }

        this.currentUnits = List.copyOf(unitMap.values());
        this.lastUpdated = data.lastUpdated();
    }

    /**
     * Returns the timestamp of the last successful rate update.
     *
     * @return the instant when rates were last updated, or {@link Instant#EPOCH} if never updated
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "currency";
    }
}
