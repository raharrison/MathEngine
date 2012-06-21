package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Romer extends TemperatureSubUnit
{
	public Romer()
	{
		super("degree romer", "degrees romer", new String[] { "rom", "roms", "romer", "romers",
				"ro", "ros" });
	}

	@Override
	double toCelsius(double amount)
	{
		return (amount - 7.5) * 40.0 / 21.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return (60 - amount) * 20.0 / 7.0;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return (amount - 7.5) * 24.0 / 7.0 + 32;
	}

	@Override
	double toKelvins(double amount)
	{
		return (amount - 7.5) * 40.0 / 21.0 + 273.15;
	}

	@Override
	double toNewtons(double amount)
	{
		return (amount - 7.5) * 22.0 / 35.0;
	}

	@Override
	double toRankine(double amount)
	{
		return (amount - 7.5) * 24.0 / 7.0 + 491.67;
	}

	@Override
	double toReaumur(double amount)
	{
		return (amount - 7.5) * 32.0 / 21.0;
	}

	@Override
	double toRomer(double amount)
	{
		return amount;
	}
}
