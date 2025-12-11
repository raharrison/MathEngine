package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineTemperatureTest extends ConversionEngineBaseTest {

    @Test
    public void testTemperatures() {
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "celsius", "celsius", PLACES), 0.0);
        assertEquals(37.148, engine.convertToDouble(AMOUNT, "celsius", "fahrenheit", PLACES), 0.0);
        assertEquals(276.01, engine.convertToDouble(AMOUNT, "celsius", "kelvin", PLACES), 0.0);
        assertEquals(496.818, engine.convertToDouble(AMOUNT, "celsius", "rankine", PLACES), 0.0);
        assertEquals(145.71, engine.convertToDouble(AMOUNT, "celsius", "delisle", PLACES), 0.0);
        assertEquals(2.288, engine.convertToDouble(AMOUNT, "celsius", "reaumur", PLACES), 0.0);
        assertEquals(0.9438, engine.convertToDouble(AMOUNT, "celsius", "newton", PLACES), 0.0);
        assertEquals(9.0015, engine.convertToDouble(AMOUNT, "celsius", "romer", PLACES), 0.0);

        assertEquals(-16.1888889, engine.convertToDouble(AMOUNT, "fahrenheit", "celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "fahrenheit", "fahrenheit", PLACES), 0.0);
        assertEquals(256.9611111, engine.convertToDouble(AMOUNT, "fahrenheit", "kelvin", PLACES), 0.0);
        assertEquals(462.53, engine.convertToDouble(AMOUNT, "fahrenheit", "rankine", PLACES), 0.0);
        assertEquals(174.2833333, engine.convertToDouble(AMOUNT, "fahrenheit", "delisle", PLACES), 0.0);
        assertEquals(-12.9511111, engine.convertToDouble(AMOUNT, "fahrenheit", "reaumur", PLACES), 0.0);
        assertEquals(-5.3423333, engine.convertToDouble(AMOUNT, "fahrenheit", "newton", PLACES), 0.0);
        assertEquals(-0.9991667, engine.convertToDouble(AMOUNT, "fahrenheit", "romer", PLACES), 0.0);

        assertEquals(-270.29, engine.convertToDouble(AMOUNT, "kelvin", "celsius", PLACES), 0.0);
        assertEquals(-454.522, engine.convertToDouble(AMOUNT, "kelvin", "fahrenheit", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "kelvin", "kelvin", PLACES), 0.0);
        assertEquals(5.148, engine.convertToDouble(AMOUNT, "kelvin", "rankine", PLACES), 0.0);
        assertEquals(555.435, engine.convertToDouble(AMOUNT, "kelvin", "delisle", PLACES), 0.0);
        assertEquals(-216.232, engine.convertToDouble(AMOUNT, "kelvin", "reaumur", PLACES), 0.0);
        assertEquals(-89.1957, engine.convertToDouble(AMOUNT, "kelvin", "newton", PLACES), 0.0);
        assertEquals(-134.40225, engine.convertToDouble(AMOUNT, "kelvin", "romer", PLACES), 0.0);

        assertEquals(-271.5611111, engine.convertToDouble(AMOUNT, "rankine", "celsius", PLACES), 0.0);
        assertEquals(-456.81, engine.convertToDouble(AMOUNT, "rankine", "fahrenheit", PLACES), 0.0);
        assertEquals(1.5888889, engine.convertToDouble(AMOUNT, "rankine", "kelvin", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "rankine", "rankine", PLACES), 0.0);
        assertEquals(557.3416667, engine.convertToDouble(AMOUNT, "rankine", "delisle", PLACES), 0.0);
        assertEquals(-217.2488889, engine.convertToDouble(AMOUNT, "rankine", "reaumur", PLACES), 0.0);
        assertEquals(-89.6151667, engine.convertToDouble(AMOUNT, "rankine", "newton", PLACES), 0.0);
        assertEquals(-135.0695833, engine.convertToDouble(AMOUNT, "rankine", "romer", PLACES), 0.0);

        assertEquals(98.0933333, engine.convertToDouble(AMOUNT, "delisle", "celsius", PLACES), 0.0);
        assertEquals(208.568, engine.convertToDouble(AMOUNT, "delisle", "fahrenheit", PLACES), 0.0);
        assertEquals(371.2433333, engine.convertToDouble(AMOUNT, "delisle", "kelvin", PLACES), 0.0);
        assertEquals(668.238, engine.convertToDouble(AMOUNT, "delisle", "rankine", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "delisle", "delisle", PLACES), 0.0);
        assertEquals(78.4746667, engine.convertToDouble(AMOUNT, "delisle", "reaumur", PLACES), 0.0);
        assertEquals(32.3708, engine.convertToDouble(AMOUNT, "delisle", "newton", PLACES), 0.0);
        assertEquals(58.999, engine.convertToDouble(AMOUNT, "delisle", "romer", PLACES), 0.0);

        assertEquals(3.575, engine.convertToDouble(AMOUNT, "reaumur", "celsius", PLACES), 0.0);
        assertEquals(38.435, engine.convertToDouble(AMOUNT, "reaumur", "fahrenheit", PLACES), 0.0);
        assertEquals(276.725, engine.convertToDouble(AMOUNT, "reaumur", "kelvin", PLACES), 0.0);
        assertEquals(498.105, engine.convertToDouble(AMOUNT, "reaumur", "rankine", PLACES), 0.0);
        assertEquals(144.6375, engine.convertToDouble(AMOUNT, "reaumur", "delisle", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "reaumur", "reaumur", PLACES), 0.0);
        assertEquals(1.17975, engine.convertToDouble(AMOUNT, "reaumur", "newton", PLACES), 0.0);
        assertEquals(9.376875, engine.convertToDouble(AMOUNT, "reaumur", "romer", PLACES), 0.0);

        assertEquals(8.6666667, engine.convertToDouble(AMOUNT, "newton", "celsius", PLACES), 0.0);
        assertEquals(47.6, engine.convertToDouble(AMOUNT, "newton", "fahrenheit", PLACES), 0.0);
        assertEquals(281.8166667, engine.convertToDouble(AMOUNT, "newton", "kelvin", PLACES), 0.0);
        assertEquals(507.27, engine.convertToDouble(AMOUNT, "newton", "rankine", PLACES), 0.0);
        assertEquals(137.0, engine.convertToDouble(AMOUNT, "newton", "delisle", PLACES), 0.0);
        assertEquals(6.9333333, engine.convertToDouble(AMOUNT, "newton", "reaumur", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "newton", "newton", PLACES), 0.0);
        assertEquals(12.05, engine.convertToDouble(AMOUNT, "newton", "romer", PLACES), 0.0);

        assertEquals(-8.8380952, engine.convertToDouble(AMOUNT, "romer", "celsius", PLACES), 0.0);
        assertEquals(16.0914286, engine.convertToDouble(AMOUNT, "romer", "fahrenheit", PLACES), 0.0);
        assertEquals(264.3119048, engine.convertToDouble(AMOUNT, "romer", "kelvin", PLACES), 0.0);
        assertEquals(475.7614286, engine.convertToDouble(AMOUNT, "romer", "rankine", PLACES), 0.0);
        assertEquals(163.2571429, engine.convertToDouble(AMOUNT, "romer", "delisle", PLACES), 0.0);
        assertEquals(-7.0704762, engine.convertToDouble(AMOUNT, "romer", "reaumur", PLACES), 0.0);
        assertEquals(-2.9165714, engine.convertToDouble(AMOUNT, "romer", "newton", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "romer", "romer", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "degree Celsius", "degrees Celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "c", "c", PLACES), 0.0);
        assertEquals(276.01, engine.convertToDouble(AMOUNT, "degree Celsius", "degrees Kelvin", PLACES), 0.0);
        assertEquals(276.01, engine.convertToDouble(AMOUNT, "c", "k", PLACES), 0.0);
        assertEquals(37.148, engine.convertToDouble(AMOUNT, "degree Celsius", "degrees Fahrenheit", PLACES), 0.0);
        assertEquals(37.148, engine.convertToDouble(AMOUNT, "c", "f", PLACES), 0.0);
        assertEquals(-270.29, engine.convertToDouble(AMOUNT, "degree Kelvin", "degrees Celsius", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "degree Kelvin", "degrees Kelvin", PLACES), 0.0);
        assertEquals(-454.522, engine.convertToDouble(AMOUNT, "degree Kelvin", "degrees Fahrenheit", PLACES), 0.0);
        assertEquals(-16.1888889, engine.convertToDouble(AMOUNT, "degree Fahrenheit", "degrees Celsius", PLACES), 0.0);
        assertEquals(256.9611111, engine.convertToDouble(AMOUNT, "degree Fahrenheit", "degrees Kelvin", PLACES), 0.0);
        assertEquals(2.86, engine.convertToDouble(AMOUNT, "degree Fahrenheit", "degrees Fahrenheit", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        ConversionResult conversion = engine.convert(AMOUNT, "celsius", "fahrenheit");
        assertEquals("temperature", conversion.groupName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertToDouble(AMOUNT, "celcius", "fahrenheit");
        });
    }
}