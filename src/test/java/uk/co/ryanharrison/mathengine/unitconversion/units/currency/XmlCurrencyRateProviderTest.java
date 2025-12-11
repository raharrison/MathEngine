package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class XmlCurrencyRateProviderTest {

    // ==================== Parsing - Success ====================

    @Test
    void parseXmlExtractsMultipleCurrencyRates() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                            <Cube currency='GBP' rate='0.8990'/>
                            <Cube currency='JPY' rate='126.49'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        // Should have 3 currencies + EUR (added automatically)
        assertThat(data.rates()).hasSize(4);

        List<String> codes = data.rates().stream()
                .map(CurrencyRate::currencyCode)
                .toList();
        assertThat(codes).contains("USD", "GBP", "JPY", "EUR");
    }

    @Test
    void parseXmlInvertsRatesCorrectly() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                            <Cube currency='GBP' rate='2.0'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        CurrencyRate usdRate = data.rates().stream()
                .filter(r -> r.currencyCode().equals("USD"))
                .findFirst()
                .orElseThrow();

        CurrencyRate gbpRate = data.rates().stream()
                .filter(r -> r.currencyCode().equals("GBP"))
                .findFirst()
                .orElseThrow();

        // Rates are inverted: 1.0 / original_rate
        assertThat(usdRate.rate()).isCloseTo(1.0 / 1.2271, within(0.0001));
        assertThat(gbpRate.rate()).isCloseTo(1.0 / 2.0, within(0.0001));
    }

    @Test
    void parseXmlAddsEurWithRateOfOne() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        CurrencyRate eurRate = data.rates().stream()
                .filter(r -> r.currencyCode().equals("EUR"))
                .findFirst()
                .orElseThrow();

        assertThat(eurRate.rate()).isEqualTo(1.0);
    }

    @Test
    void parseXmlExtractsTimestamp() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-15'>
                            <Cube currency='USD' rate='1.2271'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        LocalDate expectedDate = LocalDate.of(2021, 1, 15);
        assertThat(data.lastUpdated()).isEqualTo(expectedDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    @Test
    void parseXmlHandlesDecimalRates() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.227123'/>
                            <Cube currency='JPY' rate='126.456789'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        // Should have 2 currencies + EUR
        assertThat(data.rates()).hasSize(3);
    }

    @Test
    void parseXmlIgnoresCubesWithoutRates() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube/>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                        </Cube>
                        <Cube/>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        // Should have 1 currency (USD) + EUR
        assertThat(data.rates()).hasSize(2);
    }

    @Test
    void parseXmlHandlesMultipleDates() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                        </Cube>
                        <Cube time='2021-01-02'>
                            <Cube currency='GBP' rate='0.8990'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        // Should extract rates from all dates
        List<String> codes = data.rates().stream()
                .map(CurrencyRate::currencyCode)
                .toList();
        assertThat(codes).contains("USD", "GBP", "EUR");

        // Timestamp should be from the last valid date
        LocalDate expectedDate = LocalDate.of(2021, 1, 2);
        assertThat(data.lastUpdated()).isEqualTo(expectedDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    // ==================== Factory Methods ====================

    @Test
    void europeanCentralBankCreatesProviderWithCorrectUrl() {
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.europeanCentralBank();

        assertThat(provider.toString()).contains("ecb.int");
        assertThat(provider.toString()).contains("eurofxref");
    }

    @Test
    void fromDocumentCreatesProviderThatParsesDocument() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
                    <Cube>
                        <Cube time='2021-01-01'>
                            <Cube currency='USD' rate='1.2271'/>
                        </Cube>
                    </Cube>
                </gesmes:Envelope>
                """;

        Document document = parseXmlString(xml);
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.fromDocument(document);
        CurrencyRateProvider.CurrencyRateData data = provider.fetchRates();

        assertThat(data.rates()).hasSizeGreaterThanOrEqualTo(1);
    }

    // ==================== toString ====================

    @Test
    void toStringContainsUrl() {
        XmlCurrencyRateProvider provider = XmlCurrencyRateProvider.europeanCentralBank();

        String string = provider.toString();

        assertThat(string).contains("XmlCurrencyRateProvider");
        assertThat(string).contains("http");
    }

    // ==================== Helper Methods ====================

    private Document parseXmlString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }
}
