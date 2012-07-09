package uk.co.raharrison.mathengine.unitconversion.units.simple;

import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;

public class SimpleSubUnit extends SubUnit
{
	protected double conversion;

	public SimpleSubUnit(String singular, String plural, String[] aliases, double conversion)
	{
		super(singular, plural, aliases);
		this.conversion = conversion;
	}

	public double getConversion()
	{
		return this.conversion;
	}
}
