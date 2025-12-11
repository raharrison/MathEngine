package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import java.time.Instant;
import java.util.List;

interface CurrencyRateProvider {
    CurrencyRateData fetchRates();

    record CurrencyRateData(List<CurrencyRate> rates, Instant lastUpdated) {
        public CurrencyRateData {
            if (rates == null || rates.isEmpty()) {
                throw new IllegalArgumentException("Rates list cannot be null or empty");
            }
            if (lastUpdated == null) {
                throw new IllegalArgumentException("Last updated timestamp cannot be null");
            }
        }
    }
}
