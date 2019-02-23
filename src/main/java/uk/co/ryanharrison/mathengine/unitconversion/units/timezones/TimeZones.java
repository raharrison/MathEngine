package uk.co.ryanharrison.mathengine.unitconversion.units.timezones;

import uk.co.ryanharrison.mathengine.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleSubUnit;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnitGroup;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TimeZones extends SimpleUnitGroup
{
	private CityTimeZoneProvider cityTimeZoneProvider;
	private static final String CITYPROVIDERARCHIVEPATH = "timezones.zip";
	
	public TimeZones()
	{
		cityTimeZoneProvider = new CityTimeZoneProvider(CITYPROVIDERARCHIVEPATH);
	} 
	
	@Override
	protected BigRational doConversion(Conversion params)
	{
		double amount = Math.abs(params.getValue().doubleValue());
		amount = Math.round(amount);

		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			String time = Long.toString((long) amount);

			if (time.length() > 6)
				throw new IllegalArgumentException("Expected 24 hour time");

			amount = normaliseTime(time);

			int hour = (int) ((amount % 1000000) / 10000);
			int minute = (int) ((amount % 10000) / 100);
			int second = (int) ((amount % 100) / 1);

			Calendar cal = new GregorianCalendar(2012, Calendar.JUNE, 12, hour, minute, second);
			cal.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

//			int fromHourShift = (int) ((from.getConversion() % 10000) / 100);
//			int fromMinuteShift = (int) ((from.getConversion() % 100) / 1);
//
//			int toHourShift = (int) ((to.getConversion() % 10000) / 100);
//			int toMinuteShift = (int) ((to.getConversion() % 100) / 1);

//			cal.add(Calendar.MINUTE, -fromMinuteShift + toMinuteShift);
//			cal.add(Calendar.HOUR_OF_DAY, -fromHourShift + toHourShift);
			
			cal.add(Calendar.SECOND, (int) (-from.getConversion().doubleValue() + to.getConversion().doubleValue()));

			// System.out.println(cal.getTime().toString());

			if (second != 0)
				return new BigRational("" + formatForTime(cal.get(Calendar.HOUR_OF_DAY))
						+ formatForTime(cal.get(Calendar.MINUTE))
						+ formatForTime(cal.get(Calendar.SECOND)));
			else
				return new BigRational("" + formatForTime(cal.get(Calendar.HOUR_OF_DAY))
						+ formatForTime(cal.get(Calendar.MINUTE)));
		}

		return null;
	}

	private String formatForTime(int i)
	{
		if (i < 10)
			return "0" + i;
		else
			return "" + i;
	}

	private double normaliseTime(String time)
	{
		StringBuilder builder = new StringBuilder(time);

		// Length of 6 if even length (no zero in front of hour), otherwise 5
		int finalLength = time.length() % 2 == 0 ? 6 : 5;

		while (builder.length() < finalLength)
			builder.append("0");

		return Double.parseDouble(builder.toString());

	}

	@Override
	public String toString()
	{
		return "time zones";
	}
	
	private void updateTimeZones(CityTimeZoneProvider provider)
	{
		try
		{
			List<TimeZone> timezones = provider.process();
			
			for (TimeZone timeZone : timezones)
			{
				SimpleSubUnit sub = new SimpleSubUnit();
				sub.setSingular(timeZone.getCity());
				sub.setPlural(timeZone.getCity());
				sub.setConversion(timeZone.getOffset());
				units.add(sub);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("Unable to update timezones", e);
		}
	}

	@Override
	public void update()
	{
		updateTimeZones(cityTimeZoneProvider);
	}
}
