package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.ArrayList;

public abstract class UnitGroup
{
	protected ArrayList<SubUnit> units;

	public UnitGroup()
	{
		units = new ArrayList<SubUnit>();
		fillUnits();
	}

	public Conversion convert(double amount, String from, String to)
	{
		Conversion params = getConversionParams(from, to);
		params.setValue(amount);
		params.setResult(doConversion(params, amount));

		return params;
	}

	protected abstract double doConversion(Conversion params, double amount);

	protected abstract void fillUnits();

	public Conversion getConversionParams(String from, String to)
	{
		Conversion params = new Conversion();

		for (SubUnit unit : units)
		{
			if (unit.isMatch(from))
				params.setFrom(unit);

			if (unit.isMatch(to))
				params.setTo(unit);
		}

		return params;
	}

	public String[] getUnits()
	{
		ArrayList<String> results = new ArrayList<String>();

		for (SubUnit unit : units)
		{
			results.add(unit.toString());
		}

		return results.toArray(new String[results.size()]);
	}

	@Override
	public abstract String toString();
}
