package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.BeforeAll;

public class ConversionEngineTest {

    static ConversionEngine engine;

    static final Double AMOUNT = 2.86;
    static final int PLACES = 7;

    @BeforeAll
    public static void beforeClass() {
        engine = ConversionEngine.getInstance();
    }
}
