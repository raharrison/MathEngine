package uk.co.ryanharrison.mathengine.unitconversion.units;

public class SimpleUnitGroup extends UnitGroup
{
	@Override
	protected double doConversion(Conversion params)
	{
		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			return params.getValue() * to.getConversion() / from.getConversion();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
}
