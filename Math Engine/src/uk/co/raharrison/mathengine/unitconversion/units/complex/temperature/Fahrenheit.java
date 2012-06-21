package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Fahrenheit extends TemperatureSubUnit
{
	public Fahrenheit()
	{
		super("degree fahrenheit", "degrees fahrenheit", new String[] { "f", "fs", "fahrenheit",
				"fahrenheits" });
	}

	@Override
	double toCelsius(double amount)
	{
		return (amount - 32) * 5.0 / 9.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return (212 - amount) * 5.0 / 6.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount;
	}

	@Override
	double toKelvins(double amount)
	{
		return (amount + 459.67) * 5.0 / 9.0;
	}

	@Override
	double toNewtons(double amount)
	{
		return (amount - 32) * 11.0 / 60.0;
	}

	@Override
	double toRankine(double amount)
	{
		return amount + 459.67;
	}

	@Override
	double toReaumur(double amount)
	{
		return (amount - 32) * 4.0 / 9.0;
	}

	@Override
	double toRomer(double amount)
	{
		return (amount - 32) * 7.0 / 24.0 + 7.5;
	}
}
