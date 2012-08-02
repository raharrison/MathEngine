package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Speed extends SimpleUnitGroup
{

	@Override
	protected void fillUnits()
	{
		// TODO : Add more aliases
		units.add(new SimpleSubUnit("centimetre per day", "centimetres per day", new String[] {
				"cmd", "cm/d", "centimeter per day", "centimeters per day", "centimetre/day", "centimetres/day", "centimeter/day", "centimeters/day" }, 1.1574074074e-7));

		units.add(new SimpleSubUnit("centimetre per hour", "centimetres per hour", new String[] {
				"cmh", "cm/h", "centimeter per hour", "centimeters per hour", "centimetre/hour", "centimetres/hour", "centimeter/hour", "centimeters/hour" }, 0.0000027777777778));

		units.add(new SimpleSubUnit("centimetre per minute", "centimetres per minute",
				new String[] { "cmm", "cm/m", "centimeter per minute", "centimeters per minute", "centimeters per hour", "centimetre/minute", "centimetres/minute", "centimeter/minute", "centimeters/minute" },
				0.00016666666667));

		units.add(new SimpleSubUnit("centimetre per second", "centimetres per second",
				new String[] { "cms", "cm/s", "centimeter per second", "centimeters per second", "centimetre/second", "centimetres/second", "centimeter/second", "centimeters/second" },
				0.01));

		units.add(new SimpleSubUnit("dekametre per day", "dekametres per day", new String[] {
				"dmd", "dm/d", "dekameter per day", "dekameters per day", "dekametre/day", "dekametres/day", "dekameter/day", "dekameters/day" }, 0.00011574074074));

		units.add(new SimpleSubUnit("dekametre per hour", "dekametres per hour", new String[] {
				"dmh", "dm/h", "dekameter per hour", "dekameters per hour", "dekametre/hour", "dekametres/hour", "dekameter/hour", "dekameters/hour" }, 0.0027777777778));

		units.add(new SimpleSubUnit("dekametre per minute", "dekametres per minute", new String[] {
				"dmm", "dm/m", "dekameter per minute", "dekameters per minute", "dekametre/minute", "dekametres/minute", "dekameter/minute", "dekameters/minute" }, 0.16666666667));

		units.add(new SimpleSubUnit("dekametre per second", "dekametres per second", new String[] {
				"dms", "dm/s", "dekameter per second", "dekameters per second", "dekametre/second", "dekametres/second", "dekameter/second", "dekameters/second" }, 10));

		units.add(new SimpleSubUnit("hectometre per day", "hectometres per day", new String[] {
				"hmd", "hm/d", "hectometer per day", "hectometers per day", "hectometre/day", "hectometres/day", "hectometer/day", "hectometers/day" }, 0.0011574074074));

		units.add(new SimpleSubUnit("hectometre per hour", "hectometres per hour", new String[] {
				"hmh", "hm/h", "hectometer per hour", "hectometers per hour", "hectometre/hour", "hectometres/hour", "hectometer/hour", "hectometers/hour" }, 0.027777777778));

		units.add(new SimpleSubUnit("hectometre per minute", "hectometres per minute",
				new String[] { "hmm", "hm/m", "hectometer per minute", "hectometers per minute", "hectometre/minute", "hectometres/minute", "hectometer/minute", "hectometers/minute" },
				1.6666666667));

		units.add(new SimpleSubUnit("hectometre per second", "hectometres per second",
				new String[] { "hms", "hm/s", "hectometer per second", "hectometers per second", "hectometre/second", "hectometres/second", "hectometer/second", "hectometers/second" },
				100));

		units.add(new SimpleSubUnit("foot per day", "feet per day", new String[] { "fpd", "foot/day", "feet/day" },
				0.0000035277777778));

		units.add(new SimpleSubUnit("foot per hour", "feet per hour", new String[] { "fph", "foot/hour", "feet/hour" },
				8.466667E-5));

		units.add(new SimpleSubUnit("foot per minute", "feet per minute", new String[] { "fpm", "foot/minute", "feet/minute" },
				5.08E-3));

		units.add(new SimpleSubUnit("foot per second", "feet per second", new String[] { "fps", "foot/second", "feet/second" },
				3.048E-1));

		units.add(new SimpleSubUnit("furlong per fortnight", "furlongs per fortnight",
				new String[] { "fupf", "furlong/fortnight", "furlongs/fortnight" }, 1.663095E-4));

		units.add(new SimpleSubUnit("furlong per day", "furlongs per day", new String[] { "fupd", "furlong/day", "furlongs/day" },
				0.002328337963));

		units.add(new SimpleSubUnit("furlong per hour", "furlongs per hour",
				new String[] { "fuph", "furlong/hour", "furlongs/hour" }, 0.055880111111));

		units.add(new SimpleSubUnit("furlong per minute", "furlongs per minute",
				new String[] { "fupm", "furlong/minute", "furlongs/minute" }, 3.3528066667));

		units.add(new SimpleSubUnit("furlong per second", "furlongs per second",
				new String[] { "fups", "furlong/second", "furlongs/second" }, 201.1684));

		units.add(new SimpleSubUnit("inch per day", "inches per day", new String[] { "ipd", "inch/day", "inches/day" },
				2.9398148148e-7));

		units.add(new SimpleSubUnit("inch per hour", "inches per hour", new String[] { "iph", "inch/hour", "inches/hour" },
				7.05556E-6));

		units.add(new SimpleSubUnit("inch per minute", "inches per minute", new String[] { "ipm", "inch/minute", "inches/minute" },
				4.23333E-4));

		units.add(new SimpleSubUnit("inch per second", "inches per second", new String[] { "ips", "inch/second", "inches/second" },
				2.54E-2));

		units.add(new SimpleSubUnit("kilometre per day", "kilometres per day", new String[] {
				"kmd", "km/d", "kilometer per day", "kilometers per day", "kilometre/day", "kilometres/day", "kilometer/day", "kilometers/day" }, 0.011574074074));

		units.add(new SimpleSubUnit("kilometre per hour", "kilometres per hour", new String[] {
				"kmh", "km/h", "kilometer per hour", "kilometers per hour", "kilometre/hour", "kilometres/hour", "kilometer/hour", "kilometers/hour" }, 0.27777777778));

		units.add(new SimpleSubUnit("kilometre per minute", "kilometres per minute", new String[] {
				"kmm", "km/m", "kilometer per minute", "kilometers per minute", "kilometre/minute", "kilometres/minute", "kilometer/minute", "kilometers/minute" }, 16.666666667));

		units.add(new SimpleSubUnit("kilometre per second", "kilometres per second", new String[] {
				"kms", "km/s", "kilometer per second", "kilometers per second", "kilometre/second", "kilometres/second", "kilometer/second", "kilometers/second" }, 1000));

		units.add(new SimpleSubUnit("metre per day", "metres per day", new String[] { "md", "m/d",
				"meter per day", "meters per day", "metre/day", "metres/day", "meter/day", "meters/day" }, 0.000011574074074));

		units.add(new SimpleSubUnit("metre per hour", "metres per hour", new String[] { "mh",
				"m/h", "meter per hour", "meters per hour", "metre/hour", "metres/hour", "meter/hour", "meters/hour" }, 0.00027777777778));

		units.add(new SimpleSubUnit("metre per minute", "metres per minute", new String[] { "m/m",
				"meter per minute", "meters per minute", "metre/minute", "metres/minute", "meter/minute", "meters/minute" }, 0.016666666667));

		units.add(new SimpleSubUnit("metre per second", "metres per second", new String[] { "m/s",
				"meter per second", "meters per second", "benz", "metre/second", "metres/second", "meter/second", "meters/second" }, 1));

		units.add(new SimpleSubUnit("millimetre per day", "millimetres per day", new String[] {
				"mmd", "mm/d", "millimeter per day", "millimeters per day", "millimetre/day", "millimetres/day", "millimeter/day", "millimeters/day" }, 1.1574074074e-8));

		units.add(new SimpleSubUnit("millimetre per hour", "millimetres per hour", new String[] {
				"mmh", "mm/h", "millimeter per hour", "millimeters per hour", "millimetre/hour", "millimetres/hour", "millimeter/hour", "millimeters/hour" }, 2.7777777778e-7));

		units.add(new SimpleSubUnit("millimetre per minute", "millimetres per minute",
				new String[] { "mmm", "mm/m", "millimeter per minute", "millimeters per minute", "millimetre/minute", "millimetres/minute", "millimeter/minute", "millimeters/minute" },
				0.000016666666667));

		units.add(new SimpleSubUnit("millimetre per second", "millimetres per second",
				new String[] { "mms", "mm/s", "millimeter per second", "millimeters per second", "millimetre/second", "millimetres/second", "millimeter/second", "millimeters/second" },
				0.001));

		units.add(new SimpleSubUnit("millimetre per microsecond", "millimetres per microsecond",
				new String[] { "mmms", "mm/ms", "millimeter per microsecond",
						"millimeters per microsecond", "millimetre/microsecond", "millimetres/microsecond", "millimeter/microsecond", "millimeters/microsecond" }, 1000));

		units.add(new SimpleSubUnit("knot", "knots", new String[] { "kn", "kt" }, 0.514444444));

		units.add(new SimpleSubUnit("mach", "mach", new String[] { "M" }, 340.29));

		units.add(new SimpleSubUnit("mile per day", "miles per day", new String[] { "mpd", "mile/day", "miles/day" },
				0.018626666667));

		units.add(new SimpleSubUnit("mile per hour", "miles per hour", new String[] { "mph", "mile/hour", "miles/hour" },
				0.447049));

		units.add(new SimpleSubUnit("mile per minute", "miles per minute", new String[] { "mpm", "mile/minute", "miles/minute" },
				26.8224));

		units.add(new SimpleSubUnit("mile per second", "miles per second", new String[] { "mps", "mile/second", "miles/second"  },
				1609.344));

		units.add(new SimpleSubUnit("yard per day", "yards per day", new String[] { "ypd", "yard/day", "yards/day", "yd/day", "yd/dy" },
				0.000010583333333));

		units.add(new SimpleSubUnit("yard per hour", "yards per hour", new String[] { "yph", "yard/hour", "yards/hour", "yd/hour", "yd/hr" },
				0.000254));

		units.add(new SimpleSubUnit("yard per minute", "yards per minute", new String[] { "ypm", "yard/minute", "yards/minute", "yd/minute", "yd/min" },
				0.01524));

		units.add(new SimpleSubUnit("yard per second", "yards per second", new String[] { "yps", "yard/second", "yards/second", "yd/second", "yd/sec" },
				0.9144));

		units.add(new SimpleSubUnit("speed of sound in metal", "times speed of sound in metal",
				new String[] { "speed of sound metal","times speed of sound metal", "sound/metal" }, 5000));

		units.add(new SimpleSubUnit("speed of sound in air", "times speed of sound in air",
				new String[] { "s", "speed of sound air","times speed of sound air", "sound/air" }, 340.29));

		units.add(new SimpleSubUnit("speed of sound in water", "times speed of sound in water",
				new String[] { "speed of sound water","times speed of sound water", "sound/water" }, 1500));

		units.add(new SimpleSubUnit("speed of light in air", "times speed of light in air",
				new String[] { "speed of light air","times speed of light air", "light/air" }, 299702547));

		units.add(new SimpleSubUnit("speed of light in glass", "times speed of light in glass",
				new String[] { "speed of light glass","times speed of light glass", "light/glass" }, 199861638));

		units.add(new SimpleSubUnit("speed of light in ice", "times speed of light in ice",
				new String[] { "speed of light ice","times speed of light ice", "light/ice" }, 228849204));

		units.add(new SimpleSubUnit("speed of light in vacuum", "times speed of light in vacuum",
				new String[] { "c", "speed of light vacuum","times speed of light vacuum", "light/vacuum", "speed of light", "times speed of light" }, 299792458));

		units.add(new SimpleSubUnit("speed of light in water", "times speed of light in water",
				new String[] { "speed of light water","times speed of light water", "light/water" }, 225407863));
	}

	@Override
	public String toString()
	{
		return "speed";
	}
}
