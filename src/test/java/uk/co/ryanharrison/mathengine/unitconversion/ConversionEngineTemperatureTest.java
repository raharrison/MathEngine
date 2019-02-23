package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineTemperatureTest extends ConversionEngineTest {

    @Test
    public void testTemperatures() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "celsius", "celsius", PLACES), 0.0);
        assertEquals(37.148, engine.convertAsDouble(AMOUNT, "celsius", "fahrenheit", PLACES), 0.0);
        assertEquals(276.01, engine.convertAsDouble(AMOUNT, "celsius", "kelvin", PLACES), 0.0);
        assertEquals(496.818, engine.convertAsDouble(AMOUNT, "celsius", "rankine", PLACES), 0.0);
        assertEquals(145.71, engine.convertAsDouble(AMOUNT, "celsius", "delisle", PLACES), 0.0);
        assertEquals(2.288, engine.convertAsDouble(AMOUNT, "celsius", "reaumur", PLACES), 0.0);
        assertEquals(0.9438, engine.convertAsDouble(AMOUNT, "celsius", "newton", PLACES), 0.0);
        assertEquals(9.0015, engine.convertAsDouble(AMOUNT, "celsius", "romer", PLACES), 0.0);

        assertEquals(-16.1888889, engine.convertAsDouble(AMOUNT, "fahrenheit", "celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "fahrenheit", "fahrenheit", PLACES), 0.0);
        assertEquals(256.9611111, engine.convertAsDouble(AMOUNT, "fahrenheit", "kelvin", PLACES), 0.0);
        assertEquals(462.53, engine.convertAsDouble(AMOUNT, "fahrenheit", "rankine", PLACES), 0.0);
        assertEquals(174.2833333, engine.convertAsDouble(AMOUNT, "fahrenheit", "delisle", PLACES), 0.0);
        assertEquals(-12.9511111, engine.convertAsDouble(AMOUNT, "fahrenheit", "reaumur", PLACES), 0.0);
        assertEquals(-5.3423333, engine.convertAsDouble(AMOUNT, "fahrenheit", "newton", PLACES), 0.0);
        assertEquals(-0.9991667, engine.convertAsDouble(AMOUNT, "fahrenheit", "romer", PLACES), 0.0);

        assertEquals(-270.29, engine.convertAsDouble(AMOUNT, "kelvin", "celsius", PLACES), 0.0);
        assertEquals(-454.522, engine.convertAsDouble(AMOUNT, "kelvin", "fahrenheit", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "kelvin", "kelvin", PLACES), 0.0);
        assertEquals(5.148, engine.convertAsDouble(AMOUNT, "kelvin", "rankine", PLACES), 0.0);
        assertEquals(555.435, engine.convertAsDouble(AMOUNT, "kelvin", "delisle", PLACES), 0.0);
        assertEquals(-216.232, engine.convertAsDouble(AMOUNT, "kelvin", "reaumur", PLACES), 0.0);
        assertEquals(-89.1957, engine.convertAsDouble(AMOUNT, "kelvin", "newton", PLACES), 0.0);
        assertEquals(-134.40225, engine.convertAsDouble(AMOUNT, "kelvin", "romer", PLACES), 0.0);

        assertEquals(-271.5611111, engine.convertAsDouble(AMOUNT, "rankine", "celsius", PLACES), 0.0);
        assertEquals(-456.81, engine.convertAsDouble(AMOUNT, "rankine", "fahrenheit", PLACES), 0.0);
        assertEquals(1.5888889, engine.convertAsDouble(AMOUNT, "rankine", "kelvin", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "rankine", "rankine", PLACES), 0.0);
        assertEquals(557.3416667, engine.convertAsDouble(AMOUNT, "rankine", "delisle", PLACES), 0.0);
        assertEquals(-217.2488889, engine.convertAsDouble(AMOUNT, "rankine", "reaumur", PLACES), 0.0);
        assertEquals(-89.6151667, engine.convertAsDouble(AMOUNT, "rankine", "newton", PLACES), 0.0);
        assertEquals(-135.0695833, engine.convertAsDouble(AMOUNT, "rankine", "romer", PLACES), 0.0);

        assertEquals(98.0933333, engine.convertAsDouble(AMOUNT, "delisle", "celsius", PLACES), 0.0);
        assertEquals(208.568, engine.convertAsDouble(AMOUNT, "delisle", "fahrenheit", PLACES), 0.0);
        assertEquals(371.2433333, engine.convertAsDouble(AMOUNT, "delisle", "kelvin", PLACES), 0.0);
        assertEquals(668.238, engine.convertAsDouble(AMOUNT, "delisle", "rankine", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "delisle", "delisle", PLACES), 0.0);
        assertEquals(78.4746667, engine.convertAsDouble(AMOUNT, "delisle", "reaumur", PLACES), 0.0);
        assertEquals(32.3708, engine.convertAsDouble(AMOUNT, "delisle", "newton", PLACES), 0.0);
        assertEquals(58.999, engine.convertAsDouble(AMOUNT, "delisle", "romer", PLACES), 0.0);

        assertEquals(3.575, engine.convertAsDouble(AMOUNT, "reaumur", "celsius", PLACES), 0.0);
        assertEquals(38.435, engine.convertAsDouble(AMOUNT, "reaumur", "fahrenheit", PLACES), 0.0);
        assertEquals(276.725, engine.convertAsDouble(AMOUNT, "reaumur", "kelvin", PLACES), 0.0);
        assertEquals(498.105, engine.convertAsDouble(AMOUNT, "reaumur", "rankine", PLACES), 0.0);
        assertEquals(144.6375, engine.convertAsDouble(AMOUNT, "reaumur", "delisle", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "reaumur", "reaumur", PLACES), 0.0);
        assertEquals(1.17975, engine.convertAsDouble(AMOUNT, "reaumur", "newton", PLACES), 0.0);
        assertEquals(9.376875, engine.convertAsDouble(AMOUNT, "reaumur", "romer", PLACES), 0.0);

        assertEquals(8.6666667, engine.convertAsDouble(AMOUNT, "newton", "celsius", PLACES), 0.0);
        assertEquals(47.6, engine.convertAsDouble(AMOUNT, "newton", "fahrenheit", PLACES), 0.0);
        assertEquals(281.8166667, engine.convertAsDouble(AMOUNT, "newton", "kelvin", PLACES), 0.0);
        assertEquals(507.27, engine.convertAsDouble(AMOUNT, "newton", "rankine", PLACES), 0.0);
        assertEquals(137.0, engine.convertAsDouble(AMOUNT, "newton", "delisle", PLACES), 0.0);
        assertEquals(6.9333333, engine.convertAsDouble(AMOUNT, "newton", "reaumur", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "newton", "newton", PLACES), 0.0);
        assertEquals(12.05, engine.convertAsDouble(AMOUNT, "newton", "romer", PLACES), 0.0);

        assertEquals(-8.8380952, engine.convertAsDouble(AMOUNT, "romer", "celsius", PLACES), 0.0);
        assertEquals(16.0914286, engine.convertAsDouble(AMOUNT, "romer", "fahrenheit", PLACES), 0.0);
        assertEquals(264.3119048, engine.convertAsDouble(AMOUNT, "romer", "kelvin", PLACES), 0.0);
        assertEquals(475.7614286, engine.convertAsDouble(AMOUNT, "romer", "rankine", PLACES), 0.0);
        assertEquals(163.2571429, engine.convertAsDouble(AMOUNT, "romer", "delisle", PLACES), 0.0);
        assertEquals(-7.0704762, engine.convertAsDouble(AMOUNT, "romer", "reaumur", PLACES), 0.0);
        assertEquals(-2.9165714, engine.convertAsDouble(AMOUNT, "romer", "newton", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "romer", "romer", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "degree Celsius", "degrees Celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "c", "c", PLACES), 0.0);
        assertEquals(276.01, engine.convertAsDouble(AMOUNT, "degree Celsius", "degrees Kelvin", PLACES), 0.0);
        assertEquals(276.01, engine.convertAsDouble(AMOUNT, "c", "k", PLACES), 0.0);
        assertEquals(37.148, engine.convertAsDouble(AMOUNT, "degree Celsius", "degrees Fahrenheit", PLACES), 0.0);
        assertEquals(37.148, engine.convertAsDouble(AMOUNT, "c", "f", PLACES), 0.0);
        assertEquals(-270.29, engine.convertAsDouble(AMOUNT, "degree Kelvin", "degrees Celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "degree Kelvin", "degrees Kelvin", PLACES), 0.0);
        assertEquals(-454.522, engine.convertAsDouble(AMOUNT, "degree Kelvin", "degrees Fahrenheit", PLACES), 0.0);
        assertEquals(-16.1888889, engine.convertAsDouble(AMOUNT, "degree Fahrenheit", "degrees Celsius", PLACES), 0.0);
        assertEquals(256.9611111, engine.convertAsDouble(AMOUNT, "degree Fahrenheit", "degrees Kelvin", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "degree Fahrenheit", "degrees Fahrenheit", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "celsius", "fahrenheit");
        assertEquals("temperature", conversion.getUnitGroup().getName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertAsDouble(AMOUNT, "celcius", "fahrenheit");
        });
    }
}