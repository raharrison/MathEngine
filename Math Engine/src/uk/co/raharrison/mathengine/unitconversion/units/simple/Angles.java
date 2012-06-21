package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Angles extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("degree", "degrees", new String[] { "deg", "degs", "d" },
				0.01745329252));

		units.add(new SimpleSubUnit("full circle", "full circles", new String[] { "circle",
				"circles", "fc", "revolution", "revolutions" }, 6.2831853072));

		units.add(new SimpleSubUnit("gradian", "gradians", new String[] { "grad", "grads", "g" },
				0.015707963268));

		units.add(new SimpleSubUnit("half circle", "half circles", new String[] { "1/2 circle",
				"1/2 circles", "hc" }, Math.PI));

		units.add(new SimpleSubUnit("minute", "minutes", new String[] { "min", "mins", "m" },
				0.00029088820867));

		units.add(new SimpleSubUnit("point", "points", new String[] { "p", "pt", "pts" },
				0.19634954085));

		units.add(new SimpleSubUnit("quarter circle", "quarter circles", new String[] {
				"1/4 circle", "1/4 circles", "qc" }, Math.PI / 2));

		units.add(new SimpleSubUnit("radian", "radians", new String[] { "rad", "rads", "r" }, 1));

		units.add(new SimpleSubUnit("second", "seconds", new String[] { "s", "sec", "secs" },
				0.0000048481368111));
	}

	@Override
	public String toString()
	{
		return "angles";
	}
}
