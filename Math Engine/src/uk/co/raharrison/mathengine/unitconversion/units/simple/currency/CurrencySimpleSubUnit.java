package uk.co.raharrison.mathengine.unitconversion.units.simple.currency;

import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleSubUnit;

public class CurrencySimpleSubUnit extends SimpleSubUnit implements Cloneable
{
	public CurrencySimpleSubUnit(String singular, String plural, String[] aliases, double conversion)
	{
		super(singular, plural, aliases, conversion);
	}
	
	void setConversion(double conversion)
	{
		this.conversion = conversion;
	}
}
