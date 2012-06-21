package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Celsius extends TemperatureSubUnit
{
	public Celsius()
	{
		super("degree celsius", "degrees celsius", new String[] { "c", "cs", "degrees centigrade",
				"centigrade", "celsius" });
	}

	@Override
	double toCelsius(double amount)
	{
		return amount;
	}

	@Override
	double toDelisle(double amount)
	{
		return (100 - amount) * 3.0 / 2.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount * 9.0 / 5.0 + 32;
	}

	@Override
	double toKelvins(double amount)
	{
		return amount + 273.15;
	}

	@Override
	double toNewtons(double amount)
	{
		return amount * 33.0 / 100.0;
	}

	@Override
	double toRankine(double amount)
	{
		return (amount + 273.15) * 9.0 / 5.0;
	}

	@Override
	double toReaumur(double amount)
	{
		return amount * 4.0 / 5.0;
	}

	@Override
	double toRomer(double amount)
	{
		return amount * 21.0 / 40.0 + 7.5;
	}
}
