package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Reaumur extends TemperatureSubUnit
{
	public Reaumur()
	{
		super("degree reaumur", "degrees reaumur", new String[] { "reaumur", "reaumurs", "re",
				"res" });
	}

	@Override
	double toCelsius(double amount)
	{
		return amount * 5.0 / 4.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return (80.0 - amount) * 15.0 / 8.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount * 9.0 / 4.0 + 32;
	}

	@Override
	double toKelvins(double amount)
	{
		return amount * 5.0 / 4.0 + 273.15;
	}

	@Override
	double toNewtons(double amount)
	{
		return amount * 33.0 / 80.0;
	}

	@Override
	double toRankine(double amount)
	{
		return amount * 9.0 / 4.0 + 491.67;
	}

	@Override
	double toReaumur(double amount)
	{
		return amount;
	}

	@Override
	double toRomer(double amount)
	{
		return amount * 21.0 / 32.0 + 7.5;
	}
}
