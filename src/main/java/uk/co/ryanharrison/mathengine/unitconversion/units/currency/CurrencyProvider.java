package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

abstract class CurrencyProvider {
    protected Date lastUpdated;
    protected String url;

    CurrencyProvider(String url) {
        this.url = url;
    }

    abstract void download() throws SAXException, IOException, ParserConfigurationException;

    Date getLastUpdated() {
        return lastUpdated;
    }

    String getUrl() {
        return url;
    }

    abstract List<Unit> process() throws ParseException;
}

class Unit {
    private String unit;

    private double rate;

    Unit(String unit, double rate) {
        this.unit = unit;
        this.rate = rate;
    }

    String getUnit() {
        return unit;
    }

    double getRate() {
        return rate;
    }
}
