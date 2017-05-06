package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.Assert.assertEquals;

public class ConversionEngineDigitalStorageTest extends ConversionEngineTest {

    @Test
    public void testDigitalStorage() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Bit", "Bits"), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Bit", "Kilobits", PLACES), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "Bit", "Megabits"), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "Bit", "Gigabits", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "Bit", "Terabits", PLACES), 0.0);
        assertEquals(0.3575, engine.convertAsDouble(AMOUNT, "Bit", "Bytes"), 0.0);
        assertEquals(0.0003575, engine.convertAsDouble(AMOUNT, "Bit", "Kilobytes", PLACES), 0.0);
        assertEquals(3.575e-7, engine.convertAsDouble(AMOUNT, "Bit", "Megabytes", PLACES+3), 0.0);
        assertEquals(3.575E-10, engine.convertAsDouble(AMOUNT, "Bit", "Gigabytes"), 0.0);
        assertEquals(3.575E-13, engine.convertAsDouble(AMOUNT, "Bit", "Terabytes"), 0.0);
        assertEquals(2860, engine.convertAsDouble(AMOUNT, "Kilobit", "Bits"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Kilobit", "Kilobits"), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Kilobit", "Megabits", PLACES), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "Kilobit", "Gigabits"), 0.0);
        assertEquals(2.86E-9, engine.convertAsDouble(AMOUNT, "Kilobit", "Terabits"), 0.0);
        assertEquals(357.5, engine.convertAsDouble(AMOUNT, "Kilobit", "Bytes"), 0.0);
        assertEquals(0.3575, engine.convertAsDouble(AMOUNT, "Kilobit", "Kilobytes"), 0.0);
        assertEquals(3.5749999999999996E-4, engine.convertAsDouble(AMOUNT, "Kilobit", "Megabytes"), 0.0);
        assertEquals(3.5749999999999997E-7, engine.convertAsDouble(AMOUNT, "Kilobit", "Gigabytes"), 0.0);
        assertEquals(3.575E-10, engine.convertAsDouble(AMOUNT, "Kilobit", "Terabytes"), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "Megabit", "Bits"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Megabit", "Kilobits"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Megabit", "Megabits"), 0.0);
        assertEquals(0.0028599999999999997, engine.convertAsDouble(AMOUNT, "Megabit", "Gigabits"), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "Megabit", "Terabits"), 0.0);
        assertEquals(357500.0, engine.convertAsDouble(AMOUNT, "Megabit", "Bytes"), 0.0);
        assertEquals(357.5, engine.convertAsDouble(AMOUNT, "Megabit", "Kilobytes"), 0.0);
        assertEquals(0.3575, engine.convertAsDouble(AMOUNT, "Megabit", "Megabytes"), 0.0);
        assertEquals(3.5749999999999996E-4, engine.convertAsDouble(AMOUNT, "Megabit", "Gigabytes"), 0.0);
        assertEquals(3.5749999999999997E-7, engine.convertAsDouble(AMOUNT, "Megabit", "Terabytes"), 0.0);
        assertEquals(2.86E9, engine.convertAsDouble(AMOUNT, "Gigabit", "Bits"), 0.0);
        assertEquals(2860000, engine.convertAsDouble(AMOUNT, "Gigabit", "Kilobits"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Gigabit", "Megabits"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Gigabit", "Gigabits"), 0.0);
        assertEquals(0.0028599999999999997, engine.convertAsDouble(AMOUNT, "Gigabit", "Terabits"), 0.0);
        assertEquals(3.575E8, engine.convertAsDouble(AMOUNT, "Gigabit", "Bytes"), 0.0);
        assertEquals(357500.0, engine.convertAsDouble(AMOUNT, "Gigabit", "Kilobytes"), 0.0);
        assertEquals(357.5, engine.convertAsDouble(AMOUNT, "Gigabit", "Megabytes"), 0.0);
        assertEquals(0.3575, engine.convertAsDouble(AMOUNT, "Gigabit", "Gigabytes"), 0.0);
        assertEquals(3.5749999999999996E-4, engine.convertAsDouble(AMOUNT, "Gigabit", "Terabytes"), 0.0);
        assertEquals(2.86E12, engine.convertAsDouble(AMOUNT, "Terabit", "Bits"), 0.0);
        assertEquals(2.86E9, engine.convertAsDouble(AMOUNT, "Terabit", "Kilobits"), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "Terabit", "Megabits"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Terabit", "Gigabits"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Terabit", "Terabits"), 0.0);
        assertEquals(3.575E11, engine.convertAsDouble(AMOUNT, "Terabit", "Bytes"), 0.0);
        assertEquals(3.575E8, engine.convertAsDouble(AMOUNT, "Terabit", "Kilobytes"), 0.0);
        assertEquals(357500.0, engine.convertAsDouble(AMOUNT, "Terabit", "Megabytes"), 0.0);
        assertEquals(357.5, engine.convertAsDouble(AMOUNT, "Terabit", "Gigabytes"), 0.0);
        assertEquals(0.3575, engine.convertAsDouble(AMOUNT, "Terabit", "Terabytes"), 0.0);
        assertEquals(22.88, engine.convertAsDouble(AMOUNT, "Byte", "Bits"), 0.0);
        assertEquals(0.022879999999999998, engine.convertAsDouble(AMOUNT, "Byte", "Kilobits"), 0.0);
        assertEquals(2.2879999999999998E-5, engine.convertAsDouble(AMOUNT, "Byte", "Megabits"), 0.0);
        assertEquals(2.288E-8, engine.convertAsDouble(AMOUNT, "Byte", "Gigabits"), 0.0);
        assertEquals(2.288E-11, engine.convertAsDouble(AMOUNT, "Byte", "Terabits"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Byte", "Bytes"), 0.0);
        assertEquals(0.0028599999999999997, engine.convertAsDouble(AMOUNT, "Byte", "Kilobytes"), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "Byte", "Megabytes"), 0.0);
        assertEquals(2.86E-9, engine.convertAsDouble(AMOUNT, "Byte", "Gigabytes"), 0.0);
        assertEquals(2.86E-12, engine.convertAsDouble(AMOUNT, "Byte", "Terabytes"), 0.0);
        assertEquals(22880.0, engine.convertAsDouble(AMOUNT, "Kilobyte", "Bits"), 0.0);
        assertEquals(22.88, engine.convertAsDouble(AMOUNT, "Kilobyte", "Kilobits"), 0.0);
        assertEquals(0.022879999999999998, engine.convertAsDouble(AMOUNT, "Kilobyte", "Megabits"), 0.0);
        assertEquals(2.2879999999999998E-5, engine.convertAsDouble(AMOUNT, "Kilobyte", "Gigabits"), 0.0);
        assertEquals(2.288E-8, engine.convertAsDouble(AMOUNT, "Kilobyte", "Terabits"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Kilobyte", "Bytes", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Kilobyte", "Kilobytes"), 0.0);
        assertEquals(0.0028599999999999997, engine.convertAsDouble(AMOUNT, "Kilobyte", "Megabytes"), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "Kilobyte", "Gigabytes"), 0.0);
        assertEquals(2.86E-9, engine.convertAsDouble(AMOUNT, "Kilobyte", "Terabytes"), 0.0);
        assertEquals(2.288E7, engine.convertAsDouble(AMOUNT, "Megabyte", "Bits"), 0.0);
        assertEquals(22880.0, engine.convertAsDouble(AMOUNT, "Megabyte", "Kilobits"), 0.0);
        assertEquals(22.88, engine.convertAsDouble(AMOUNT, "Megabyte", "Megabits"), 0.0);
        assertEquals(0.022879999999999998, engine.convertAsDouble(AMOUNT, "Megabyte", "Gigabits"), 0.0);
        assertEquals(2.2879999999999998E-5, engine.convertAsDouble(AMOUNT, "Megabyte", "Terabits"), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "Megabyte", "Bytes"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Megabyte", "Kilobytes"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Megabyte", "Megabytes"), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Megabyte", "Gigabytes", PLACES), 0.0);
        assertEquals(2.9E-6, engine.convertAsDouble(AMOUNT, "Megabyte", "Terabytes", PLACES), 0.0);
        assertEquals(2.288E10, engine.convertAsDouble(AMOUNT, "Gigabyte", "Bits"), 0.0);
        assertEquals(2.288E7, engine.convertAsDouble(AMOUNT, "Gigabyte", "Kilobits"), 0.0);
        assertEquals(22880.0, engine.convertAsDouble(AMOUNT, "Gigabyte", "Megabits"), 0.0);
        assertEquals(22.88, engine.convertAsDouble(AMOUNT, "Gigabyte", "Gigabits"), 0.0);
        assertEquals(0.02288, engine.convertAsDouble(AMOUNT, "Gigabyte", "Terabits", PLACES), 0.0);
        assertEquals(2.86E9, engine.convertAsDouble(AMOUNT, "Gigabyte", "Bytes"), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "Gigabyte", "Kilobytes"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Gigabyte", "Megabytes"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Gigabyte", "Gigabytes"), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Gigabyte", "Terabytes", PLACES), 0.0);
        assertEquals(2.288E13, engine.convertAsDouble(AMOUNT, "Terabyte", "Bits"), 0.0);
        assertEquals(2.288E10, engine.convertAsDouble(AMOUNT, "Terabyte", "Kilobits"), 0.0);
        assertEquals(2.288E7, engine.convertAsDouble(AMOUNT, "Terabyte", "Megabits"), 0.0);
        assertEquals(22880.0, engine.convertAsDouble(AMOUNT, "Terabyte", "Gigabits"), 0.0);
        assertEquals(22.88, engine.convertAsDouble(AMOUNT, "Terabyte", "Terabits"), 0.0);
        assertEquals(2.86E12, engine.convertAsDouble(AMOUNT, "Terabyte", "Bytes"), 0.0);
        assertEquals(2.86E9, engine.convertAsDouble(AMOUNT, "Terabyte", "Kilobytes"), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "Terabyte", "Megabytes"), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Terabyte", "Gigabytes"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Terabyte", "Terabytes"), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2.86e-9, engine.convertAsDouble(AMOUNT, "b", "gb"), 0.0);
        assertEquals(3.5749999999999997E-7, engine.convertAsDouble(AMOUNT, "bts", "mbs"), 0.0);
        assertEquals(2.288e+7, engine.convertAsDouble(AMOUNT, "mb", "bt"), 0.0);
        assertEquals(2.8599999999999997E-6, engine.convertAsDouble(AMOUNT, "kbs", "gb"), 0.0);
        assertEquals(0.0028599999999999997, engine.convertAsDouble(AMOUNT, "gbs", "tbs"), 0.0);
        assertEquals(2.86e-9, engine.convertAsDouble(AMOUNT, "kb", "tb"), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "gb", "mb");
        assertEquals("digital storage", conversion.getUnitGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConversion() {
        engine.convertAsDouble(AMOUNT, "gigabytes", "kikobits");
    }
}