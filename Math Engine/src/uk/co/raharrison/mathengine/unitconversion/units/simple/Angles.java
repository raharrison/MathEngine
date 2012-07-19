package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Angles extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("degree", "degrees", new String[] { "degree of arc", "degrees of arc", "deg", "degs", "d" },
				0.01745329252));

		units.add(new SimpleSubUnit("full circle", "full circles", new String[] { "circle",
				"circles", "fc", "revolution", "revolutions" }, 2 * Math.PI));

		units.add(new SimpleSubUnit("gradian", "gradians", new String[] { "grad", "grads", "g" },
				0.015707963268));

		units.add(new SimpleSubUnit("half circle", "half circles", new String[] { "1/2 circle",
				"1/2 circles", "hc" }, Math.PI));

		units.add(new SimpleSubUnit("arc minute", "arc minutes", new String[] { "min", "mins", "m", "minute", "minutes", "arcminute", "arcminutes" },
				0.00029088820867));

		units.add(new SimpleSubUnit("point", "points", new String[] { "p", "pt", "pts" },
				0.19634954085));

		units.add(new SimpleSubUnit("quarter circle", "quarter circles", new String[] {
				"1/4 circle", "1/4 circles", "qc", "quadrant", "quadrants", "qdt", "qdts" }, Math.PI / 2.0));
		
		units.add(new SimpleSubUnit("sixth of a circle", "sixths of a circle", new String[] {
				"1/6 circle", "1/6 circles", "sextant", "sextants", "sxt", "sxts" }, Math.PI / 3.0));
		
		units.add(new SimpleSubUnit("eighth of a circle", "eighths of a circle", new String[] {
				"1/8 circle", "1/8 circles", "octant", "octants", "oct", "octs" }, Math.PI / 4.0));
		
		units.add(new SimpleSubUnit("tenth of a circle", "tenths of a circle", new String[] {
				"1/10 circle", "1/10 circles"}, Math.PI / 5.0));
		
		units.add(new SimpleSubUnit("twelfth of a circle", "twelfths of a circle", new String[] {
				"1/12 circle", "1/12 circles", "sign", "signs", "sgn", "sgns"}, Math.PI / 6.0));
		
		units.add(new SimpleSubUnit("sixteenth of a circle", "sixteenths of a circle", new String[] {
				"1/16 circle", "1/16 circles"}, Math.PI / 8.0));

		units.add(new SimpleSubUnit("radian", "radians", new String[] { "rad", "rads", "r" }, 1));

		units.add(new SimpleSubUnit("arc second", "arc seconds", new String[] { "s", "sec", "secs", "second", "seconds", "arcsecond", "arcseconds" },
				0.0000048481368111));
		
		units.add(new SimpleSubUnit("angular mil", "angular mils", new String[] { "mil", "mils" }, 0.00098174770425));
	}

	@Override
	public String toString()
	{
		return "angles";
	}
}
