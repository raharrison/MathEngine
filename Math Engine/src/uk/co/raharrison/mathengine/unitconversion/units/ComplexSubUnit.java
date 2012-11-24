package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.HashMap;

public class ComplexSubUnit extends SubUnit
{
	private HashMap<String, String> conversions;
	private String variable;

	public ComplexSubUnit()
	{
		this.conversions = new HashMap<>();
	}

	public void addConversion(String unit, String equation)
	{
		this.conversions.put(unit, equation);
	}

	public String getEquationFor(ComplexSubUnit to)
	{
		for (String s : to.getUniqueAliases())
		{
			if (this.conversions.containsKey(s))
				return this.conversions.get(s);
		}
		return null;
	}

	public String getVariable()
	{
		return variable;
	}

	public void setVariable(String variable)
	{
		this.variable = variable;
	}
}
