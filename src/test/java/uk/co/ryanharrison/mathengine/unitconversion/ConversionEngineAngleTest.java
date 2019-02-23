package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.Assert.assertEquals;

public class ConversionEngineAngleTest extends ConversionEngineTest {

    @Test
    public void testAngles() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "degree", "degree"), 0.0);
        assertEquals(3.1777777777777776, engine.convertAsDouble(AMOUNT, "degree", "gradian"), 0.0);
        assertEquals(0.0499164166072, engine.convertAsDouble(AMOUNT, "degree", "radian"), 0.0);
        assertEquals(0.007944444444470256, engine.convertAsDouble(AMOUNT, "degree", "rotation"), 0.0);
        assertEquals(2.574, engine.convertAsDouble(AMOUNT, "gradian", "degree"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "gradian", "gradian"), 0.0);
        assertEquals(0.04492477494647999, engine.convertAsDouble(AMOUNT, "gradian", "radian"), 0.0);
        assertEquals(0.007150000000023229, engine.convertAsDouble(AMOUNT, "gradian", "rotation"), 0.0);
        assertEquals(163.86592940688305, engine.convertAsDouble(AMOUNT, "radian", "degree"), 0.0);
        assertEquals(182.07325489653672, engine.convertAsDouble(AMOUNT, "radian", "gradian"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "radian", "radian"), 0.0);
        assertEquals(0.45518313724282067, engine.convertAsDouble(AMOUNT, "radian", "rotation"), 0.0);
        assertEquals(1029.5999999966548, engine.convertAsDouble(AMOUNT, "rotation", "degree"), 0.0);
        assertEquals(1143.9999999962831, engine.convertAsDouble(AMOUNT, "rotation", "gradian"), 0.0);
        assertEquals(17.969909978533614, engine.convertAsDouble(AMOUNT, "rotation", "radian"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "rotation", "rotation"), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(182.07325489653672, engine.convertAsDouble(AMOUNT, "rads", "grads"), 0.0);
        assertEquals(3.1777777777777776, engine.convertAsDouble(AMOUNT, "degs", "g"), 0.0);
        assertEquals(17.969909978533614, engine.convertAsDouble(AMOUNT, "circles", "rads"), 0.0);
        assertEquals(182.07325489653672, engine.convertAsDouble(AMOUNT, "rad", "grads"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "deg", "d"), 0.0);
        assertEquals(0.45518313724282067, engine.convertAsDouble(AMOUNT, "r", "fc"), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "radians", "degrees");
        assertEquals("angles", conversion.getUnitGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConversion() {
        engine.convertAsDouble(AMOUNT, "radians", "bits");
    }
}