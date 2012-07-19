package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Luminance extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("apostilb", "apostilbs", new String[] { "asb", "asbs", "a", "apos" },
				0.31830989));

		units.add(new SimpleSubUnit("blondel", "blondels", new String[] { "blond", "blo", "bl",
				"bls" }, 0.31830989));

		units.add(new SimpleSubUnit("candela per square centimetre",
				"candelas per square centimetre", new String[] { "candelas per square centimeter",
						"candela per square centimeter", "cd/cm2", "cpscm", "cd/cm^2",
						"candela/square centimetre", "candelas/square centimetre",
						"candela/square centimeter", "candelas/square centimeter" }, 10000.0));

		units.add(new SimpleSubUnit("candela per square foot", "candelas per square foot",
				new String[] { "candelas per square feet", "candela per square feet", "cd/ft2",
						"cpsft", "cd/ft^2", "candela/square foot", "candelas/square foot" }, 10.763910417));

		units.add(new SimpleSubUnit("candela per square inch", "candelas per square inch",
				new String[] { "cd/in2", "cpsin", "cd/in^2", "candela/square inch", "candelas/square inch"}, 1550.0031));

		units.add(new SimpleSubUnit("candela per square metre", "candelas per square metre",
				new String[] { "candelas per square meter", "candela per square meter", "cd/m2",
						"cpsm", "cd/m^2", "candela/square metre", "candelas/square metre",
						"candela/square meter", "candelas/square meter" }, 1));

		units.add(new SimpleSubUnit("footlambert", "footlamberts", new String[] { "fl", "ftlamb",
				"ftlambs" }, 3.4262590996));

		units.add(new SimpleSubUnit("kilocandela per square centimetre",
				"kilocandelas per square centimetre", new String[] {
						"kilocandelas per square centimeter", "kilocandela per square centimeter",
						"kcd/cm2", "kcpscm", "kcd/cm^2", "kilocandela/square centimetre", "kilocandelas/square centimetre",
						"kilocandela/square centimeter", "kilocandelas/square centimeter" }, 10000000.0));

		units.add(new SimpleSubUnit("kilocandela per square foot", "kilocandelas per square foot",
				new String[] { "kilocandelas per square feet", "kilocandela per square feet",
						"kcd/ft2", "kcpsft", "kcd/ft^2", "kilocandela/square foot", "kilocandelas/square foot" }, 10763.910417));

		units.add(new SimpleSubUnit("kilocandela per square inch", "kilocandelas per square inch",
				new String[] { "kcd/in2", "kcpsin", "kcd/in^2", "kilocandela/square inch", "kilocandelas/square inch" }, 1550003.1));

		units.add(new SimpleSubUnit("kilocandela per square metre",
				"kilocandelas per square metre", new String[] { "kilocandelas per square meter",
						"kilocandela per square meter", "kcd/m2", "kcpsm", "kcd/m^2", "kilocandela/square metre", "kilocandela/square metre",
						"kilocandela/square meter", "kilocandela/square meter" }, 1000.0));

		units.add(new SimpleSubUnit("lambert", "lamberts", new String[] { "lam", "lambs" },
				3183.0988618));

		units.add(new SimpleSubUnit("millilambert", "millilamberts", new String[] { "millilam",
				"millilambs", "millamb", "millambs", "mlamb", "mlambs" }, 3.1830988618));

		units.add(new SimpleSubUnit("nit", "nits", new String[] { "ni", "n" }, 1));

		units.add(new SimpleSubUnit("stilb", "stilbs", new String[] { "sb", "stb", "stbs" },
				10000.0));
	}

	@Override
	public String toString()
	{
		return "luminance";
	}
}
