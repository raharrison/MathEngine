package uk.co.raharrison.mathengine.unitconversion.units.simple.storage;

import java.math.BigDecimal;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleSubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleUnitGroup;

public class DigitalStorage extends SimpleUnitGroup
{
	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			BigDecimal fromDec = BigDecimal.valueOf(from.getConversion());
			BigDecimal toDec = BigDecimal.valueOf(to.getConversion());
			
			BigDecimal result = BigDecimal.valueOf(amount).multiply(fromDec).divide(toDec);
			
			return result.doubleValue();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
	
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("bit", "bits", new String[] { "bt", "bts" }, 1.0));
		units.add(new SimpleSubUnit("byte", "bytes", new String[] { "b", "bs" }, 8.0));
		units.add(new SimpleSubUnit("kilobyte", "kilobytes", new String[] { "kb", "kbs" }, 8192.0));
		units.add(new SimpleSubUnit("megabyte", "megabytes", new String[] { "mb", "mbs" }, 8388608.0));
		units.add(new SimpleSubUnit("gigabyte", "gigabytes", new String[] { "gb", "gbs" }, 8589934592.0));
		units.add(new SimpleSubUnit("terabyte", "terabytes", new String[] { "tb", "tbs" }, 8796093022208.0));
		units.add(new SimpleSubUnit("petabyte", "petabytes", new String[] { "pb", "pbs" }, 9007199254740992.0));
	}
	
	@Override
	public String toString()
	{
		return "digital storage";
	}
}
