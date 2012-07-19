package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Illuminance extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("kilolux", "kilolux", new String[] { "klx" }, 1000.0));

		units.add(new SimpleSubUnit("lumen per square centimetre", "lumens per square centimetre",
				new String[] { "lm/cm", "lm/cm^2", "lumen/square centimetre",
						"lumens/square centimetre", "lumen/square centimeter",
						"lumens/square centimeter", "lumen/centimetre", "lumens/centimetre",
						"lumen/centimeter", "lumens/centimeter" }, 10000.0));

		units.add(new SimpleSubUnit("lumen per square metre", "lumens per square metre",
				new String[] { "lm/m", "lm/m^2", "lumen/square metre", "lumens/square metre",
						"lumen/square meter", "lumens/square meter", "lumen/metre", "lumens/metre",
						"lumen/meter", "lumens/meter" }, 1.0));

		units.add(new SimpleSubUnit("lumen per square foot", "lumens per square foot",
				new String[] { "lm/ft", "lm/ft^2", "lumen/square foot", "lumens/square foot",
						"lumen/foot", "lumens/foot", "footcandle", "footcandles", "fc", "fcs" }, 10.7639104));
		
		units.add(new SimpleSubUnit("lumen per square inch", "lumens per square inch",
				new String[] { "lm/in", "lm/in^2", "lumen/square inch", "lumens/square inch",
						"lumen/inch", "lumens/inch" }, 1550.0030976));
		
		units.add(new SimpleSubUnit("lux", "lux", new String[] {"lx", "metercandle", "metercandles", "metrecandle", "metrecandles"}, 1.0));

		units.add(new SimpleSubUnit("milliphot", "milliphots", new String[] {"mph"}, 10.0));
		units.add(new SimpleSubUnit("nox", "nox", new String[] {"nx"}, 0.001));
		units.add(new SimpleSubUnit("phot", "phot", new String[] {"ph"}, 10000.0));
	}

	@Override
	public String toString()
	{
		return "illuminance";
	}
}
