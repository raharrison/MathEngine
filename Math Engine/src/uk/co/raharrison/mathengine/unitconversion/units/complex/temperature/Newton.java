package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Newton extends TemperatureSubUnit
{
	public Newton()
	{
		super("degree newton", "degrees newton", new String[] { "newton", "newtons", "n", "ns",
				"ne", "nes" });
	}

	@Override
	double toCelsius(double amount)
	{
		return amount * 100.0 / 33.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return (33.0 - amount) * 50.0 / 11.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount * 60.0 / 11.0 + 32;
	}

	@Override
	double toKelvins(double amount)
	{
		return amount * 100.0 / 33.0 + 273.15;
	}

	@Override
	double toNewtons(double amount)
	{
		return amount;
	}

	@Override
	double toRankine(double amount)
	{
		return amount * 60.0 / 11.0 + 491.67;
	}

	@Override
	double toReaumur(double amount)
	{
		return amount * 80.0 / 33.0;
	}

	@Override
	double toRomer(double amount)
	{
		return amount * 35.0 / 22.0 + 7.5;
	}
}
