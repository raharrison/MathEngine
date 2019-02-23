package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.BeforeClass;

public class ConversionEngineTest {

    static ConversionEngine engine;

    static final Double AMOUNT = 2.86;
    static final int PLACES = 7;

    @BeforeClass
    public static void beforeClass() {
        engine = ConversionEngine.getInstance();
    }
}
