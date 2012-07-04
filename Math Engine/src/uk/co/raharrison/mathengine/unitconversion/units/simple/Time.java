package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Time extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("atomic unit of time", "atomic units of time", new String[] {
				"au", "aus" }, 2.418884254E-17));
		units.add(new SimpleSubUnit("callippic cycle", "callippic cycles", new String[] { "cc",
				"ccs" }, 2.3983776E9));
		units.add(new SimpleSubUnit("century", "centuries", new String[] { "c", "cs" },
				3153600000.0));
		units.add(new SimpleSubUnit("day", "days", new String[] { "d", "ds" }, 86400));
		units.add(new SimpleSubUnit("decade", "decades", new String[] { "dec", "decs" },
				315360000.0));
		units.add(new SimpleSubUnit("fortnight", "fortnights", new String[] { "f", "fs", "fort",
				"forts" }, 1209600));
		units.add(new SimpleSubUnit("helek", "heleks", new String[] { "hlk", "hlks" }, 10.0 / 3.0));
		units.add(new SimpleSubUnit("hipparchic cycle", "hipparchic cycles", new String[] { "hc",
				"hcs" }, 9.593424E9));
		units.add(new SimpleSubUnit("hour", "hours", new String[] { "h", "hs" }, 3600));
		units.add(new SimpleSubUnit("jiffy", "jiffies", new String[] { "j", "js" }, 1.0 / 60.0));
		units.add(new SimpleSubUnit("centiday", "centidays", new String[] { "cd", "cds" }, 864));
		units.add(new SimpleSubUnit("lustre", "lustres", new String[] { "lstr", "lstrs" }, 1.5768E8));
		units.add(new SimpleSubUnit("metonic cycle", "metonic cycles",
				new String[] { "mc", "mcs" }, 5.99616E8));
		units.add(new SimpleSubUnit("millennium", "millenniums", new String[] { "mm", "mms" },
				31536000000.0));
		units.add(new SimpleSubUnit("milliday", "millidays", new String[] { "md", "mds" }, 86.4));
		units.add(new SimpleSubUnit("millisecond", "milliseconds", new String[] { "ms", "mss" },
				0.001));
		units.add(new SimpleSubUnit("minute", "minutes", new String[] { "m", "min", "mins" }, 60));
		units.add(new SimpleSubUnit("moment", "moments", new String[] { "mom", "moms" }, 90));
		units.add(new SimpleSubUnit("month", "months", new String[] { "mo", "mos" }, 2628000));
		units.add(new SimpleSubUnit("quarter", "quarters", new String[] { "q", "qs" }, 7884000));
		units.add(new SimpleSubUnit("second", "seconds", new String[] { "s", "sec", "secs" }, 1));
		units.add(new SimpleSubUnit("shake", "shakes", new String[] { "shk", "shks" }, 1.0E-8));
		units.add(new SimpleSubUnit("sigma", "sigmas", new String[] { "sgma", "sgmas" }, 1.0E-6));
		units.add(new SimpleSubUnit("sothic cycle", "sothic cycles", new String[] { "sc", "scs" },
				4.6074096E10));
		units.add(new SimpleSubUnit("week", "weeks", new String[] { "w", "ws" }, 604800));
		units.add(new SimpleSubUnit("year", "years", new String[] { "y", "ys" }, 31536000.0));
	}

	@Override
	public String toString()
	{
		return "time";
	}
}
