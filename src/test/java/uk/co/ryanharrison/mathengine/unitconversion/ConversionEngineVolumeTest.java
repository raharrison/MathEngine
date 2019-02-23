package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineVolumeTest extends ConversionEngineTest {

    @Test
    public void testVolume() {
        assertEquals(3527.7581062, engine.convertAsDouble(AMOUNT, "acre foot", "cubic meter", PLACES), 0.0);
        assertEquals(293.9798422, engine.convertAsDouble(AMOUNT, "acre inch", "cubic meter", PLACES), 0.0);
        assertEquals(0.4547037, engine.convertAsDouble(AMOUNT, "barrel", "cubic meter", PLACES), 0.0);
        assertEquals(0.0067488, engine.convertAsDouble(AMOUNT, "board foot", "cubic meter", PLACES), 0.0);
        assertEquals(2.86e-05, engine.convertAsDouble(AMOUNT, "centiliter", "cubic meter", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertAsDouble(AMOUNT, "cubic centimeter", "cubic meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "cubic decimeter", "cubic meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "cubic dekameter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0809862, engine.convertAsDouble(AMOUNT, "cubic foot", "cubic meter", PLACES), 0.0);
        assertEquals(4.69e-05, engine.convertAsDouble(AMOUNT, "cubic inch", "cubic meter", PLACES), 0.0);
        assertEquals(2860000000.0, engine.convertAsDouble(AMOUNT, "cubic kilometer", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "cubic meter", "cubic meter", PLACES), 0.0);
        assertEquals(11921000020.760057, engine.convertAsDouble(AMOUNT, "cubic mile", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "cubic micrometer", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "cubic millimeter", "cubic meter", PLACES), 0.0);
        assertEquals(2.1866269, engine.convertAsDouble(AMOUNT, "cubic yard", "cubic meter", PLACES), 0.0);
        assertEquals(0.000715, engine.convertAsDouble(AMOUNT, "cup", "cubic meter", PLACES), 0.0);
        assertEquals(0.000286, engine.convertAsDouble(AMOUNT, "deciliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0286, engine.convertAsDouble(AMOUNT, "dekaliter", "cubic meter", PLACES), 0.0);
        assertEquals(1.06e-05, engine.convertAsDouble(AMOUNT, "dram", "cubic meter", PLACES), 0.0);
        assertEquals(0.0021653, engine.convertAsDouble(AMOUNT, "fifth", "cubic meter", PLACES), 0.0);
        assertEquals(0.0108263, engine.convertAsDouble(AMOUNT, "gallon", "cubic meter", PLACES), 0.0);
        assertEquals(28600.0, engine.convertAsDouble(AMOUNT, "hectare meter", "cubic meter", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "hectoliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0130018, engine.convertAsDouble(AMOUNT, "imperial gallon", "cubic meter", PLACES), 0.0);
        assertEquals(0.0001269, engine.convertAsDouble(AMOUNT, "jigger", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "kiloliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "liter", "cubic meter", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "megaliter", "cubic meter", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "microliter", "cubic meter", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertAsDouble(AMOUNT, "milliliter", "cubic meter", PLACES), 0.0);
        assertEquals(2e-07, engine.convertAsDouble(AMOUNT, "minim", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertAsDouble(AMOUNT, "ounce", "cubic meter", PLACES), 0.0);
        assertEquals(0.0016252, engine.convertAsDouble(AMOUNT, "pint", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertAsDouble(AMOUNT, "pony", "cubic meter", PLACES), 0.0);
        assertEquals(0.0027066, engine.convertAsDouble(AMOUNT, "quart", "cubic meter", PLACES), 0.0);
        assertEquals(2.86e-05, engine.convertAsDouble(AMOUNT, "Robie", "cubic meter", PLACES), 0.0);
        assertEquals(8.46e-05, engine.convertAsDouble(AMOUNT, "shot", "cubic meter", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "stere", "cubic meter", PLACES), 0.0);
        assertEquals(4.29e-05, engine.convertAsDouble(AMOUNT, "Tablespoon", "cubic meter", PLACES), 0.0);
        assertEquals(1.43e-05, engine.convertAsDouble(AMOUNT, "Teaspoon", "cubic meter", PLACES), 0.0);
        assertEquals(2.1866269, engine.convertAsDouble(AMOUNT, "yard", "cubic meter", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(133436.16, engine.convertAsDouble(AMOUNT, "yd", "in^3", PLACES), 0.0);
        assertEquals(0.0027066, engine.convertAsDouble(AMOUNT, "qts", "m^3", PLACES), 0.0);
        assertEquals(2.9E-6, engine.convertAsDouble(AMOUNT, "ml", "cubic m", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "l", "dcl", PLACES), 0.0);
        assertEquals(755532.0697443, engine.convertAsDouble(AMOUNT, "mgl", "g", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "cubic meter", "gallons");
        assertEquals("volume", conversion.getUnitGroup().getName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertAsDouble(AMOUNT, "teaspoons", "meters");
        });
    }
}