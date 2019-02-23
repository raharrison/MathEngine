package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConversionEngineEnergyTest extends ConversionEngineTest {

    @Test
    public void testEnergy() {
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "attojoule", "joule", PLACES), 0.0);
        assertEquals(10296000.0, engine.convertAsDouble(AMOUNT, "Board of Trade unit", "joule", PLACES), 0.0);
        assertEquals(3017.459874, engine.convertAsDouble(AMOUNT, "Btu", "joule", PLACES), 0.0);
        assertEquals(11.974248, engine.convertAsDouble(AMOUNT, "calories", "joule", PLACES), 0.0);
        assertEquals(5431.42743, engine.convertAsDouble(AMOUNT, "celsius heat unit", "joule", PLACES), 0.0);
        assertEquals(0.0286, engine.convertAsDouble(AMOUNT, "centijoule", "joule", PLACES), 0.0);
        assertEquals(7572695.13, engine.convertAsDouble(AMOUNT, "cheval vapeur heure", "joule", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "decijoule", "joule", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "dekajoule", "joule", PLACES), 0.0);
        assertEquals(102960.0, engine.convertAsDouble(AMOUNT, "dekawatt hour", "joule", PLACES), 0.0);
        assertEquals(3017463020.0, engine.convertAsDouble(AMOUNT, "dekatherm", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "electronvolt", "joule", PLACES), 0.0);
        assertEquals(3e-07, engine.convertAsDouble(AMOUNT, "erg", "joule", PLACES), 0.0);
        assertEquals(2.86e+18, engine.convertAsDouble(AMOUNT, "exajoule", "joule", PLACES), 0.0);
        assertEquals(1.0296e+22, engine.convertAsDouble(AMOUNT, "exawatt hour", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "femtojoule", "joule", PLACES), 0.0);
        assertEquals(3.8776392, engine.convertAsDouble(AMOUNT, "foot pound", "joule", PLACES), 0.0);
        assertEquals(0.1205207, engine.convertAsDouble(AMOUNT, "foot poundal", "joule", PLACES), 0.0);
        assertEquals(376833600.0, engine.convertAsDouble(AMOUNT, "gallons", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "gigaelectronvolt", "joule", PLACES), 0.0);
        assertEquals(11974248000.0, engine.convertAsDouble(AMOUNT, "gigacalorie", "joule", PLACES), 0.0);
        assertEquals(2860000000.0, engine.convertAsDouble(AMOUNT, "gigajoule", "joule", PLACES), 0.0);
        assertEquals(10296000000000.0, engine.convertAsDouble(AMOUNT, "gigawatt hour", "joule", PLACES), 0.0);
        assertEquals(11.971388, engine.convertAsDouble(AMOUNT, "gram calorie", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "hartree", "joule", PLACES), 0.0);
        assertEquals(286.0, engine.convertAsDouble(AMOUNT, "hectojoule", "joule", PLACES), 0.0);
        assertEquals(1029600.0, engine.convertAsDouble(AMOUNT, "hectowatt hour", "joule", PLACES), 0.0);
        assertEquals(7677725.77, engine.convertAsDouble(AMOUNT, "horsepower hour", "joule", PLACES), 0.0);
        assertEquals(310939200.0, engine.convertAsDouble(AMOUNT, "hundred cubic foot of natural gas", "joule", PLACES), 0.0);
        assertEquals(0.020196, engine.convertAsDouble(AMOUNT, "inch ounce", "joule", PLACES), 0.0);
        assertEquals(0.3231366, engine.convertAsDouble(AMOUNT, "inch pound", "joule", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "joule", "joule", PLACES), 0.0);
        assertEquals(11974.248, engine.convertAsDouble(AMOUNT, "kilocalorie", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "kiloelectronvolt", "joule", PLACES), 0.0);
        assertEquals(11971.388, engine.convertAsDouble(AMOUNT, "kilogram calorie", "joule", PLACES), 0.0);
        assertEquals(28.047019, engine.convertAsDouble(AMOUNT, "kilogram force meter", "joule", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "kilojoule", "joule", PLACES), 0.0);
        assertEquals(28.047019, engine.convertAsDouble(AMOUNT, "kilopond meter", "joule", PLACES), 0.0);
        assertEquals(11966240000000.0, engine.convertAsDouble(AMOUNT, "kiloton", "joule", PLACES), 0.0);
        assertEquals(10296000.0, engine.convertAsDouble(AMOUNT, "kilowatt hour", "joule", PLACES), 0.0);
        assertEquals(289.7895, engine.convertAsDouble(AMOUNT, "liter atmosphere", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "megaelectronvolt", "joule", PLACES), 0.0);
        assertEquals(11974248.0, engine.convertAsDouble(AMOUNT, "megacalorie", "joule", PLACES), 0.0);
        assertEquals(2860000.0, engine.convertAsDouble(AMOUNT, "megajoule", "joule", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "megalerg", "joule", PLACES), 0.0);
        assertEquals(1.196624e+16, engine.convertAsDouble(AMOUNT, "megaton", "joule", PLACES), 0.0);
        assertEquals(10296000000.0, engine.convertAsDouble(AMOUNT, "megawatthour", "joule", PLACES), 0.0);
        assertEquals(28.047019, engine.convertAsDouble(AMOUNT, "meter kilogram force", "joule", PLACES), 0.0);
        assertEquals(2.9e-06, engine.convertAsDouble(AMOUNT, "microjoule", "joule", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "millijoule", "joule", PLACES), 0.0);
        assertEquals(102960000.0, engine.convertAsDouble(AMOUNT, "myriawatt hour", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "nanojoule", "joule", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "newton meter", "joule", PLACES), 0.0);
        assertEquals(2860000000000000.0, engine.convertAsDouble(AMOUNT, "petajoule", "joule", PLACES), 0.0);
        assertEquals(1.0296e+19, engine.convertAsDouble(AMOUNT, "petawatthour", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "picojoule", "joule", PLACES), 0.0);
        assertEquals(3.0174598739999997e+21, engine.convertAsDouble(AMOUNT, "Q unit", "joule", PLACES), 0.0);
        assertEquals(3.017459874e+18, engine.convertAsDouble(AMOUNT, "quad", "joule", PLACES), 0.0);
        assertEquals(5e-07, engine.convertAsDouble(AMOUNT, "teraelectronvolt", "joule", PLACES), 0.0);
        assertEquals(2860000000000.0, engine.convertAsDouble(AMOUNT, "terajoule", "joule", PLACES), 0.0);
        assertEquals(1.0296e+16, engine.convertAsDouble(AMOUNT, "terawatthour", "joule", PLACES), 0.0);
        assertEquals(11971388.0, engine.convertAsDouble(AMOUNT, "thermie", "joule", PLACES), 0.0);
        assertEquals(11966240000.0, engine.convertAsDouble(AMOUNT, "ton", "joule", PLACES), 0.0);
        assertEquals(83819736000.0, engine.convertAsDouble(AMOUNT, "tonne of coal", "joule", PLACES), 0.0);
        assertEquals(119742480000.0, engine.convertAsDouble(AMOUNT, "tonne of oil", "joule", PLACES), 0.0);
        assertEquals(10296.0, engine.convertAsDouble(AMOUNT, "watthour", "joule", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "wattsecond", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "yoctojoule", "joule", PLACES), 0.0);
        assertEquals(2.86e+24, engine.convertAsDouble(AMOUNT, "yottajoule", "joule", PLACES), 0.0);
        assertEquals(1.0295999999999999e+28, engine.convertAsDouble(AMOUNT, "yottawatthour", "joule", PLACES), 0.0);
        assertEquals(0.0, engine.convertAsDouble(AMOUNT, "zeptojoule", "joule", PLACES), 0.0);
        assertEquals(2.86e+21, engine.convertAsDouble(AMOUNT, "zettajoule", "joule", PLACES), 0.0);
        assertEquals(1.0296e+25, engine.convertAsDouble(AMOUNT, "zettawatthour", "joule", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "wh", "mlwh", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "j", "mlj", PLACES), 0.0);
        assertEquals(1.0296E19, engine.convertAsDouble(AMOUNT, "mwh", "nj", PLACES), 0.0);
        assertEquals(2.9E-6, engine.convertAsDouble(AMOUNT, "nm", "mj", PLACES), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "calories", "joules");
        assertEquals("energy", conversion.getUnitGroup().getName());
    }

    @Test
    public void testInvalidConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            engine.convertAsDouble(AMOUNT, "gigajoule", "watts");
        });
    }
}