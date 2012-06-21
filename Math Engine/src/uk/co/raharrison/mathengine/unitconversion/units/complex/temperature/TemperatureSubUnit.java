package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

import uk.co.raharrison.mathengine.unitconversion.units.complex.ComplexSubUnit;

abstract class TemperatureSubUnit extends ComplexSubUnit
{
	public TemperatureSubUnit(String singular, String plural, String[] aliases)
	{
		super(singular, plural, aliases);
	}

	@Override
	public double convert(double amount, ComplexSubUnit to)
	{
		if (to instanceof Celsius)
		{
			return toCelsius(amount);
		}
		else if (to instanceof Delisle)
		{
			return toDelisle(amount);
		}
		else if (to instanceof Fahrenheit)
		{
			return toFahrenheit(amount);
		}
		else if (to instanceof Kelvin)
		{
			return toKelvins(amount);
		}
		else if (to instanceof Rankine)
		{
			return toRankine(amount);
		}
		else if (to instanceof Reaumur)
		{
			return toReaumur(amount);
		}
		else if (to instanceof Romer)
		{
			return toRomer(amount);
		}
		else if (to instanceof Newton)
		{
			return toNewtons(amount);
		}
		else
		{
			throw new IllegalArgumentException("Unable to handle unit - " + to);
		}
	}

	abstract double toCelsius(double amount);

	abstract double toDelisle(double amount);

	abstract double toFahrenheit(double amount);

	abstract double toKelvins(double amount);

	abstract double toNewtons(double amount);

	abstract double toRankine(double amount);

	abstract double toReaumur(double amount);

	abstract double toRomer(double amount);
}
