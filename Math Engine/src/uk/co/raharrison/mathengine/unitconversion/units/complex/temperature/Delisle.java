package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

class Delisle extends TemperatureSubUnit
{
	public Delisle()
	{
		super("degree delisle", "degrees delisle", new String[] { "delisle", "delisles", "d", "ds",
				"de" });
	}

	@Override
	double toCelsius(double amount)
	{
		return 100 - amount * 2.0 / 3.0;
	}

	@Override
	double toDelisle(double amount)
	{
		return amount;
	}

	@Override
	double toFahrenheit(double amount)
	{
		return 212 - amount * 6.0 / 5.0;
	}

	@Override
	double toKelvins(double amount)
	{
		return 373.15 - amount * 2.0 / 3.0;
	}

	@Override
	double toNewtons(double amount)
	{
		return 33 - amount * 11.0 / 50.0;
	}

	@Override
	double toRankine(double amount)
	{
		return 671.67 - amount * 6.0 / 5.0;
	}

	@Override
	double toReaumur(double amount)
	{
		return 80 - amount * 8.0 / 15.0;
	}

	@Override
	double toRomer(double amount)
	{
		return 60 - amount * 7.0 / 20.0;
	}
}
