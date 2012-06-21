package uk.co.raharrison.mathengine.unitconversion.units.complex.temperature;

import uk.co.raharrison.mathengine.unitconversion.units.complex.ComplexUnitGroup;;

public class Temperature extends ComplexUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new Celsius());
		units.add(new Delisle());
		units.add(new Fahrenheit());
		units.add(new Kelvin());
		units.add(new Newton());
		units.add(new Rankine());
		units.add(new Reaumur());
		units.add(new Romer());
	}

	@Override
	public String toString()
	{
		return "temperature";
	}
}
