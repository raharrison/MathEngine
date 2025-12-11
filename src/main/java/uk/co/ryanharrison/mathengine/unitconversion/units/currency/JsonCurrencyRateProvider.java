package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JsonCurrencyRateProvider implements CurrencyRateProvider {
    private static final Pattern CURRENCY_PATTERN = Pattern.compile(
            "\"([A-Z]+)\":\\s(\\d+\\.?\\d*)",
            Pattern.CASE_INSENSITIVE
    );

    private final URI apiUrl;

    private JsonCurrencyRateProvider() {
        this.apiUrl = null;
    }

    JsonCurrencyRateProvider(URI apiUrl) {
        this.apiUrl = Objects.requireNonNull(apiUrl, "API URL cannot be null");
    }

    static JsonCurrencyRateProvider openExchangeRates(String appId) {
        String url = String.format("https://openexchangerates.org/api/latest.json?app_id=%s", appId);
        return new JsonCurrencyRateProvider(URI.create(url));
    }

    /**
     * Creates a provider for testing that parses raw JSON content.
     *
     * @param jsonContent the raw JSON content to parse
     * @return a currency rate provider that parses the given JSON
     */
    static JsonCurrencyRateProvider fromJson(String jsonContent) {
        return new JsonCurrencyRateProvider() {
            @Override
            public CurrencyRateData fetchRates() {
                return parseJson(jsonContent);
            }
        };
    }

    @Override
    public CurrencyRateData fetchRates() {
        try {
            String jsonContent = downloadJson();
            return parseJson(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch currency rates from " + apiUrl, e);
        }
    }

    private String downloadJson() throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(apiUrl.toURL().openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        }
        return content.toString();
    }

    CurrencyRateData parseJson(String json) {
        Matcher matcher = CURRENCY_PATTERN.matcher(json);
        List<CurrencyRate> rates = new ArrayList<>();
        Instant lastUpdated = Instant.now();

        while (matcher.find()) {
            String code = matcher.group(1);
            String value = matcher.group(2);

            if ("timestamp".equalsIgnoreCase(code)) {
                lastUpdated = Instant.ofEpochSecond(Long.parseLong(value));
            } else {
                double rate = Double.parseDouble(value);
                rates.add(new CurrencyRate(code, 1.0 / rate));
            }
        }

        if (rates.isEmpty()) {
            throw new IllegalStateException("No currency rates found in JSON response");
        }

        return new CurrencyRateData(rates, lastUpdated);
    }

    @Override
    public String toString() {
        return "JsonCurrencyRateProvider(" + apiUrl + ")";
    }
}
