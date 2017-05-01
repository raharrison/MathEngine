package uk.co.ryanharrison.mathengine.unitconversion;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;

import static org.junit.Assert.assertEquals;

public class ConversionEngineTimeTest extends ConversionEngineTest {

    @Test
    public void testTimes() {
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "century", "century", PLACES), 0.0);
        assertEquals(104390.0, engine.convertAsDouble(AMOUNT, "century", "day", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "century", "decade", PLACES), 0.0);
        assertEquals(7456.4285714, engine.convertAsDouble(AMOUNT, "century", "fortnight", PLACES), 0.0);
        assertEquals(2505360.0, engine.convertAsDouble(AMOUNT, "century", "hour", PLACES), 0.0);
        assertEquals(1.503216E8, engine.convertAsDouble(AMOUNT, "century", "minute", PLACES), 0.0);
        assertEquals(9.019296E9, engine.convertAsDouble(AMOUNT, "century", "second", PLACES), 0.0);
        assertEquals(14912.8571429, engine.convertAsDouble(AMOUNT, "century", "week", PLACES), 0.0);
        assertEquals(286, engine.convertAsDouble(AMOUNT, "century", "year", PLACES), 0.0);
        assertEquals(7.835616438356165E-5, engine.convertAsDouble(AMOUNT, "day", "century"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "day", "day", PLACES), 0.0);
        assertEquals(7.835616438356164E-4, engine.convertAsDouble(AMOUNT, "day", "decade"), 0.0);
        assertEquals(0.2042857, engine.convertAsDouble(AMOUNT, "day", "fortnight", PLACES), 0.0);
        assertEquals(68.64, engine.convertAsDouble(AMOUNT, "day", "hour", PLACES), 0.0);
        assertEquals(4118.4, engine.convertAsDouble(AMOUNT, "day", "minute", PLACES), 0.0);
        assertEquals(247104, engine.convertAsDouble(AMOUNT, "day", "second", PLACES), 0.0);
        assertEquals(0.4085714, engine.convertAsDouble(AMOUNT, "day", "week", PLACES), 0.0);
        assertEquals(0.0078356, engine.convertAsDouble(AMOUNT, "day", "year", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "decade", "century", PLACES), 0.0);
        assertEquals(10439.0, engine.convertAsDouble(AMOUNT, "decade", "day", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "decade", "decade", PLACES), 0.0);
        assertEquals(745.6428571, engine.convertAsDouble(AMOUNT, "decade", "fortnight", PLACES), 0.0);
        assertEquals(250536.0, engine.convertAsDouble(AMOUNT, "decade", "hour", PLACES), 0.0);
        assertEquals(1.503216E7, engine.convertAsDouble(AMOUNT, "decade", "minute", PLACES), 0.0);
        assertEquals(9.019296E8, engine.convertAsDouble(AMOUNT, "decade", "second", PLACES), 0.0);
        assertEquals(1491.2857143, engine.convertAsDouble(AMOUNT, "decade", "week", PLACES), 0.0);
        assertEquals(28.6, engine.convertAsDouble(AMOUNT, "decade", "year", PLACES), 0.0);
        assertEquals(0.001097, engine.convertAsDouble(AMOUNT, "fortnight", "century", PLACES), 0.0);
        assertEquals(40.04, engine.convertAsDouble(AMOUNT, "fortnight", "day", PLACES), 0.0);
        assertEquals(0.0109699, engine.convertAsDouble(AMOUNT, "fortnight", "decade", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "fortnight", "fortnight", PLACES), 0.0);
        assertEquals(960.96, engine.convertAsDouble(AMOUNT, "fortnight", "hour", PLACES), 0.0);
        assertEquals(57657.6, engine.convertAsDouble(AMOUNT, "fortnight", "minute", PLACES), 0.0);
        assertEquals(3459456, engine.convertAsDouble(AMOUNT, "fortnight", "second", PLACES), 0.0);
        assertEquals(5.72, engine.convertAsDouble(AMOUNT, "fortnight", "week", PLACES), 0.0);
        assertEquals(0.1096986, engine.convertAsDouble(AMOUNT, "fortnight", "year", PLACES), 0.0);
        assertEquals(3.3E-6, engine.convertAsDouble(AMOUNT, "hour", "century", PLACES), 0.0);
        assertEquals(0.11916666666666666, engine.convertAsDouble(AMOUNT, "hour", "day"), 0.0);
        assertEquals(3.26E-5, engine.convertAsDouble(AMOUNT, "hour", "decade", PLACES), 0.0);
        assertEquals(0.0085119, engine.convertAsDouble(AMOUNT, "hour", "fortnight", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "hour", "hour", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "hour", "minute", PLACES), 0.0);
        assertEquals(10296, engine.convertAsDouble(AMOUNT, "hour", "second", PLACES), 0.0);
        assertEquals(0.0170238, engine.convertAsDouble(AMOUNT, "hour", "week", PLACES), 0.0);
        assertEquals(3.2648401826484015E-4, engine.convertAsDouble(AMOUNT, "hour", "year"), 0.0);
        assertEquals(5.441400304414003E-8, engine.convertAsDouble(AMOUNT, "minute", "century"), 0.0);
        assertEquals(0.0019861111111111112, engine.convertAsDouble(AMOUNT, "minute", "day"), 0.0);
        assertEquals(5.441400304414003E-7, engine.convertAsDouble(AMOUNT, "minute", "decade"), 0.0);
        assertEquals(0.00014186507936507937, engine.convertAsDouble(AMOUNT, "minute", "fortnight"), 0.0);
        assertEquals(0.04766666666666666, engine.convertAsDouble(AMOUNT, "minute", "hour"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "minute", "minute", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "minute", "second", PLACES), 0.0);
        assertEquals(0.00028373015873015873, engine.convertAsDouble(AMOUNT, "minute", "week"), 0.0);
        assertEquals(5.441400304414003E-6, engine.convertAsDouble(AMOUNT, "minute", "year"), 0.0);
        assertEquals(9.069000507356671E-10, engine.convertAsDouble(AMOUNT, "second", "century"), 0.0);
        assertEquals(0.00003310185185185185, engine.convertAsDouble(AMOUNT, "second", "day"), 0.0);
        assertEquals(9.069000507356672E-9, engine.convertAsDouble(AMOUNT, "second", "decade"), 0.0);
        assertEquals(0.0000023644179894179893, engine.convertAsDouble(AMOUNT, "second", "fortnight"), 0.0);
        assertEquals(0.0007944444444444444, engine.convertAsDouble(AMOUNT, "second", "hour"), 0.0);
        assertEquals(0.04766666666666666, engine.convertAsDouble(AMOUNT, "second", "minute"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "second", "second", PLACES), 0.0);
        assertEquals(0.0000047288359788359785, engine.convertAsDouble(AMOUNT, "second", "week"), 0.0);
        assertEquals(9.069000507356671E-8, engine.convertAsDouble(AMOUNT, "second", "year"), 0.0);
        assertEquals(5.484931506849315E-4, engine.convertAsDouble(AMOUNT, "week", "century"), 0.0);
        assertEquals(20.02, engine.convertAsDouble(AMOUNT, "week", "day", PLACES), 0.0);
        assertEquals(0.005484931506849315, engine.convertAsDouble(AMOUNT, "week", "decade"), 0.0);
        assertEquals(1.43, engine.convertAsDouble(AMOUNT, "week", "fortnight"), 0.0);
        assertEquals(480.48, engine.convertAsDouble(AMOUNT, "week", "hour", PLACES), 0.0);
        assertEquals(28828.8, engine.convertAsDouble(AMOUNT, "week", "minute", PLACES), 0.0);
        assertEquals(1729728, engine.convertAsDouble(AMOUNT, "week", "second", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "week", "week", PLACES), 0.0);
        assertEquals(0.05484931506849315, engine.convertAsDouble(AMOUNT, "week", "year"), 0.0);
        assertEquals(0.0286, engine.convertAsDouble(AMOUNT, "year", "century"), 0.0);
        assertEquals(1043.9, engine.convertAsDouble(AMOUNT, "year", "day", PLACES), 0.0);
        assertEquals(0.286, engine.convertAsDouble(AMOUNT, "year", "decade"), 0.0);
        assertEquals(74.5642857142857, engine.convertAsDouble(AMOUNT, "year", "fortnight"), 0.0);
        assertEquals(25053.6, engine.convertAsDouble(AMOUNT, "year", "hour"), 0.0);
        assertEquals(1503216.0, engine.convertAsDouble(AMOUNT, "year", "minute"), 0.0);
        assertEquals(9.019296E7, engine.convertAsDouble(AMOUNT, "year", "second"), 0.0);
        assertEquals(149.1285714285714, engine.convertAsDouble(AMOUNT, "year", "week"), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "year", "year"), 0.0);

        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Nanosecond", "Nanoseconds", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Nanosecond", "Microseconds", PLACES), 0.0);
        assertEquals(0.00000286, engine.convertAsDouble(AMOUNT, "Nanosecond", "Milliseconds"), 0.0);
        assertEquals(2.86e-9, engine.convertAsDouble(AMOUNT, "Nanosecond", "Seconds", PLACES), 0.0);
        assertEquals(4.766666666666667e-11, engine.convertAsDouble(AMOUNT, "Nanosecond", "Minutes", PLACES), 0.0);
        assertEquals(7.944444444444444e-13, engine.convertAsDouble(AMOUNT, "Nanosecond", "Hours", PLACES), 0.0);
        assertEquals(3.310185185185185e-14, engine.convertAsDouble(AMOUNT, "Nanosecond", "Days", PLACES), 0.0);
        assertEquals(4.728835978835979e-15, engine.convertAsDouble(AMOUNT, "Nanosecond", "Weeks", PLACES), 0.0);
        assertEquals(1.0882800608828006E-15, engine.convertAsDouble(AMOUNT, "Nanosecond", "Months", PLACES), 0.0);
        assertEquals(9.069000507356671E-17, engine.convertAsDouble(AMOUNT, "Nanosecond", "Years", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Microsecond", "Nanoseconds", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Microsecond", "Microseconds", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Microsecond", "Milliseconds", PLACES), 0.0);
        assertEquals(0.0000028599999999999997, engine.convertAsDouble(AMOUNT, "Microsecond", "Seconds"), 0.0);
        assertEquals(4.7666666666666663e-8, engine.convertAsDouble(AMOUNT, "Microsecond", "Minutes", PLACES), 0.0);
        assertEquals(7.944444444444445E-10, engine.convertAsDouble(AMOUNT, "Microsecond", "Hours"), 0.0);
        assertEquals(3.310185185185185E-11, engine.convertAsDouble(AMOUNT, "Microsecond", "Days", PLACES), 0.0);
        assertEquals(4.7288359788359786e-12, engine.convertAsDouble(AMOUNT, "Microsecond", "Weeks", PLACES), 0.0);
        assertEquals(1.0882800608828006E-12, engine.convertAsDouble(AMOUNT, "Microsecond", "Months", PLACES), 0.0);
        assertEquals(9.069000507356672E-14, engine.convertAsDouble(AMOUNT, "Microsecond", "Years", PLACES), 0.0);
        assertEquals(2860000, engine.convertAsDouble(AMOUNT, "Millisecond", "Nanoseconds", PLACES), 0.0);
        assertEquals(2860.0, engine.convertAsDouble(AMOUNT, "Millisecond", "Microseconds", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Millisecond", "Milliseconds", PLACES), 0.0);
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "Millisecond", "Seconds", PLACES), 0.0);
        assertEquals(0.00004766666666666667, engine.convertAsDouble(AMOUNT, "Millisecond", "Minutes"), 0.0);
        assertEquals(7.944444444444445e-7, engine.convertAsDouble(AMOUNT, "Millisecond", "Hours"), 0.0);
        assertEquals(3.310185185185185e-8, engine.convertAsDouble(AMOUNT, "Millisecond", "Days", PLACES), 0.0);
        assertEquals(4.728835978835979e-9, engine.convertAsDouble(AMOUNT, "Millisecond", "Weeks", PLACES), 0.0);
        assertEquals(1.0882800608828006E-9, engine.convertAsDouble(AMOUNT, "Millisecond", "Months"), 0.0);
        assertEquals(9.069000507356673E-11, engine.convertAsDouble(AMOUNT, "Millisecond", "Years", PLACES), 0.0);
        assertEquals(2859999999.9999995, engine.convertAsDouble(AMOUNT, "Second", "Nanoseconds", PLACES), 0.0);
        assertEquals(2860000, engine.convertAsDouble(AMOUNT, "Second", "Microseconds", PLACES), 0.0);
        assertEquals(2860, engine.convertAsDouble(AMOUNT, "Second", "Milliseconds", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Second", "Seconds", PLACES), 0.0);
        assertEquals(0.0476667, engine.convertAsDouble(AMOUNT, "Second", "Minutes", PLACES), 0.0);
        assertEquals(0.0007944444444444444, engine.convertAsDouble(AMOUNT, "Second", "Hours"), 0.0);
        assertEquals(0.00003310185185185185, engine.convertAsDouble(AMOUNT, "Second", "Days"), 0.0);
        assertEquals(0.0000047288359788359785, engine.convertAsDouble(AMOUNT, "Second", "Weeks"), 0.0);
        assertEquals(1.0882800608828005E-6, engine.convertAsDouble(AMOUNT, "Second", "Months"), 0.0);
        assertEquals(9.069000507356671E-8, engine.convertAsDouble(AMOUNT, "Second", "Years", PLACES), 0.0);
        assertEquals(171599999999.99997, engine.convertAsDouble(AMOUNT, "Minute", "Nanoseconds", PLACES), 0.0);
        assertEquals(171600000, engine.convertAsDouble(AMOUNT, "Minute", "Microseconds", PLACES), 0.0);
        assertEquals(171600, engine.convertAsDouble(AMOUNT, "Minute", "Milliseconds", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "Minute", "Seconds", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Minute", "Minutes", PLACES), 0.0);
        assertEquals(0.04766666666666666, engine.convertAsDouble(AMOUNT, "Minute", "Hours"), 0.0);
        assertEquals(0.0019861111111111112, engine.convertAsDouble(AMOUNT, "Minute", "Days"), 0.0);
        assertEquals(0.00028373015873015873, engine.convertAsDouble(AMOUNT, "Minute", "Weeks"), 0.0);
        assertEquals(6.529680365296803E-5, engine.convertAsDouble(AMOUNT, "Minute", "Months"), 0.0);
        assertEquals(5.441400304414003E-6, engine.convertAsDouble(AMOUNT, "Minute", "Years"), 0.0);
        assertEquals(10296000000000.0, engine.convertAsDouble(AMOUNT, "Hour", "Nanoseconds", PLACES), 0.0);
        assertEquals(10296000000.0, engine.convertAsDouble(AMOUNT, "Hour", "Microseconds", PLACES), 0.0);
        assertEquals(10296000, engine.convertAsDouble(AMOUNT, "Hour", "Milliseconds", PLACES), 0.0);
        assertEquals(10296, engine.convertAsDouble(AMOUNT, "Hour", "Seconds", PLACES), 0.0);
        assertEquals(171.6, engine.convertAsDouble(AMOUNT, "Hour", "Minutes", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Hour", "Hours", PLACES), 0.0);
        assertEquals(0.1191667, engine.convertAsDouble(AMOUNT, "Hour", "Days", PLACES), 0.0);
        assertEquals(0.017023809523809524, engine.convertAsDouble(AMOUNT, "Hour", "Weeks"), 0.0);
        assertEquals(0.003917808219178082, engine.convertAsDouble(AMOUNT, "Hour", "Months"), 0.0);
        assertEquals(3.2648401826484015E-4, engine.convertAsDouble(AMOUNT, "Hour", "Years"), 0.0);
        assertEquals(247104000000000.0, engine.convertAsDouble(AMOUNT, "Day", "Nanoseconds", PLACES), 0.0);
        assertEquals(247104000000.0, engine.convertAsDouble(AMOUNT, "Day", "Microseconds", PLACES), 0.0);
        assertEquals(247104000, engine.convertAsDouble(AMOUNT, "Day", "Milliseconds", PLACES), 0.0);
        assertEquals(247104, engine.convertAsDouble(AMOUNT, "Day", "Seconds", PLACES), 0.0);
        assertEquals(4118.4, engine.convertAsDouble(AMOUNT, "Day", "Minutes", PLACES), 0.0);
        assertEquals(68.64, engine.convertAsDouble(AMOUNT, "Day", "Hours", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Day", "Days", PLACES), 0.0);
        assertEquals(0.40857142857142853, engine.convertAsDouble(AMOUNT, "Day", "Weeks"), 0.0);
        assertEquals(0.0940274, engine.convertAsDouble(AMOUNT, "Day", "Months", PLACES), 0.0);
        assertEquals(0.007835616438356164, engine.convertAsDouble(AMOUNT, "Day", "Years"), 0.0);
        assertEquals(1729728000000000.0, engine.convertAsDouble(AMOUNT, "Week", "Nanoseconds", PLACES), 0.0);
        assertEquals(1729728000000.0, engine.convertAsDouble(AMOUNT, "Week", "Microseconds", PLACES), 0.0);
        assertEquals(1729728000, engine.convertAsDouble(AMOUNT, "Week", "Milliseconds", PLACES), 0.0);
        assertEquals(1729728, engine.convertAsDouble(AMOUNT, "Week", "Seconds", PLACES), 0.0);
        assertEquals(28828.8, engine.convertAsDouble(AMOUNT, "Week", "Minutes", PLACES), 0.0);
        assertEquals(480.48, engine.convertAsDouble(AMOUNT, "Week", "Hours", PLACES), 0.0);
        assertEquals(20.02, engine.convertAsDouble(AMOUNT, "Week", "Days", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Week", "Weeks", PLACES), 0.0);
        assertEquals(0.6581917808219178, engine.convertAsDouble(AMOUNT, "Week", "Months"), 0.0);
        assertEquals(0.0548493, engine.convertAsDouble(AMOUNT, "Week", "Years", PLACES), 0.0);
        assertEquals(7.51608E15, engine.convertAsDouble(AMOUNT, "Month", "Nanoseconds", PLACES), 0.0);
        assertEquals(7.51608E12, engine.convertAsDouble(AMOUNT, "Month", "Microseconds", PLACES), 0.0);
        assertEquals(7.51608E9, engine.convertAsDouble(AMOUNT, "Month", "Milliseconds", PLACES), 0.0);
        assertEquals(7516080.0, engine.convertAsDouble(AMOUNT, "Month", "Seconds", PLACES), 0.0);
        assertEquals(125268.0, engine.convertAsDouble(AMOUNT, "Month", "Minutes", PLACES), 0.0);
        assertEquals(2087.8, engine.convertAsDouble(AMOUNT, "Month", "Hours", PLACES), 0.0);
        assertEquals(86.9916667, engine.convertAsDouble(AMOUNT, "Month", "Days", PLACES), 0.0);
        assertEquals(12.427381, engine.convertAsDouble(AMOUNT, "Month", "Weeks", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Month", "Months", PLACES), 0.0);
        assertEquals(0.2383333, engine.convertAsDouble(AMOUNT, "Month", "Years", PLACES), 0.0);
        assertEquals(9.019296E16, engine.convertAsDouble(AMOUNT, "Year", "Nanoseconds", PLACES), 0.0);
        assertEquals(9.019296E13, engine.convertAsDouble(AMOUNT, "Year", "Microseconds", PLACES), 0.0);
        assertEquals(9.019296E10, engine.convertAsDouble(AMOUNT, "Year", "Milliseconds", PLACES), 0.0);
        assertEquals(9.019296E7, engine.convertAsDouble(AMOUNT, "Year", "Seconds", PLACES), 0.0);
        assertEquals(1503216.0, engine.convertAsDouble(AMOUNT, "Year", "Minutes", PLACES), 0.0);
        assertEquals(25053.6, engine.convertAsDouble(AMOUNT, "Year", "Hours", PLACES), 0.0);
        assertEquals(1043.9, engine.convertAsDouble(AMOUNT, "Year", "Days", PLACES), 0.0);
        assertEquals(149.1285714, engine.convertAsDouble(AMOUNT, "Year", "Weeks", PLACES), 0.0);
        assertEquals(34.32, engine.convertAsDouble(AMOUNT, "Year", "Months", PLACES), 0.0);
        assertEquals(2.86, engine.convertAsDouble(AMOUNT, "Year", "Years", PLACES), 0.0);
    }

    @Test
    public void testAliases() {
        assertEquals(0.00286, engine.convertAsDouble(AMOUNT, "ms", "secs", PLACES), 0.0);
        assertEquals(0.0476667, engine.convertAsDouble(AMOUNT, "min", "hrs", PLACES), 0.0);
        assertEquals(10296.0, engine.convertAsDouble(AMOUNT, "hr", "s"), 0.0);
        assertEquals(2.86E-6, engine.convertAsDouble(AMOUNT, "ns", "ms"), 0.0);
        assertEquals(5.441400304414003E-6, engine.convertAsDouble(AMOUNT, "mins", "yrs"), 0.0);
        assertEquals(34.32, engine.convertAsDouble(AMOUNT, "yr", "months"), 0.0);
        assertEquals(20.02, engine.convertAsDouble(AMOUNT, "wks", "days"), 0.0);
    }

    @Test
    public void testUnitGroup() {
        Conversion conversion = engine.convert(AMOUNT, "minutes", "hours");
        assertEquals("time", conversion.getUnitGroup().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConversion() {
        engine.convertAsDouble(AMOUNT, "minutes", "meter");
    }
}