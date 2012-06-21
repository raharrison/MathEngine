package uk.co.raharrison.mathengine.unitconversion.units.complex;

import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;

public abstract class ComplexSubUnit extends SubUnit
{
	public ComplexSubUnit(String singular, String plural, String[] aliases)
	{
		super(singular, plural, aliases);
	}

	public abstract double convert(double amount, ComplexSubUnit to);
}
