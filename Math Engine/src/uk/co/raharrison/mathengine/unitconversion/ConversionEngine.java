package uk.co.raharrison.mathengine.unitconversion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.UnitGroup;
import uk.co.raharrison.mathengine.unitconversion.units.complex.temperature.Temperature;
import uk.co.raharrison.mathengine.unitconversion.units.simple.Angles;
import uk.co.raharrison.mathengine.unitconversion.units.simple.Luminance;
import uk.co.raharrison.mathengine.unitconversion.units.simple.Mass;
import uk.co.raharrison.mathengine.unitconversion.units.simple.Time;

public final class ConversionEngine
{
	private static double roundTo(double valueToRound, int numberOfDecimalPlaces)
	{
		return BigDecimal.valueOf(valueToRound)
				.setScale(numberOfDecimalPlaces, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}

	private ArrayList<UnitGroup> groups;

	private Pattern conversionPattern;

	public ConversionEngine()
	{
		groups = new ArrayList<UnitGroup>();
		groups.add(new Mass());
		groups.add(new Temperature());
		groups.add(new Time());
		groups.add(new Angles());
		groups.add(new Luminance());

		conversionPattern = Pattern.compile("(-?\\d*\\.?\\d*)(.+) (in|to) (.+)");
	}

	public double convert(double amount, String from, String to)
	{
		ConversionParams result = getResult(amount, from, to);

		if (result != null)
		{
			return result.getResult();
		}

		throw new IllegalArgumentException("Unable to convert units " + from + " and " + to);
	}

	public String convertToString(double amount, String from, String to)
	{
		ConversionParams result = getResult(amount, from, to);

		if (result != null)
		{
			double converted = roundTo(result.getResult(), 7);

			String resultFrom = Math.abs(amount) == 1.0 ? result.getFrom().getBaseAliasSingular()
					: result.getFrom().getBaseAliasPlural();
			String resultTo = Math.abs(converted) == 1.0 ? result.getTo().getBaseAliasSingular()
					: result.getTo().getBaseAliasPlural();
			return String.format("%s %s = %s %s", amount, resultFrom, converted, resultTo);
		}

		throw new IllegalArgumentException("Unable to convert units " + from + " and " + to);
	}

	// In the format [number] [unit] in/to [unit]
	public String convertToString(String conversion)
	{
		Matcher m = conversionPattern.matcher(conversion.toLowerCase());

		if (m.matches())
		{
			if (m.groupCount() == 4)
			{
				try
				{
					return convertToString(Double.parseDouble(m.group(1).trim()),
							m.group(2).trim(), m.group(4).trim());
				}
				catch (NumberFormatException e)
				{
					throw new IllegalArgumentException("Unable to handle conversion string - "
							+ conversion);
				}
			}
		}

		throw new IllegalArgumentException("Unable to handle conversion string - " + conversion);
	}

	private ConversionParams getResult(double amount, String from, String to)
	{
		for (UnitGroup g : groups)
		{
			try
			{
				return g.convert(amount, from, to);

			}
			catch (IllegalArgumentException e)
			{
			}
		}

		return null;
	}

	public String[] getUnitGroups()
	{
		ArrayList<String> results = new ArrayList<String>();

		for (UnitGroup group : groups)
		{
			results.add(group.toString());
		}

		return results.toArray(new String[results.size()]);
	}

	public String[] getUnits()
	{
		ArrayList<String> results = new ArrayList<String>();

		for (UnitGroup group : groups)
		{
			for (String string : group.getUnits())
			{
				results.add(string);
			}
		}

		return results.toArray(new String[results.size()]);
	}

	public String[] getUnitsFor(String unitType)
	{
		for (UnitGroup group : groups)
		{
			if (group.toString().equalsIgnoreCase(unitType))
				return group.getUnits();
		}

		throw new IllegalArgumentException("Could not find units associated with " + unitType);
	}
}
