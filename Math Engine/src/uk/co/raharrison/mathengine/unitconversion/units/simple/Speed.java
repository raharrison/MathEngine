package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Speed extends SimpleUnitGroup
{

	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("centimetre per day", "centimetres per day", new String[] {
				"cmd", "cm/d", "centimeter per day", "centimeters per day" }, 1.1574074074e-7));

		units.add(new SimpleSubUnit("centimetre per hour", "centimetres per hour", new String[] {
				"cmh", "cm/h", "centimeter per hour", "centimeters per hour" }, 0.0000027777777778));

		units.add(new SimpleSubUnit("centimetre per minute", "centimetres per minute",
				new String[] { "cmm", "cm/m", "centimeter per minute", "centimeters per minute" },
				0.00016666666667));

		units.add(new SimpleSubUnit("centimetre per second", "centimetres per second",
				new String[] { "cms", "cm/s", "centimeter per second", "centimeters per second" },
				0.01));

		units.add(new SimpleSubUnit("dekametre per day", "dekametres per day", new String[] {
				"dmd", "dm/d", "dekameter per day", "dekameters per day" }, 0.00011574074074));

		units.add(new SimpleSubUnit("dekametre per hour", "dekametres per hour", new String[] {
				"dmh", "dm/h", "dekameter per hour", "dekameters per hour" }, 0.0027777777778));

		units.add(new SimpleSubUnit("dekametre per minute", "dekametres per minute", new String[] {
				"dmm", "dm/m", "dekameter per minute", "dekameters per minute" }, 0.16666666667));

		units.add(new SimpleSubUnit("dekametre per second", "dekametres per second", new String[] {
				"dms", "dm/s", "dekameter per second", "dekameters per second" }, 10));

		units.add(new SimpleSubUnit("hectometre per day", "hectometres per day", new String[] {
				"hmd", "hm/d", "hectometer per day", "hectometers per day" }, 0.0011574074074));

		units.add(new SimpleSubUnit("hectometre per hour", "hectometres per hour", new String[] {
				"hmh", "hm/h", "hectometer per hour", "hectometers per hour" }, 0.027777777778));

		units.add(new SimpleSubUnit("hectometre per minute", "hectometres per minute",
				new String[] { "hmm", "hm/m", "hectometer per minute", "hectometers per minute" },
				1.6666666667));

		units.add(new SimpleSubUnit("hectometre per second", "hectometres per second",
				new String[] { "hms", "hm/s", "hectometer per second", "hectometers per second" },
				100));

		units.add(new SimpleSubUnit("foot per day", "feet per day", new String[] { "fpd" },
				0.0000035277777778));

		units.add(new SimpleSubUnit("foot per hour", "feet per hour", new String[] { "fph" },
				8.466667E-5));

		units.add(new SimpleSubUnit("foot per minute", "feet per minute", new String[] { "fpm" },
				5.08E-3));

		units.add(new SimpleSubUnit("foot per second", "feet per second", new String[] { "fps" },
				3.048E-1));

		units.add(new SimpleSubUnit("furlong per fortnight", "furlongs per fortnight",
				new String[] { "fupf" }, 1.663095E-4));

		units.add(new SimpleSubUnit("furlong per day", "furlongs per day", new String[] { "fupd" },
				0.002328337963));

		units.add(new SimpleSubUnit("furlong per hour", "furlongs per hour",
				new String[] { "fuph" }, 0.055880111111));

		units.add(new SimpleSubUnit("furlong per minute", "furlongs per minute",
				new String[] { "fupm" }, 3.3528066667));

		units.add(new SimpleSubUnit("furlong per second", "furlongs per second",
				new String[] { "fups" }, 201.1684));

		units.add(new SimpleSubUnit("inch per day", "inches per day", new String[] { "ipd" },
				2.9398148148e-7));

		units.add(new SimpleSubUnit("inch per hour", "inches per hour", new String[] { "iph" },
				7.05556E-6));

		units.add(new SimpleSubUnit("inch per minute", "inches per minute", new String[] { "ipm" },
				4.23333E-4));

		units.add(new SimpleSubUnit("inch per second", "inches per second", new String[] { "ips" },
				2.54E-2));

		units.add(new SimpleSubUnit("kilometre per day", "kilometres per day", new String[] {
				"kmd", "km/d", "kilometer per day", "kilometers per day" }, 0.011574074074));

		units.add(new SimpleSubUnit("kilometre per hour", "kilometres per hour", new String[] {
				"kmh", "km/h", "kilometer per hour", "kilometers per hour" }, 0.27777777778));

		units.add(new SimpleSubUnit("kilometre per minute", "kilometres per minute", new String[] {
				"kmm", "km/m", "kilometer per minute", "kilometers per minute" }, 16.666666667));

		units.add(new SimpleSubUnit("kilometre per second", "kilometres per second", new String[] {
				"kms", "km/s", "kilometer per second", "kilometers per second" }, 1000));

		units.add(new SimpleSubUnit("metre per day", "metres per day", new String[] { "md", "m/d",
				"meter per day", "meters per day" }, 0.000011574074074));

		units.add(new SimpleSubUnit("metre per hour", "metres per hour", new String[] { "mh",
				"m/h", "meter per hour", "meters per hour" }, 0.00027777777778));

		units.add(new SimpleSubUnit("metre per minute", "metres per minute", new String[] { "m/m",
				"meter per minute", "meters per minute" }, 0.016666666667));

		units.add(new SimpleSubUnit("metre per second", "metres per second", new String[] { "m/s",
				"meter per second", "meters per second", "benz" }, 1));

		units.add(new SimpleSubUnit("millimetre per day", "millimetres per day", new String[] {
				"mmd", "mm/d", "millimeter per day", "millimeters per day" }, 1.1574074074e-8));

		units.add(new SimpleSubUnit("millimetre per hour", "millimetres per hour", new String[] {
				"mmh", "mm/h", "millimeter per hour", "millimeters per hour" }, 2.7777777778e-7));

		units.add(new SimpleSubUnit("millimetre per minute", "millimetres per minute",
				new String[] { "mmm", "mm/m", "millimeter per minute", "millimeters per minute" },
				0.000016666666667));

		units.add(new SimpleSubUnit("millimetre per second", "millimetres per second",
				new String[] { "mms", "mm/s", "millimeter per second", "millimeters per second" },
				0.001));

		units.add(new SimpleSubUnit("millimetre per microsecond", "millimetres per microsecond",
				new String[] { "mmms", "mm/ms", "millimeter per microsecond",
						"millimeters per microsecond" }, 1000));

		units.add(new SimpleSubUnit("knot", "knots", new String[] { "kn", "kt" }, 0.514444444));

		units.add(new SimpleSubUnit("mach", "mach", new String[] { "M" }, 340.29));

		units.add(new SimpleSubUnit("mile per day", "miles per day", new String[] { "mpd" },
				0.018626666667));

		units.add(new SimpleSubUnit("mile per hour", "miles per hour", new String[] { "mph" },
				0.447049));

		units.add(new SimpleSubUnit("mile per minute", "miles per minute", new String[] { "mpm" },
				26.8224));

		units.add(new SimpleSubUnit("mile per second", "miles per second", new String[] { "mps" },
				1609.344));

		units.add(new SimpleSubUnit("yard per day", "yards per day", new String[] { "ypd" },
				0.000010583333333));

		units.add(new SimpleSubUnit("yard per hour", "yards per hour", new String[] { "yph" },
				0.000254));

		units.add(new SimpleSubUnit("yard per minute", "yards per minute", new String[] { "ypm" },
				0.01524));

		units.add(new SimpleSubUnit("yard per second", "yards per second", new String[] { "yps" },
				0.9144));

		units.add(new SimpleSubUnit("speed of sound in metal", "times speed of sound in metal",
				new String[] { "speed of sound metal" }, 5000));

		units.add(new SimpleSubUnit("speed of sound in air", "times speed of sound in air",
				new String[] { "speed of sound air" }, 340.29));

		units.add(new SimpleSubUnit("speed of sound in water", "times speed of sound in water",
				new String[] { "speed of sound water" }, 1500));

		units.add(new SimpleSubUnit("speed of light in air", "times speed of light in air",
				new String[] { "speed of light air" }, 299702547));

		units.add(new SimpleSubUnit("speed of light in glass", "times speed of light in glass",
				new String[] { "speed of light glass" }, 199861638));

		units.add(new SimpleSubUnit("speed of light in ice", "times speed of light in ice",
				new String[] { "speed of light ice" }, 228849204));

		units.add(new SimpleSubUnit("speed of light in vacuum", "times speed of light in vacuum",
				new String[] { "speed of light vacuum" }, 299792458));

		units.add(new SimpleSubUnit("speed of light in water", "times speed of light in water",
				new String[] { "speed of light water" }, 225407863));
	}

	@Override
	public String toString()
	{
		return "speed";
	}
}
