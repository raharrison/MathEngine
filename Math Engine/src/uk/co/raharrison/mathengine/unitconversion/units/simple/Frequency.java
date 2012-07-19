package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Frequency extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("hertz", "hertz", new String[] { "hz", "revolution per second",
				"revolutions per second", "revolution second", "revolutions second",
				"cycle per second", "cycles per second", "cycle second", "cycles second",
				"1/second" }, 1));

		units.add(new SimpleSubUnit("degree per hour", "degrees per hour", new String[] {
				"degree/hour", "degrees/hour", "deg hour", "degs hour", "deg/hour", "degs/hour" },
				7.7160493827e-7));
		
		units.add(new SimpleSubUnit("degree per minute", "degrees per minute", new String[] {
				"degree/minute", "degrees/minute", "deg minute", "degs minute", "deg/minute", "degs/minute" },
				0.000046296296296));
		
		units.add(new SimpleSubUnit("degree per second", "degrees per second", new String[] {
				"degree/second", "degrees/second", "deg second", "degs second", "deg/second", "degs/second" },
				0.00277777777778));
		
		units.add(new SimpleSubUnit("gigahertz", "gigahertz", new String[] { "ghz" }, 1000000000));
		units.add(new SimpleSubUnit("kilohertz", "kilohertz", new String[] { "khz" }, 1000));
		units.add(new SimpleSubUnit("megahertz", "megahertz", new String[] { "mhz" }, 1000000));
		units.add(new SimpleSubUnit("millihertz", "millihertz", new String[] { "mmhz" }, 0.001));
		units.add(new SimpleSubUnit("terrahertz", "terrahertz", new String[] { "thz" }, 1000000000000.0));
		
		units.add(new SimpleSubUnit("radian per hour", "radians per hour", new String[] {
				"radian/hour", "radians/hour", "rad hour", "rads hour", "rad/hour", "rads/hour" },
				0.000044209706621));
		
		units.add(new SimpleSubUnit("radian per minute", "radians per minute", new String[] {
				"radian/minute", "radians/minute", "rad minute", "rads minute", "rad/minute", "rads/minute" },
				0.0026525823738));
		
		units.add(new SimpleSubUnit("radian per second", "radians per second", new String[] {
				"radian/second", "radians/second", "rad second", "rads second", "rad/second", "rads/second" },
				0.15915494327));
		
		units.add(new SimpleSubUnit("revolution per hour", "revolutions per hour", new String[] {
				"revolution/hour", "revolutions/hour", "rev hour", "revs hour", "rev/hour", "revs/hour" },
				0.00027777777778));
		
		units.add(new SimpleSubUnit("revolution per minute", "revolutions per minute", new String[] {
				"revolution/minute", "revolutions/minute", "rev minute", "revs minute", "rev/minute", "revs/minute", "rpm" },
				 0.016666666667));
		
		units.add(new SimpleSubUnit("revolution per second", "revolutions per second", new String[] {
				"revolution/second", "revolutions/second", "rev second", "revs second", "rev/second", "revs/second" },
				1.0));

	}

	@Override
	public String toString()
	{
		return "frequency";
	}
}
