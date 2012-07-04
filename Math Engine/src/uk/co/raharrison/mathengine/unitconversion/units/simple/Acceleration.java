package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Acceleration extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("centigal", "centigals", new String[] { "cgal", "cgals" },
				00001));
		units.add(new SimpleSubUnit("centimetre/square second", "centimetres/square second",
				new String[] { "centimeter/square second", "centimeters/square second", "cm/s^2",
						"cm second square" }, 0.0001));

		units.add(new SimpleSubUnit("decigal", "decigals", new String[] { "dgal", "dgals" }, 0.001));

		units.add(new SimpleSubUnit("decimetre/square second", "decimetres/square second",
				new String[] { "decimeter/square second", "decimeters/square second", "dm/s^2",
						"dm second square" }, 0.1));

		units.add(new SimpleSubUnit("dekametre/square second", "dekametres/square second",
				new String[] { "dekameter/square second", "decimeters/square second", "dkm/s^2",
						"dkm second square" }, 0.1));

		units.add(new SimpleSubUnit("foot/square second", "foot/square second", new String[] {
				"feet/square second", "feet/square second", "ft/s^2", "ft second square" }, 0.3048));

		units.add(new SimpleSubUnit("g unit", "g units", new String[] { "standard gravity",
				"standard gravities", "g", "g's" }, 9.80665));

		units.add(new SimpleSubUnit("gal", "gals", new String[] { "galileo", "galileos" }, 0.01));

		units.add(new SimpleSubUnit("hectometre/square second", "hectometres/square second",
				new String[] { "hectometer/square second", "hectomeers/square second", "hm/s^2",
						"hm second square" }, 100));

		units.add(new SimpleSubUnit("inch/square second", "inches/square second", new String[] {
				"in/square second", "ins/square second", "in/s^2", "in second square" }, 0.0254));

		units.add(new SimpleSubUnit("kilometre/hour second", "kilometres/hour second",
				new String[] { "kilometer/hour second", "kilometers/hour second", "kmh/s",
						"km per hour per second" }, 0.27777777778));

		units.add(new SimpleSubUnit("kilometre/square second", "kilometres/square second",
				new String[] { "kilometer/square second", "kilometers/square second", "km/s^2",
						"km second square" }, 1000));

		units.add(new SimpleSubUnit("metre/square second", "metres/square second", new String[] {
				"meter/square second", "meters/square second", "m/s^2", "m second square" }, 1));

		units.add(new SimpleSubUnit("mile/hour minute", "miles/hour minute", new String[] {
				"mile/hour minute", "mile/hour minute", "mph/m", "mile per hour per minute" },
				0.0074506666667));

		units.add(new SimpleSubUnit("mile/hour second", "miles/hour second", new String[] {
				"mile/hour second", "mile/hour second", "mph/s", "mile per hour per second" },
				0.44704));

		units.add(new SimpleSubUnit("mile/square second", "miles/square second", new String[] {
				"mile/square second", "miles/square second", "mile/s^2", "mile second square" },
				1609.344));

		units.add(new SimpleSubUnit("milligal", "milligals", new String[] { "mgal", "mgals" },
				0.00001));

		units.add(new SimpleSubUnit("millimetre/square second", "millimetres/square second",
				new String[] { "millimeter/square second", "millimeters/square second", "mm/s^2",
						"mm second square" }, 0.001));
	}

	@Override
	public String toString()
	{
		return "acceleration";
	}
}
