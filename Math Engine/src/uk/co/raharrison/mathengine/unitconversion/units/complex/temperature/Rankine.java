package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Rankine extends TemperatureSubUnit
{
	public Rankine()
	{
		super("degree rankine", "degrees rankine", new String[] { "rankine", "rankines", "ra",
				"ras" });
	}

	@Override
	double toCelsius(double amount)
	{
		return (amount - 491.67) * 5.0 / 9.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return (671.67 - amount) * 5.0 / 6.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount - 459.67;
	}

	@Override
	double toKelvins(double amount)
	{
		return amount * 5.0 / 9.0;
	}

	@Override
	double toNewtons(double amount)
	{
		return (amount - 491.67) * 11.0 / 60.0;
	}

	@Override
	double toRankine(double amount)
	{
		return amount;
	}

	@Override
	double toReaumur(double amount)
	{
		return (amount - 491.67) * 4.0 / 9.0;
	}

	@Override
	double toRomer(double amount)
	{
		return (amount - 491.67) * 7.0 / 24.0 + 7.5;
	}
}
