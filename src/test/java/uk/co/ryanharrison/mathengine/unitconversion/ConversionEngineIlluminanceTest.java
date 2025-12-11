package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineIlluminanceTest extends ConversionEngineBaseTest {

    @Test
    public void testIlluminance() {
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "footcandle", "footcandle", PLACES), 0.0);
        assertEquals(0.0307848, engine.convertToDouble(AMOUNT, "footcandle", "kilolux", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertToDouble(AMOUNT, "footcandle", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "footcandle", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0198611, engine.convertToDouble(AMOUNT, "footcandle", "lumen per square inch", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "footcandle", "lumen per square meter", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "footcandle", "lux", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "footcandle", "metercandle", PLACES), 0.0);
        assertEquals(3.0784784, engine.convertToDouble(AMOUNT, "footcandle", "milliphot", PLACES), 0.0);
        assertEquals(30784.783744, engine.convertToDouble(AMOUNT, "footcandle", "nox", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertToDouble(AMOUNT, "footcandle", "phot", PLACES), 0.0);
        assertEquals(265.7026948, engine.convertToDouble(AMOUNT, "kilolux", "footcandle", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "kilolux", "kilolux", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "kilolux", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(265.7026948, engine.convertToDouble(AMOUNT, "kilolux", "lumen per square foot", PLACES), 0.0);
        assertEquals(1.8451576, engine.convertToDouble(AMOUNT, "kilolux", "lumen per square inch", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "kilolux", "lumen per square meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "kilolux", "lux", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "kilolux", "metercandle", PLACES), 0.0);
        assertEquals(286.0, engine.convertToDouble(AMOUNT, "kilolux", "milliphot", PLACES), 0.0);
        assertEquals(2860000.0, engine.convertToDouble(AMOUNT, "kilolux", "nox", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "kilolux", "phot", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "footcandle", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "kilolux", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "lumen per square foot", PLACES), 0.0);
        assertEquals(18.451576, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "lumen per square inch", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "lumen per square meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "lux", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "metercandle", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "milliphot", PLACES), 0.0);
        assertEquals(28600000.0, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "nox", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square centimeter", "phot", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square foot", "footcandle", PLACES), 0.0);
        assertEquals(0.0307848, engine.convertToDouble(AMOUNT, "lumen per square foot", "kilolux", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertToDouble(AMOUNT, "lumen per square foot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square foot", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0198611, engine.convertToDouble(AMOUNT, "lumen per square foot", "lumen per square inch", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "lumen per square foot", "lumen per square meter", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "lumen per square foot", "lux", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertToDouble(AMOUNT, "lumen per square foot", "metercandle", PLACES), 0.0);
        assertEquals(3.0784784, engine.convertToDouble(AMOUNT, "lumen per square foot", "milliphot", PLACES), 0.0);
        assertEquals(30784.783744, engine.convertToDouble(AMOUNT, "lumen per square foot", "nox", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertToDouble(AMOUNT, "lumen per square foot", "phot", PLACES), 0.0);
        assertEquals(411.84, engine.convertToDouble(AMOUNT, "lumen per square inch", "footcandle", PLACES), 0.0);
        assertEquals(4.4330089, engine.convertToDouble(AMOUNT, "lumen per square inch", "kilolux", PLACES), 0.0);
        assertEquals(0.4433009, engine.convertToDouble(AMOUNT, "lumen per square inch", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(411.84, engine.convertToDouble(AMOUNT, "lumen per square inch", "lumen per square foot", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square inch", "lumen per square inch", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertToDouble(AMOUNT, "lumen per square inch", "lumen per square meter", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertToDouble(AMOUNT, "lumen per square inch", "lux", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertToDouble(AMOUNT, "lumen per square inch", "metercandle", PLACES), 0.0);
        assertEquals(443.3008859, engine.convertToDouble(AMOUNT, "lumen per square inch", "milliphot", PLACES), 0.0);
        assertEquals(4433008.859136, engine.convertToDouble(AMOUNT, "lumen per square inch", "nox", PLACES), 0.0);
        assertEquals(0.4433009, engine.convertToDouble(AMOUNT, "lumen per square inch", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "lumen per square meter", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "lumen per square meter", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "lumen per square meter", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "lumen per square meter", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertToDouble(AMOUNT, "lumen per square meter", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square meter", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square meter", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lumen per square meter", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "lumen per square meter", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "lumen per square meter", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "lumen per square meter", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "lux", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "lux", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "lux", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "lux", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertToDouble(AMOUNT, "lux", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lux", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lux", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lux", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "lux", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "lux", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "lux", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "metercandle", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "metercandle", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "metercandle", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertToDouble(AMOUNT, "metercandle", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertToDouble(AMOUNT, "metercandle", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "metercandle", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "metercandle", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "metercandle", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "metercandle", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "metercandle", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "metercandle", "phot", PLACES), 0.0);
        assertEquals(2.6570269, engine.convertToDouble(AMOUNT, "milliphot", "footcandle", PLACES), 0.0);
        assertEquals(0.0286, engine.convertToDouble(AMOUNT, "milliphot", "kilolux", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "milliphot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.6570269, engine.convertToDouble(AMOUNT, "milliphot", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0184516, engine.convertToDouble(AMOUNT, "milliphot", "lumen per square inch", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "milliphot", "lumen per square meter", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "milliphot", "lux", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "milliphot", "metercandle", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "milliphot", "milliphot", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "milliphot", "nox", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "milliphot", "phot", PLACES), 0.0);
        assertEquals(0.0002657, engine.convertToDouble(AMOUNT, "nox", "footcandle", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertToDouble(AMOUNT, "nox", "kilolux", PLACES), 0.0);
        assertEquals(3e-07, engine.convertToDouble(AMOUNT, "nox", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.0002657, engine.convertToDouble(AMOUNT, "nox", "lumen per square foot", PLACES), 0.0);
        assertEquals(1.8e-06, engine.convertToDouble(AMOUNT, "nox", "lumen per square inch", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "nox", "lumen per square meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "nox", "lux", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "nox", "metercandle", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "nox", "milliphot", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "nox", "nox", PLACES), 0.0);
        assertEquals(3e-07, engine.convertToDouble(AMOUNT, "nox", "phot", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertToDouble(AMOUNT, "phot", "footcandle", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "phot", "kilolux", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "phot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertToDouble(AMOUNT, "phot", "lumen per square foot", PLACES), 0.0);
        assertEquals(18.451576, engine.convertToDouble(AMOUNT, "phot", "lumen per square inch", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "phot", "lumen per square meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "phot", "lux", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "phot", "metercandle", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "phot", "milliphot", PLACES), 0.0);
        assertEquals(28600000.0, engine.convertToDouble(AMOUNT, "phot", "nox", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "phot", "phot", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "lx", "lm/m", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        ConversionResult conversion = engine.convert(AMOUNT, "lux", "phot");
        assertEquals("illuminance", conversion.groupName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertToDouble(AMOUNT, "lux", "blondel");
        });
    }
}