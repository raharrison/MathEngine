package uk.co.raharrison.mathengine.unitconversion.units.simple;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.UnitGroup;

public abstract class SimpleUnitGroup extends UnitGroup
{
	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			return amount * from.getConversion() / to.getConversion();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
}
