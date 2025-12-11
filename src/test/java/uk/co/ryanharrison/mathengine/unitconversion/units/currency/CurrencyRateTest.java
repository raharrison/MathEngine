package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyRateTest {

    // ==================== Construction ====================

    @Test
    void constructorCreatesRateWithValidValues() {
        CurrencyRate rate = new CurrencyRate("USD", 1.0);

        assertThat(rate.currencyCode()).isEqualTo("USD");
        assertThat(rate.rate()).isEqualTo(1.0);
    }

    @Test
    void constructorNormalizesCurrencyCodeToUppercase() {
        CurrencyRate rate1 = new CurrencyRate("usd", 1.0);
        CurrencyRate rate2 = new CurrencyRate("Eur", 0.85);
        CurrencyRate rate3 = new CurrencyRate("GBP", 0.75);

        assertThat(rate1.currencyCode()).isEqualTo("USD");
        assertThat(rate2.currencyCode()).isEqualTo("EUR");
        assertThat(rate3.currencyCode()).isEqualTo("GBP");
    }

    @Test
    void constructorRejectsNullCurrencyCode() {
        assertThatThrownBy(() -> new CurrencyRate(null, 1.0))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Currency code");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.1, -1.0, -100.0})
    void constructorRejectsNonPositiveRates(double invalidRate) {
        assertThatThrownBy(() -> new CurrencyRate("USD", invalidRate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rate must be positive")
                .hasMessageContaining(String.valueOf(invalidRate));
    }

    @Test
    void constructorAcceptsPositiveRates() {
        CurrencyRate rate1 = new CurrencyRate("USD", 0.001);
        CurrencyRate rate2 = new CurrencyRate("EUR", 1.0);
        CurrencyRate rate3 = new CurrencyRate("GBP", 100.0);

        assertThat(rate1.rate()).isEqualTo(0.001);
        assertThat(rate2.rate()).isEqualTo(1.0);
        assertThat(rate3.rate()).isEqualTo(100.0);
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsAndHashCodeWorkCorrectly() {
        CurrencyRate rate1 = new CurrencyRate("USD", 1.0);
        CurrencyRate rate2 = new CurrencyRate("USD", 1.0);

        assertThat(rate1).isEqualTo(rate2);
        assertThat(rate1.hashCode()).isEqualTo(rate2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentRates() {
        CurrencyRate rate1 = new CurrencyRate("USD", 1.0);
        CurrencyRate rate2 = new CurrencyRate("USD", 1.2);

        assertThat(rate1).isNotEqualTo(rate2);
    }

    @Test
    void equalsReturnsFalseForDifferentCurrencyCodes() {
        CurrencyRate rate1 = new CurrencyRate("USD", 1.0);
        CurrencyRate rate2 = new CurrencyRate("EUR", 1.0);

        assertThat(rate1).isNotEqualTo(rate2);
    }

    // ==================== toString ====================

    @Test
    void toStringContainsCurrencyCodeAndRate() {
        CurrencyRate rate = new CurrencyRate("USD", 1.23);

        String string = rate.toString();

        assertThat(string).contains("USD");
        assertThat(string).contains("1.23");
    }
}
