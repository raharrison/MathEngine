package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JsonCurrencyRateProviderTest {

    // ==================== Parsing - Success ====================

    @Test
    void parseJsonExtractsMultipleCurrencyRates() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "base": "USD",
                    "rates": {
                        "EUR": 0.815,
                        "GBP": 0.731,
                        "JPY": 103.25
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        assertThat(data.rates()).hasSize(3);

        List<String> codes = data.rates().stream()
                .map(CurrencyRate::currencyCode)
                .toList();
        assertThat(codes).contains("EUR", "GBP", "JPY");
    }

    @Test
    void parseJsonInvertsRatesCorrectly() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "rates": {
                        "EUR": 0.815,
                        "GBP": 0.5
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        CurrencyRate eurRate = data.rates().stream()
                .filter(r -> r.currencyCode().equals("EUR"))
                .findFirst()
                .orElseThrow();

        CurrencyRate gbpRate = data.rates().stream()
                .filter(r -> r.currencyCode().equals("GBP"))
                .findFirst()
                .orElseThrow();

        // Rates are inverted: 1.0 / original_rate
        assertThat(eurRate.rate()).isCloseTo(1.0 / 0.815, within(0.0001));
        assertThat(gbpRate.rate()).isCloseTo(1.0 / 0.5, within(0.0001));
    }

    @Test
    void parseJsonExtractsTimestamp() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "rates": {
                        "EUR": 0.815
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        assertThat(data.lastUpdated()).isEqualTo(Instant.ofEpochSecond(1609459200));
    }

    @Test
    void parseJsonHandlesCaseInsensitiveKeys() {
        String json = """
                {
                    "TIMESTAMP": 1609459200,
                    "rates": {
                        "eur": 0.815,
                        "GbP": 0.731
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        List<String> codes = data.rates().stream()
                .map(CurrencyRate::currencyCode)
                .toList();

        // All currency codes should be uppercased
        assertThat(codes).contains("EUR", "GBP");
    }

    @Test
    void parseJsonHandlesDecimalRates() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "rates": {
                        "EUR": 0.815123,
                        "JPY": 103.456789
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        assertThat(data.rates()).hasSize(2);
    }

    @Test
    void parseJsonIgnoresNonRateFields() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "base": "USD",
                    "disclaimer": "some text",
                    "license": "some license",
                    "rates": {
                        "EUR": 0.815
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        // Should only extract EUR rate
        assertThat(data.rates()).hasSize(1);
        assertThat(data.rates().getFirst().currencyCode()).isEqualTo("EUR");
    }

    // ==================== Parsing - Failures ====================

    @Test
    void parseJsonRejectsEmptyRates() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "rates": {}
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);

        assertThatThrownBy(provider::fetchRates)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No currency rates found");
    }

    @Test
    void parseJsonRejectsInvalidJson() {
        String json = "not valid json";

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);

        // Should complete without throwing since pattern just won't match
        assertThatThrownBy(provider::fetchRates)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No currency rates found");
    }

    // ==================== Factory Methods ====================

    @Test
    void openExchangeRatesCreatesProviderWithCorrectUrl() {
        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.openExchangeRates("test-app-id");

        assertThat(provider.toString()).contains("openexchangerates.org");
        assertThat(provider.toString()).contains("test-app-id");
    }

    @Test
    void fromJsonCreatesProviderThatParsesContent() {
        String json = """
                {
                    "timestamp": 1609459200,
                    "rates": {
                        "EUR": 0.815
                    }
                }
                """;

        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.fromJson(json);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        assertThat(data.rates()).hasSize(1);
        assertThat(data.rates().getFirst().currencyCode()).isEqualTo("EUR");
    }

    // ==================== toString ====================

    @Test
    void toStringContainsUrl() {
        JsonCurrencyRateProvider provider = JsonCurrencyRateProvider.openExchangeRates("test-id");

        String string = provider.toString();

        assertThat(string).contains("JsonCurrencyRateProvider");
        assertThat(string).contains("http");
    }
}
