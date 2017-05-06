package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.Assert.assertEquals;

public class ConversionEngineIlluminanceTest extends ConversionEngineTest {

    @Test
    public void testIlluminance() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "footcandle", "footcandle", PLACES), 0.0);
        assertEquals(0.0307848, engine.convertAsDouble(AMOUNT, "footcandle", "kilolux", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertAsDouble(AMOUNT, "footcandle", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "footcandle", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0198611, engine.convertAsDouble(AMOUNT, "footcandle", "lumen per square inch", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "footcandle", "lumen per square meter", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "footcandle", "lux", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "footcandle", "metercandle", PLACES), 0.0);
        assertEquals(3.0784784, engine.convertAsDouble(AMOUNT, "footcandle", "milliphot", PLACES), 0.0);
        assertEquals(30784.783744, engine.convertAsDouble(AMOUNT, "footcandle", "nox", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertAsDouble(AMOUNT, "footcandle", "phot", PLACES), 0.0);
        assertEquals(265.7026948, engine.convertAsDouble(AMOUNT, "kilolux", "footcandle", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "kilolux", "kilolux", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "kilolux", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(265.7026948, engine.convertAsDouble(AMOUNT, "kilolux", "lumen per square foot", PLACES), 0.0);
        assertEquals(1.8451576, engine.convertAsDouble(AMOUNT, "kilolux", "lumen per square inch", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "kilolux", "lumen per square meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "kilolux", "lux", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "kilolux", "metercandle", PLACES), 0.0);
        assertEquals(286.0, engine.convertAsDouble(AMOUNT, "kilolux", "milliphot", PLACES), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "kilolux", "nox", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "kilolux", "phot", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "footcandle", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "kilolux", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "lumen per square foot", PLACES), 0.0);
        assertEquals(18.451576, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "lumen per square inch", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "lumen per square meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "lux", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "metercandle", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "milliphot", PLACES), 0.0);
        assertEquals(28600000.0, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "nox", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square centimeter", "phot", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square foot", "footcandle", PLACES), 0.0);
        assertEquals(0.0307848, engine.convertAsDouble(AMOUNT, "lumen per square foot", "kilolux", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertAsDouble(AMOUNT, "lumen per square foot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square foot", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0198611, engine.convertAsDouble(AMOUNT, "lumen per square foot", "lumen per square inch", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "lumen per square foot", "lumen per square meter", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "lumen per square foot", "lux", PLACES), 0.0);
        assertEquals(30.7847837, engine.convertAsDouble(AMOUNT, "lumen per square foot", "metercandle", PLACES), 0.0);
        assertEquals(3.0784784, engine.convertAsDouble(AMOUNT, "lumen per square foot", "milliphot", PLACES), 0.0);
        assertEquals(30784.783744, engine.convertAsDouble(AMOUNT, "lumen per square foot", "nox", PLACES), 0.0);
        assertEquals(0.0030785, engine.convertAsDouble(AMOUNT, "lumen per square foot", "phot", PLACES), 0.0);
        assertEquals(411.84, engine.convertAsDouble(AMOUNT, "lumen per square inch", "footcandle", PLACES), 0.0);
        assertEquals(4.4330089, engine.convertAsDouble(AMOUNT, "lumen per square inch", "kilolux", PLACES), 0.0);
        assertEquals(0.4433009, engine.convertAsDouble(AMOUNT, "lumen per square inch", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(411.84, engine.convertAsDouble(AMOUNT, "lumen per square inch", "lumen per square foot", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square inch", "lumen per square inch", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertAsDouble(AMOUNT, "lumen per square inch", "lumen per square meter", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertAsDouble(AMOUNT, "lumen per square inch", "lux", PLACES), 0.0);
        assertEquals(4433.0088591, engine.convertAsDouble(AMOUNT, "lumen per square inch", "metercandle", PLACES), 0.0);
        assertEquals(443.3008859, engine.convertAsDouble(AMOUNT, "lumen per square inch", "milliphot", PLACES), 0.0);
        assertEquals(4433008.859136, engine.convertAsDouble(AMOUNT, "lumen per square inch", "nox", PLACES), 0.0);
        assertEquals(0.4433009, engine.convertAsDouble(AMOUNT, "lumen per square inch", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "lumen per square meter", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "lumen per square meter", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "lumen per square meter", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "lumen per square meter", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertAsDouble(AMOUNT, "lumen per square meter", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square meter", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square meter", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lumen per square meter", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "lumen per square meter", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "lumen per square meter", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "lumen per square meter", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "lux", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "lux", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "lux", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "lux", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertAsDouble(AMOUNT, "lux", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lux", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lux", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lux", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "lux", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "lux", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "lux", "phot", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "metercandle", "footcandle", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "metercandle", "kilolux", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "metercandle", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.2657027, engine.convertAsDouble(AMOUNT, "metercandle", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0018452, engine.convertAsDouble(AMOUNT, "metercandle", "lumen per square inch", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "metercandle", "lumen per square meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "metercandle", "lux", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "metercandle", "metercandle", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "metercandle", "milliphot", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "metercandle", "nox", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "metercandle", "phot", PLACES), 0.0);
        assertEquals(2.6570269, engine.convertAsDouble(AMOUNT, "milliphot", "footcandle", PLACES), 0.0);
        assertEquals(0.0286, engine.convertAsDouble(AMOUNT, "milliphot", "kilolux", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "milliphot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2.6570269, engine.convertAsDouble(AMOUNT, "milliphot", "lumen per square foot", PLACES), 0.0);
        assertEquals(0.0184516, engine.convertAsDouble(AMOUNT, "milliphot", "lumen per square inch", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "milliphot", "lumen per square meter", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "milliphot", "lux", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "milliphot", "metercandle", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "milliphot", "milliphot", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "milliphot", "nox", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "milliphot", "phot", PLACES), 0.0);
        assertEquals(0.0002657, engine.convertAsDouble(AMOUNT, "nox", "footcandle", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertAsDouble(AMOUNT, "nox", "kilolux", PLACES), 0.0);
        assertEquals(3e-07, engine.convertAsDouble(AMOUNT, "nox", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(0.0002657, engine.convertAsDouble(AMOUNT, "nox", "lumen per square foot", PLACES), 0.0);
        assertEquals(1.8e-06, engine.convertAsDouble(AMOUNT, "nox", "lumen per square inch", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "nox", "lumen per square meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "nox", "lux", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "nox", "metercandle", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "nox", "milliphot", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "nox", "nox", PLACES), 0.0);
        assertEquals(3e-07, engine.convertAsDouble(AMOUNT, "nox", "phot", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertAsDouble(AMOUNT, "phot", "footcandle", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "phot", "kilolux", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "phot", "lumen per square centimeter", PLACES), 0.0);
        assertEquals(2657.0269481, engine.convertAsDouble(AMOUNT, "phot", "lumen per square foot", PLACES), 0.0);
        assertEquals(18.451576, engine.convertAsDouble(AMOUNT, "phot", "lumen per square inch", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "phot", "lumen per square meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "phot", "lux", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "phot", "metercandle", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "phot", "milliphot", PLACES), 0.0);
        assertEquals(28600000.0, engine.convertAsDouble(AMOUNT, "phot", "nox", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "phot", "phot", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "lx", "lm/m", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "lux", "phot");
        assertEquals("illuminance", conversion.getUnitGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConversion() {
        engine.convertAsDouble(AMOUNT, "lux", "blondel");
    }
}