package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.Assert.assertEquals;

public class ConversionEngineAstronomicalTest extends ConversionEngineTest {

    @Test
    public void testAstronomical() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "astronomical unit", "astronomical unit", PLACES), 0.0);
        assertEquals(4.2784991017626E8, engine.convertAsDouble(AMOUNT, "astronomical unit", "kilometer", PLACES), 0.0);
        assertEquals(1427.1536817, engine.convertAsDouble(AMOUNT, "astronomical unit", "light second", PLACES), 0.0);
        assertEquals(23.7858947, engine.convertAsDouble(AMOUNT, "astronomical unit", "light minute", PLACES), 0.0);
        assertEquals(0.3964316, engine.convertAsDouble(AMOUNT, "astronomical unit", "light hour", PLACES), 0.0);
        assertEquals(0.016518, engine.convertAsDouble(AMOUNT, "astronomical unit", "light day", PLACES), 0.0);
        assertEquals(4.52e-05, engine.convertAsDouble(AMOUNT, "astronomical unit", "light year", PLACES), 0.0);
        assertEquals(1.39e-05, engine.convertAsDouble(AMOUNT, "astronomical unit", "parsec", PLACES), 0.0);
        assertEquals(427849910176.26, engine.convertAsDouble(AMOUNT, "astronomical unit", "meter", PLACES), 0.0);
        assertEquals(265853608.7848589, engine.convertAsDouble(AMOUNT, "astronomical unit", "mile", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kilometer", "astronomical unit", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "kilometer", "kilometer", PLACES), 0.0);
        assertEquals(9.5e-06, engine.convertAsDouble(AMOUNT, "kilometer", "light second", PLACES), 0.0);
        assertEquals(2e-07, engine.convertAsDouble(AMOUNT, "kilometer", "light minute", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kilometer", "light hour", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kilometer", "light day", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kilometer", "light year", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kilometer", "parsec", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "kilometer", "meter", PLACES), 0.0);
        assertEquals(1.7771216, engine.convertAsDouble(AMOUNT, "kilometer", "mile", PLACES), 0.0);
        assertEquals(0.0057314, engine.convertAsDouble(AMOUNT, "light second", "astronomical unit", PLACES), 0.0);
        assertEquals(857406.42988, engine.convertAsDouble(AMOUNT, "light second", "kilometer", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "light second", "light second", PLACES), 0.0);
        assertEquals(0.0476667, engine.convertAsDouble(AMOUNT, "light second", "light minute", PLACES), 0.0);
        assertEquals(0.0007944, engine.convertAsDouble(AMOUNT, "light second", "light hour", PLACES), 0.0);
        assertEquals(3.31e-05, engine.convertAsDouble(AMOUNT, "light second", "light day", PLACES), 0.0);
        assertEquals(1e-07, engine.convertAsDouble(AMOUNT, "light second", "light year", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "light second", "parsec", PLACES), 0.0);
        assertEquals(857406429.88, engine.convertAsDouble(AMOUNT, "light second", "meter", PLACES), 0.0);
        assertEquals(532767.6555665, engine.convertAsDouble(AMOUNT, "light second", "mile", PLACES), 0.0);
        assertEquals(0.3438845, engine.convertAsDouble(AMOUNT, "light minute", "astronomical unit", PLACES), 0.0);
        assertEquals(51444385.7928, engine.convertAsDouble(AMOUNT, "light minute", "kilometer", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "light minute", "light second", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "light minute", "light minute", PLACES), 0.0);
        assertEquals(0.0476667, engine.convertAsDouble(AMOUNT, "light minute", "light hour", PLACES), 0.0);
        assertEquals(0.0019861, engine.convertAsDouble(AMOUNT, "light minute", "light day", PLACES), 0.0);
        assertEquals(5.4e-06, engine.convertAsDouble(AMOUNT, "light minute", "light year", PLACES), 0.0);
        assertEquals(1.7e-06, engine.convertAsDouble(AMOUNT, "light minute", "parsec", PLACES), 0.0);
        assertEquals(51444385792.799995, engine.convertAsDouble(AMOUNT, "light minute", "meter", PLACES), 0.0);
        assertEquals(31966059.3339895, engine.convertAsDouble(AMOUNT, "light minute", "mile", PLACES), 0.0);
        assertEquals(20.6330687, engine.convertAsDouble(AMOUNT, "light hour", "astronomical unit", PLACES), 0.0);
        assertEquals(3086663147.568, engine.convertAsDouble(AMOUNT, "light hour", "kilometer", PLACES), 0.0);
        assertEquals(10296.0, engine.convertAsDouble(AMOUNT, "light hour", "light second", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "light hour", "light minute", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "light hour", "light hour", PLACES), 0.0);
        assertEquals(0.1191667, engine.convertAsDouble(AMOUNT, "light hour", "light day", PLACES), 0.0);
        assertEquals(0.0003263, engine.convertAsDouble(AMOUNT, "light hour", "light year", PLACES), 0.0);
        assertEquals(0.0001, engine.convertAsDouble(AMOUNT, "light hour", "parsec", PLACES), 0.0);
        assertEquals(3086663147568.0, engine.convertAsDouble(AMOUNT, "light hour", "meter", PLACES), 0.0);
        assertEquals(1917963560.03937, engine.convertAsDouble(AMOUNT, "light hour", "mile", PLACES), 0.0);
        assertEquals(495.1936495, engine.convertAsDouble(AMOUNT, "light day", "astronomical unit", PLACES), 0.0);
        assertEquals(74079915541.63199, engine.convertAsDouble(AMOUNT, "light day", "kilometer", PLACES), 0.0);
        assertEquals(247104.0, engine.convertAsDouble(AMOUNT, "light day", "light second", PLACES), 0.0);
        assertEquals(4118.4, engine.convertAsDouble(AMOUNT, "light day", "light minute", PLACES), 0.0);
        assertEquals(68.64, engine.convertAsDouble(AMOUNT, "light day", "light hour", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "light day", "light day", PLACES), 0.0);
        assertEquals(0.0078303, engine.convertAsDouble(AMOUNT, "light day", "light year", PLACES), 0.0);
        assertEquals(0.0024008, engine.convertAsDouble(AMOUNT, "light day", "parsec", PLACES), 0.0);
        assertEquals(74079915541632.0, engine.convertAsDouble(AMOUNT, "light day", "meter", PLACES), 0.0);
        assertEquals(46031125440.94488, engine.convertAsDouble(AMOUNT, "light day", "mile", PLACES), 0.0);
        assertEquals(180869.4804719, engine.convertAsDouble(AMOUNT, "light year", "astronomical unit", PLACES), 0.0);
        assertEquals(27057689151581.086, engine.convertAsDouble(AMOUNT, "light year", "kilometer", PLACES), 0.0);
        assertEquals(90254736.0, engine.convertAsDouble(AMOUNT, "light year", "light second", PLACES), 0.0);
        assertEquals(1504245.6, engine.convertAsDouble(AMOUNT, "light year", "light minute", PLACES), 0.0);
        assertEquals(25070.76, engine.convertAsDouble(AMOUNT, "light year", "light hour", PLACES), 0.0);
        assertEquals(1044.615, engine.convertAsDouble(AMOUNT, "light year", "light day", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "light year", "light year", PLACES), 0.0);
        assertEquals(0.87688, engine.convertAsDouble(AMOUNT, "light year", "parsec", PLACES), 0.0);
        assertEquals(2.705768915158109e+16, engine.convertAsDouble(AMOUNT, "light year", "meter", PLACES), 0.0);
        assertEquals(16812868567305.117, engine.convertAsDouble(AMOUNT, "light year", "mile", PLACES), 0.0);
        assertEquals(589917.328, engine.convertAsDouble(AMOUNT, "parsec", "astronomical unit", PLACES), 0.0);
        assertEquals(88250376152524.22, engine.convertAsDouble(AMOUNT, "parsec", "kilometer", PLACES), 0.0);
        assertEquals(294371568.7221332, engine.convertAsDouble(AMOUNT, "parsec", "light second", PLACES), 0.0);
        assertEquals(4906192.8120356, engine.convertAsDouble(AMOUNT, "parsec", "light minute", PLACES), 0.0);
        assertEquals(81769.8802006, engine.convertAsDouble(AMOUNT, "parsec", "light hour", PLACES), 0.0);
        assertEquals(3407.0783417, engine.convertAsDouble(AMOUNT, "parsec", "light day", PLACES), 0.0);
        assertEquals(9.3280721, engine.convertAsDouble(AMOUNT, "parsec", "light year", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "parsec", "parsec", PLACES), 0.0);
        assertEquals(8.825037615252422e+16, engine.convertAsDouble(AMOUNT, "parsec", "meter", PLACES), 0.0);
        assertEquals(54836241445287.164, engine.convertAsDouble(AMOUNT, "parsec", "mile", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "astronomical unit", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "meter", "kilometer", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "light second", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "light minute", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "light hour", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "light day", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "light year", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "meter", "parsec", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "meter", "meter", PLACES), 0.0);
        assertEquals(0.0017771, engine.convertAsDouble(AMOUNT, "meter", "mile", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "mile", "astronomical unit", PLACES), 0.0);
        assertEquals(4.6027238, engine.convertAsDouble(AMOUNT, "mile", "kilometer", PLACES), 0.0);
        assertEquals(1.54e-05, engine.convertAsDouble(AMOUNT, "mile", "light second", PLACES), 0.0);
        assertEquals(3e-07, engine.convertAsDouble(AMOUNT, "mile", "light minute", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "mile", "light hour", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "mile", "light day", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "mile", "light year", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "mile", "parsec", PLACES), 0.0);
        assertEquals(4602.72384, engine.convertAsDouble(AMOUNT, "mile", "meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "mile", "mile", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "km", "m", PLACES), 0.0);
        assertEquals(2.943715687221332E8, engine.convertAsDouble(AMOUNT, "psec", "light sec", PLACES), 0.0);
        assertEquals(1504245.6, engine.convertAsDouble(AMOUNT, "ly", "light min", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "parsec", "miles");
        assertEquals("astronomical", conversion.getUnitGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConversion() {
        engine.convertAsDouble(AMOUNT, "light second", "feet");
    }
}