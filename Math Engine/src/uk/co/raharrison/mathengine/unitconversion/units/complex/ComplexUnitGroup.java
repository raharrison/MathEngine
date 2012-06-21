package uk.co.raharrison.mathengine.unitconversion.units.complex;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.UnitGroup;

public abstract class ComplexUnitGroup extends UnitGroup
{
	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		ComplexSubUnit from;

		if ((from = (ComplexSubUnit) params.getFrom()) != null)
		{
			return from.convert(amount, (ComplexSubUnit) params.getTo());
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
}