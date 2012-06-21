package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Kelvin extends TemperatureSubUnit
{
	public Kelvin()
	{
		super("kelvin", "kelvins", new String[] { "k", "ks" });
	}

	@Override
	double toCelsius(double amount)
	{
		return amount - 273.15;
	}

	@Override
	double toDelisle(double amount)
	{
		return (373.15 - amount) * 3.0 / 2.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return amount * 9.0 / 5.0 - 459.67;
	}

	@Override
	double toKelvins(double amount)
	{
		return amount;
	}

	@Override
	double toNewtons(double amount)
	{
		return (amount - 273.15) * 33.0 / 100.0;
	}

	@Override
	double toRankine(double amount)
	{
		return amount * 9.0 / 5.0;
	}

	@Override
	double toReaumur(double amount)
	{
		return (amount - 273.15) * 4.0 / 5.0;
	}

	@Override
	double toRomer(double amount)
	{
		return (amount - 273.15) * 21.0 / 40.0 + 7.5;
	}
}
