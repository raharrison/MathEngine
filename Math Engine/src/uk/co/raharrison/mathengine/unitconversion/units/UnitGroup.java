package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class UnitGroup
{
	protected String name;
	protected ArrayList<SubUnit> units;

	public UnitGroup()
	{
		name = null;
		units = new ArrayList<SubUnit>();
	}

	public void addSubUnit(SubUnit subUnit)
	{
		units.add(subUnit);
	}

	public Conversion convert(double amount, String from, String to)
	{
		Conversion params = getConversionParams(from, to);
		params.setValue(amount);
		params.setResult(doConversion(params));

		return params;
	}

	protected abstract double doConversion(Conversion params);

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
	
	public Set<String> getUnits()
	{
		HashSet<String> results = new LinkedHashSet<String>();

		for (SubUnit unit : units)
		{
			results.add(unit.getPlural());
		}

		return results;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
