package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineVolumeTest extends ConversionEngineBaseTest {

    @Test
    public void testVolume() {
        assertEquals(3527.7581062, engine.convertToDouble(AMOUNT, "acre foot", "cubic meter", PLACES), 0.0);
        assertEquals(293.9798422, engine.convertToDouble(AMOUNT, "acre inch", "cubic meter", PLACES), 0.0);
        assertEquals(0.4547037, engine.convertToDouble(AMOUNT, "barrel", "cubic meter", PLACES), 0.0);
        assertEquals(0.0067488, engine.convertToDouble(AMOUNT, "board foot", "cubic meter", PLACES), 0.0);
        assertEquals(2.86e-05, engine.convertToDouble(AMOUNT, "centiliter", "cubic meter", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertToDouble(AMOUNT, "cubic centimeter", "cubic meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "cubic decimeter", "cubic meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "cubic dekameter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0809862, engine.convertToDouble(AMOUNT, "cubic foot", "cubic meter", PLACES), 0.0);
        assertEquals(4.69e-05, engine.convertToDouble(AMOUNT, "cubic inch", "cubic meter", PLACES), 0.0);
        assertEquals(2860000000.0, engine.convertToDouble(AMOUNT, "cubic kilometer", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "cubic meter", "cubic meter", PLACES), 0.0);
        assertEquals(11921000020.760057, engine.convertToDouble(AMOUNT, "cubic mile", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertToDouble(AMOUNT, "cubic micrometer", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertToDouble(AMOUNT, "cubic millimeter", "cubic meter", PLACES), 0.0);
        assertEquals(2.1866269, engine.convertToDouble(AMOUNT, "cubic yard", "cubic meter", PLACES), 0.0);
        assertEquals(0.000715, engine.convertToDouble(AMOUNT, "cup", "cubic meter", PLACES), 0.0);
        assertEquals(0.000286, engine.convertToDouble(AMOUNT, "deciliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0286, engine.convertToDouble(AMOUNT, "dekaliter", "cubic meter", PLACES), 0.0);
        assertEquals(1.06e-05, engine.convertToDouble(AMOUNT, "dram", "cubic meter", PLACES), 0.0);
        assertEquals(0.0021653, engine.convertToDouble(AMOUNT, "fifth", "cubic meter", PLACES), 0.0);
        assertEquals(0.0108263, engine.convertToDouble(AMOUNT, "gallon", "cubic meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertToDouble(AMOUNT, "hectare meter", "cubic meter", PLACES), 0.0);
        assertEquals(0.286, engine.convertToDouble(AMOUNT, "hectoliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0130018, engine.convertToDouble(AMOUNT, "imperial gallon", "cubic meter", PLACES), 0.0);
        assertEquals(0.0001269, engine.convertToDouble(AMOUNT, "jigger", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "kiloliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertToDouble(AMOUNT, "liter", "cubic meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertToDouble(AMOUNT, "megaliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertToDouble(AMOUNT, "microliter", "cubic meter", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertToDouble(AMOUNT, "milliliter", "cubic meter", PLACES), 0.0);
        assertEquals(2e-07, engine.convertToDouble(AMOUNT, "minim", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertToDouble(AMOUNT, "ounce", "cubic meter", PLACES), 0.0);
        assertEquals(0.0016252, engine.convertToDouble(AMOUNT, "pint", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertToDouble(AMOUNT, "pony", "cubic meter", PLACES), 0.0);
        assertEquals(0.0027066, engine.convertToDouble(AMOUNT, "quart", "cubic meter", PLACES), 0.0);
        assertEquals(2.86e-05, engine.convertToDouble(AMOUNT, "Robie", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertToDouble(AMOUNT, "shot", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "stere", "cubic meter", PLACES), 0.0);
        assertEquals(4.29e-05, engine.convertToDouble(AMOUNT, "Tablespoon", "cubic meter", PLACES), 0.0);
        assertEquals(1.43e-05, engine.convertToDouble(AMOUNT, "Teaspoon", "cubic meter", PLACES), 0.0);
        assertEquals(2.1866269, engine.convertToDouble(AMOUNT, "yard", "cubic meter", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(133436.16, engine.convertToDouble(AMOUNT, "yd", "in^3", PLACES), 0.0);
        assertEquals(0.0027066, engine.convertToDouble(AMOUNT, "qts", "m^3", PLACES), 0.0);
        assertEquals(2.9E-6, engine.convertToDouble(AMOUNT, "ml", "cubic m", PLACES), 0.0);
        assertEquals(28.6, engine.convertToDouble(AMOUNT, "l", "dcl", PLACES), 0.0);
        assertEquals(755532.0697443, engine.convertToDouble(AMOUNT, "mgl", "g", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        ConversionResult conversion = engine.convert(AMOUNT, "cubic meter", "gallons");
        assertEquals("volume", conversion.groupName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertToDouble(AMOUNT, "teaspoons", "meters");
        });
    }
}