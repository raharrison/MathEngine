package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import java.util.Objects;

/**
 * Immutable value object representing an exchange rate for a currency.
 * <p>
 * Currency rates are typically relative to a base currency (e.g., USD).
 * Used by {@link CurrencyRateProvider} implementations.
 * </p>
 *
 * @see Currency
 * @see CurrencyRateProvider
 */
record CurrencyRate(String currencyCode, double rate) {
    CurrencyRate(String currencyCode, double rate) {
        Objects.requireNonNull(currencyCode, "Currency code cannot be null");
        if (rate <= 0.0) {
            throw new IllegalArgumentException("Rate must be positive, got: " + rate);
        }
        this.currencyCode = currencyCode.toUpperCase();
        this.rate = rate;
    }
}
