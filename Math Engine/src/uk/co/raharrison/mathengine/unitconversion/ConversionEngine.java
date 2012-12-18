package uk.co.raharrison.mathengine.unitconversion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import uk.co.raharrison.mathengine.MathUtils;
import uk.co.raharrison.mathengine.unitconversion.units.Conversion;
import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.UnitGroup;
import uk.co.raharrison.mathengine.unitconversion.units.currency.Currency;
import uk.co.raharrison.mathengine.unitconversion.units.timezones.TimeZones;

public final class ConversionEngine
{
	private List<UnitGroup> groups;

	private Pattern conversionPattern;

	private static final String PATH = "units.xml";

	public ConversionEngine()
	{
		groups = new ArrayList<UnitGroup>();

		fillGroups();

		conversionPattern = Pattern.compile("(-?\\d*\\.?\\d*)(.+) (in|to|as) (.+)");
	}

	public Conversion convert(double amount, String from, String to)
	{
		Conversion result = getResult(amount, from.toLowerCase(), to.toLowerCase());

		if (result != null)
		{
			return result;
		}

		throw generateIllegalArgumentException(from, to);
	}

	public Conversion convert(String conversion)
	{
		Matcher m = conversionPattern.matcher(conversion.toLowerCase());

		if (m.matches())
		{
			if (m.groupCount() == 4)
			{
				try
				{
					return convert(Double.parseDouble(m.group(1).trim()), m.group(2).trim(), m
							.group(4).trim());
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

	public double convertAsDouble(double amount, String from, String to)
	{
		Conversion result = getResult(amount, from.toLowerCase(), to.toLowerCase());

		if (result != null)
		{
			return result.getResult();
		}

		throw generateIllegalArgumentException(from, to);
	}

	public String convertToString(double amount, String from, String to)
	{
		Conversion result = getResult(amount, from.toLowerCase(), to.toLowerCase());

		if (result != null)
		{
			double converted = MathUtils.round(result.getResult(), 7);

			String resultFrom = Math.abs(amount) == 1.0 ? result.getFrom().getBaseAliasSingular()
					: result.getFrom().getBaseAliasPlural();
			String resultTo = Math.abs(converted) == 1.0 ? result.getTo().getBaseAliasSingular()
					: result.getTo().getBaseAliasPlural();
			return String.format("%s %s = %s %s", amount, resultFrom, converted, resultTo);
		}

		throw generateIllegalArgumentException(from, to);
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

	private void fillGroups()
	{
		File file = new File(PATH);

		// Check if the file exists
		if (!file.exists())
			throw new RuntimeException("Units file not found");

		// Create a "parser factory" for creating SAX parsers
		SAXParserFactory spfac = SAXParserFactory.newInstance();

		// Now use the parser factory to create a SAXParser object
		SAXParser sp = null;

		try
		{
			sp = spfac.newSAXParser();

			// Create an instance of this class; it defines all the handler
			// methods
			UnitHandler handler = new UnitHandler();

			// Finally, tell the parser to parse the input and notify the
			// handler
			sp.parse(file.getPath(), handler);
			this.groups = handler.getGroups();
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
	}

	private String[] generateExceptionParameters(String from, String to)
	{
		SubUnit newFrom = null, newTo = null;
		Conversion params;

		for (UnitGroup g : groups)
		{
			params = g.getConversionParams(from, to);

			if (params.getTo() != null)
				newTo = params.getTo();

			if (params.getFrom() != null)
				newFrom = params.getFrom();

			if (newFrom != null && newTo != null)
				break;
		}

		String[] result = new String[2];

		if (newFrom != null)
			result[0] = newFrom.getBaseAliasPlural();
		else
			result[0] = from;

		if (newTo != null)
			result[1] = newTo.getBaseAliasPlural();
		else
			result[1] = to;

		return result;
	}

	private RuntimeException generateIllegalArgumentException(String from, String to)
	{
		String[] params = generateExceptionParameters(from, to);
		return new IllegalArgumentException("Unable to convert from " + params[0] + " to "
				+ params[1]);
	}

	private Conversion getResult(double amount, String from, String to)
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

	public String getUnitGroupOfSubUnit(SubUnit unit)
	{
		for (UnitGroup g : groups)
		{
			if (g.getUnits().contains(unit.toString()))
			{
				return g.toString();
			}
		}

		throw new IllegalArgumentException("Could not find Unit group of supplied SubUnit");
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
			{
				Set<String> units = group.getUnits();
				return units.toArray(new String[units.size()]);
			}
		}

		throw new IllegalArgumentException("Could not find units associated with " + unitType);
	}

	public void update()
	{
		for (UnitGroup group : groups)
		{
			group.update();
		}
	}

	public void updateCurrencies()
	{
		for (UnitGroup group : groups)
		{
			if (group instanceof Currency)
			{
				group.update();
			}
		}
	}

	public void updateTimeZones()
	{
		for (UnitGroup group : groups)
		{
			if (group instanceof TimeZones)
			{
				group.update();
			}
		}
	}
}
