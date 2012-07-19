package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class DigitalStorage extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("bit", "bits", new String[] { "bt", "bts" }, 1.0));
		units.add(new SimpleSubUnit("nibble", "nibble", new String[] { "nib", "nibs" }, 4.0));
		units.add(new SimpleSubUnit("byte", "bytes", new String[] { "b", "bs" }, 8.0));
		
		units.add(new SimpleSubUnit("kilobyte", "kilobytes", new String[] { "kb", "kbs" }, 8192.0));
		units.add(new SimpleSubUnit("kilobit", "kilobits", new String[] { "kbt", "kbts" }, 1000.0));
		
		units.add(new SimpleSubUnit("megabyte", "megabytes", new String[] { "mb", "mbs" },
				8388608.0));
		units.add(new SimpleSubUnit("megabit", "megabits", new String[] { "mbt", "mbts" },
				1.0E6));
		
		units.add(new SimpleSubUnit("gigabyte", "gigabytes", new String[] { "gb", "gbs" },
				8589934592.0));
		units.add(new SimpleSubUnit("gigabit", "gigabits", new String[] { "gbt", "gbts" },
				1.0E9));
		
		units.add(new SimpleSubUnit("terabyte", "terabytes", new String[] { "tb", "tbs" },
				8796093022208.0));
		units.add(new SimpleSubUnit("terabit", "terabits", new String[] { "tbt", "tbts" },
				1.0E12));
		
		units.add(new SimpleSubUnit("petabyte", "petabytes", new String[] { "pb", "pbs" },
				9007199254740992.0));
		units.add(new SimpleSubUnit("petabit", "petabits", new String[] { "pbt", "pbts" },
				1.0E15));
	}

	@Override
	public String toString()
	{
		return "digital storage";
	}
}
