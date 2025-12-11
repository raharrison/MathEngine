package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class XmlCurrencyRateProvider implements CurrencyRateProvider {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final URI apiUrl;

    private XmlCurrencyRateProvider() {
        this.apiUrl = null;
    }

    XmlCurrencyRateProvider(URI apiUrl) {
        this.apiUrl = Objects.requireNonNull(apiUrl, "API URL cannot be null");
    }

    static XmlCurrencyRateProvider europeanCentralBank() {
        return new XmlCurrencyRateProvider(URI.create("https://www.ecb.int/stats/eurofxref/eurofxref-daily.xml"));
    }

    /**
     * Creates a provider for testing that parses an XML document.
     *
     * @param document the XML document to parse
     * @return a currency rate provider that parses the given document
     */
    static XmlCurrencyRateProvider fromDocument(Document document) {
        return new XmlCurrencyRateProvider() {
            @Override
            public CurrencyRateData fetchRates() {
                return parseXml(document);
            }
        };
    }

    @Override
    public CurrencyRateData fetchRates() {
        try {
            Document document = downloadXml();
            return parseXml(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch currency rates from " + apiUrl, e);
        }
    }

    private Document downloadXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(apiUrl.toURL().openStream());
    }

    CurrencyRateData parseXml(Document document) {
        document.getDocumentElement().normalize();

        NodeList cubeElements = document.getElementsByTagName("Cube");
        List<CurrencyRate> rates = new ArrayList<>();
        Instant lastUpdated = Instant.now();

        for (int i = 0; i < cubeElements.getLength(); i++) {
            Node node = cubeElements.item(i);
            NamedNodeMap attributes = node.getAttributes();

            if (attributes == null) {
                continue;
            }

            int attributeCount = attributes.getLength();

            if (attributeCount == 1) {
                Node timeAttr = attributes.getNamedItem("time");
                if (timeAttr != null) {
                    LocalDate date = LocalDate.parse(timeAttr.getNodeValue(), DATE_FORMATTER);
                    lastUpdated = date.atStartOfDay().toInstant(ZoneOffset.UTC);
                }
            } else if (attributeCount == 2) {
                Node currencyAttr = attributes.getNamedItem("currency");
                Node rateAttr = attributes.getNamedItem("rate");

                if (currencyAttr != null && rateAttr != null) {
                    String currency = currencyAttr.getNodeValue();
                    double rate = Double.parseDouble(rateAttr.getNodeValue());
                    rates.add(new CurrencyRate(currency, 1.0 / rate));
                }
            }
        }

        rates.add(new CurrencyRate("EUR", 1.0));

        return new CurrencyRateData(rates, lastUpdated);
    }

    @Override
    public String toString() {
        return "XmlCurrencyRateProvider(" + apiUrl + ")";
    }
}
