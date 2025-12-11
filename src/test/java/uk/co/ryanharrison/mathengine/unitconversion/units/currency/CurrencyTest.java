package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CurrencyTest {

    // ==================== Construction ====================

    @Test
    void withUnitsCreatesGroupWithInitialUnits() {
        List<SimpleUnit> units = List.of(
                createUnit("USD", 1.0),
                createUnit("EUR", 0.85),
                createUnit("GBP", 0.73)
        );

        Currency currency = Currency.withUnits(units);

        assertThat(currency.getName()).isEqualTo("currency");
        assertThat(currency.getUnits()).hasSize(3);
        assertThat(currency.getLastUpdated()).isEqualTo(Instant.EPOCH);
    }

    @Test
    void withUnitsRejectsNullUnits() {
        assertThatThrownBy(() -> Currency.withUnits(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Units list cannot be null");
    }

    // ==================== Conversion - Before Update ====================

    @Test
    void convertUsesInitialRates() {
        List<SimpleUnit> units = List.of(
                createUnit("USD", 1.0),
                createUnit("EUR", 0.85)
        );

        Currency currency = Currency.withUnits(units);
        ConversionResult result = currency.convert(BigRational.of(100), "USD", "EUR");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.result().doubleValue()).isCloseTo(100.0 * 1.0 / 0.85, within(0.01));
    }

    @Test
    void convertHandlesUnknownCurrency() {
        List<SimpleUnit> units = List.of(createUnit("USD", 1.0));

        Currency currency = Currency.withUnits(units);
        ConversionResult result = currency.convert(BigRational.of(100), "USD", "GBP");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.toUnit()).isNull();
    }

    // ==================== Update ====================

    @Test
    void updateFetchesRatesFromProvider() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));

        Currency currency = createCurrencyWithTestProvider(units, List.of(
                new CurrencyRate("EUR", 0.85),
                new CurrencyRate("GBP", 0.73)
        ));

        currency.update();

        // Should now have EUR and GBP in addition to USD
        assertThat(currency.getUnits()).hasSizeGreaterThanOrEqualTo(2);

        List<String> codes = currency.getUnits().stream()
                .map(SimpleUnit::getSingular)
                .toList();
        assertThat(codes).contains("EUR", "GBP");
    }

    @Test
    void updateUpdatesExistingCurrencyRates() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));
        units.add(createUnit("EUR", 0.5)); // Old rate

        Currency currency = createCurrencyWithTestProvider(units, List.of(
                new CurrencyRate("EUR", 0.85) // New rate
        ));

        currency.update();

        SimpleUnit eurUnit = currency.getUnits().stream()
                .filter(u -> u.getSingular().equals("EUR"))
                .findFirst()
                .orElseThrow();

        assertThat(eurUnit.getConversionFactor().doubleValue()).isCloseTo(0.85, within(0.001));
    }

    @Test
    void updateAddsNewCurrencies() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));

        Currency currency = createCurrencyWithTestProvider(units, List.of(
                new CurrencyRate("EUR", 0.85),
                new CurrencyRate("GBP", 0.73),
                new CurrencyRate("JPY", 110.0)
        ));

        currency.update();

        List<String> codes = currency.getUnits().stream()
                .map(SimpleUnit::getSingular)
                .toList();

        assertThat(codes).contains("USD", "EUR", "GBP", "JPY");
    }

    @Test
    void updateUpdatesLastUpdatedTimestamp() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));

        Instant testInstant = Instant.ofEpochSecond(1609459200);
        Currency currency = createCurrencyWithTestProvider(
                units,
                List.of(new CurrencyRate("EUR", 0.85)),
                testInstant
        );

        assertThat(currency.getLastUpdated()).isEqualTo(Instant.EPOCH);

        currency.update();

        assertThat(currency.getLastUpdated()).isEqualTo(testInstant);
    }

    @Test
    void updatePreservesExistingUnits() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));
        units.add(createUnit("CAD", 1.25));

        Currency currency = createCurrencyWithTestProvider(units, List.of(
                new CurrencyRate("EUR", 0.85)
        ));

        currency.update();

        List<String> codes = currency.getUnits().stream()
                .map(SimpleUnit::getSingular)
                .toList();

        // Should preserve USD and CAD, plus add EUR
        assertThat(codes).contains("USD", "CAD", "EUR");
    }

    // ==================== Conversion - After Update ====================

    @Test
    void convertUsesUpdatedRates() {
        List<SimpleUnit> units = new ArrayList<>();
        units.add(createUnit("USD", 1.0));
        units.add(createUnit("EUR", 0.5)); // Old rate

        Currency currency = createCurrencyWithTestProvider(units, List.of(
                new CurrencyRate("EUR", 0.85) // New rate
        ));

        currency.update();

        ConversionResult result = currency.convert(BigRational.of(100), "USD", "EUR");

        assertThat(result.isSuccessful()).isTrue();
        // Should use new rate of 0.85
        assertThat(result.result().doubleValue()).isCloseTo(100.0 * 1.0 / 0.85, within(0.01));
    }

    // ==================== toString ====================

    @Test
    void toStringReturnsCurrency() {
        List<SimpleUnit> units = List.of(createUnit("USD", 1.0));
        Currency currency = Currency.withUnits(units);

        assertThat(currency.toString()).isEqualTo("currency");
    }

    // ==================== Helper Methods ====================

    private SimpleUnit createUnit(String code, double conversionFactor) {
        return SimpleUnit.builder()
                .singular(code)
                .plural(code)
                .conversionFactor(conversionFactor)
                .build();
    }

    private Currency createCurrencyWithTestProvider(List<SimpleUnit> initialUnits,
                                                    List<CurrencyRate> testRates) {
        return createCurrencyWithTestProvider(initialUnits, testRates, Instant.now());
    }

    private Currency createCurrencyWithTestProvider(List<SimpleUnit> initialUnits,
                                                    List<CurrencyRate> testRates,
                                                    Instant timestamp) {
        // Create a test provider that returns the specified rates
        CurrencyRateProvider testProvider = () -> new CurrencyRateProvider.CurrencyRateData(
                testRates,
                timestamp
        );

        return Currency.withUnitsAndProviders(new ArrayList<>(initialUnits), List.of(testProvider));
    }
}
