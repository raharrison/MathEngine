package uk.co.raharrison.mathengine.unitconversion.units.simple.timezones;

import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleUnitGroup;

public class TimeZones extends SimpleUnitGroup
{
	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		amount = Math.abs(amount);
		amount = Math.round(amount);

		TimeZoneSimpleSubUnit from, to;

		if ((from = (TimeZoneSimpleSubUnit) params.getFrom()) != null
				&& (to = (TimeZoneSimpleSubUnit) params.getTo()) != null)
		{
			String time = Long.toString((long) amount);

			if (time.length() > 6)
				throw new IllegalArgumentException("Expected 24 hour time");

			amount = normaliseTime(time);

			int hour = (int) ((amount % 1000000) / 10000);
			int minute = (int) ((amount % 10000) / 100);
			int second = (int) ((amount % 100) / 1);

			Calendar cal = new GregorianCalendar(2012, Calendar.JUNE, 12, hour, minute, second);

			int fromHourShift = (int) ((from.getConversion() % 10000) / 100);
			int fromMinuteShift = (int) ((from.getConversion() % 100) / 1);

			int toHourShift = (int) ((to.getConversion() % 10000) / 100);
			int toMinuteShift = (int) ((to.getConversion() % 100) / 1);

			cal.add(Calendar.MINUTE, -fromMinuteShift + toMinuteShift);
			cal.add(Calendar.HOUR_OF_DAY, -fromHourShift + toHourShift);

			System.out.println(cal.getTime().toString());

			if (second != 0)
				return Double.parseDouble("" + formatForTime(cal.get(Calendar.HOUR_OF_DAY))
						+ formatForTime(cal.get(Calendar.MINUTE)) + formatForTime(cal.get(Calendar.SECOND)));
			else
				return Double.parseDouble("" + formatForTime(cal.get(Calendar.HOUR_OF_DAY))
						+ formatForTime(cal.get(Calendar.MINUTE)));
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}

	private String formatForTime(int i)
	{
		if(i < 10)
			return "0" + i;
		else
			return "" + i;
	}

	@Override
	protected void fillUnits()
	{
		units.add(new TimeZoneSimpleSubUnit("UTC", new String[] { "gmt", "greenwich mean time" }, 0));
		units.add(new TimeZoneSimpleSubUnit("UTC", new String[] { "utc",
				"coordinated universal time" }, 0));
		units.add(new TimeZoneSimpleSubUnit("UTC", new String[] { "wet", "western european time" },
				0));

		units.add(new TimeZoneSimpleSubUnit("UTC+01", new String[] { "dft", "cet",
				"central european time", "bst", "british summer time", "met",
				"middle european time", "ist", "irish summer time", "wedt",
				"western european daylight time", "wat", "western africa time", "west",
				"western european summer time" }, 100));

		units.add(new TimeZoneSimpleSubUnit("UTC+02", new String[] { "eet",
				"eastern european time", "cat", "central africa time", "cedt",
				"central european daylight time", "cest", "central european summer time", "mest",
				"middle european saving time", "haec", "heure avancee d'europe centrale", "ist",
				"israel standard time", "sast", "south african standard time" }, 200));

		units.add(new TimeZoneSimpleSubUnit("UTC+03", new String[] { "eat", "east africa time",
				"fet", "further eastern european time", "eedt", "eastern european dayligh time",
				"eest", "eastern european summer time", "ast", "arabic standard time", "idt",
				"israeli daylight time" }, 300));

		units.add(new TimeZoneSimpleSubUnit("UTC+03:30", new String[] { "irst",
				"iran standard time" }, 330));

		units.add(new TimeZoneSimpleSubUnit("UTC+04", new String[] { "get",
				"georgia standard time", "ast", "arabian standard time", "amt", "armenia time",
				"azt", "azerbaiijan time", "mut", "mauritious time", "msk", "moscow time", "gst",
				"gulf standard time", "sct", "seychelles time", "ret", "reunion time", "samt",
				"samara time" }, 400));

		units.add(new TimeZoneSimpleSubUnit("UTC+04:30",
				new String[] { "aft", "afghanistan time" }, 430));

		units.add(new TimeZoneSimpleSubUnit("UTC+05", new String[] { "amst", "armenia summer time",
				"hmt", "heard and mcdonald islands time", "pkt", "pakistan standard time", "yekt",
				"yekaterinburg time" }, 500));

		units.add(new TimeZoneSimpleSubUnit("UTC+05:30", new String[] { "ist",
				"indian standard time", "slt", "sri lanka time" }, 530));

		units.add(new TimeZoneSimpleSubUnit("UTC+05:45", new String[] { "npt", "nepal time" }, 545));

		units.add(new TimeZoneSimpleSubUnit("UTC+06", new String[] { "biot",
				"british indian ocean time", "btt", "bhutan time", "bst",
				"bangladesh standard time", "omst", "omsk time" }, 600));

		units.add(new TimeZoneSimpleSubUnit("UTC+06:30", new String[] { "cct",
				"cocos islands time", "myst", "myanmar standard time" }, 630));

		units.add(new TimeZoneSimpleSubUnit("UTC+07", new String[] { "cxt",
				"christmas island time", "krat", "krasnoyarsk time", "ict", "indochina time",
				"tha", "thailand standard time" }, 700));

		units.add(new TimeZoneSimpleSubUnit("UTC+08", new String[] { "ct", "china time", "chst",
				"china standard time", "act", "asean common time", "awst",
				"australian western standard time", "bdt", "brunei time", "myt", "malaysia time",
				"mlst", "malaysian standard time", "hkt", "hong kong time", "irkt", "irkutsk time",
				"phst", "philippine standard time", "sgt", "singapore time", "sgst",
				"singapore standard time", "wst", "western standard time" }, 800));

		units.add(new TimeZoneSimpleSubUnit("UTC+09", new String[] { "awdt",
				"australian western daylight time", "kst", "korea standard time", "jst",
				"japan standard time", "yakt", "yakutsk time" }, 900));

		units.add(new TimeZoneSimpleSubUnit("UTC+09:30", new String[] { "acst",
				"australian central standard time" }, 930));

		units.add(new TimeZoneSimpleSubUnit("UTC+10", new String[] { "chst",
				"chamorro standard time", "aest", "australian eastern standard time", "vlat",
				"vladivostok time" }, 1000));

		units.add(new TimeZoneSimpleSubUnit("UTC+10:30", new String[] { "acdt",
				"australian central daylight time", "lhst", "lord howe standard time" }, 1030));

		units.add(new TimeZoneSimpleSubUnit("UTC+11", new String[] { "aedt",
				"australian eastern daylight time", "magt", "magadan time", "sbt",
				"solomon islands time" }, 1100));

		units.add(new TimeZoneSimpleSubUnit("UTC+11:30", new String[] { "nft", "norfolk time" },
				1130));

		units.add(new TimeZoneSimpleSubUnit("UTC+12",
				new String[] { "gilt", "gilbert island time", "fjt", "fiji time", "nzst",
						"new zealand standard time", "pett", "kamchatka time" }, 1200));

		units.add(new TimeZoneSimpleSubUnit("UTC+12:45", new String[] { "chast",
				"chatham standard time" }, 1245));

		units.add(new TimeZoneSimpleSubUnit("UTC+13", new String[] { "phot", "phoenix island time",
				"nzdt", "new zealand daylight time" }, 1300));

		units.add(new TimeZoneSimpleSubUnit("UTC+13:45", new String[] { "chadt",
				"chatham island daylight time" }, 1345));

		units.add(new TimeZoneSimpleSubUnit("UTC+14", new String[] { "lint", "line islands time" },
				1400));

		units.add(new TimeZoneSimpleSubUnit("UTC-01", new String[] { "cvt", "cape verde time",
				"azost", "azores standard time" }, -100));

		units.add(new TimeZoneSimpleSubUnit("UTC-02", new String[] { "gst", "south georgia time",
				"sandwich islands time", "uyst", "uruguay summer time" }, -200));

		units.add(new TimeZoneSimpleSubUnit("UTC-02:30", new String[] { "ndt",
				"newfoundland daylight time" }, -230));

		units.add(new TimeZoneSimpleSubUnit("UTC-03", new String[] { "gft", "french guiana time",
				"fkst", "falkland islands summer time", "clst", "chile summer time", "art",
				"argentina time", "adr", "atlantic daylight time", "brt", "brasilia time", "uyt",
				"uruguay standard time" }, -300));

		units.add(new TimeZoneSimpleSubUnit("UTC-03:30", new String[] { "nt", "newfoundland time",
				"nst", "newfoundland standard time" }, -330));

		units.add(new TimeZoneSimpleSubUnit("UTC-04", new String[] { "fkt",
				"falkland islands time", "edt", "eastern daylight time", "ect",
				"eastern caribbean time", "cost", "colombia summer time", "clt",
				"chile standard time", "ast", "atlantic standard time", "bot", "bolivia time",
				"gyt", "guyana time" }, -400));

		units.add(new TimeZoneSimpleSubUnit("UTC-04:30", new String[] { "vet",
				"venezuelan standard time" }, -430));

		units.add(new TimeZoneSimpleSubUnit("UTC-05", new String[] { "ecdt", "ecuador time", "est",
				"eastern standard time", "cot", "colombia time", "cdt", "central daylight time" },
				-500));

		units.add(new TimeZoneSimpleSubUnit("UTC-06", new String[] { "galt", "galapagos time",
				"cst", "central standard time", "east", "easter island standard time", "mdt",
				"mountain daylight time" }, -600));

		units.add(new TimeZoneSimpleSubUnit("UTC-07", new String[] { "mst",
				"mountain standard time", "pdt", "pacific daylight time" }, -700));

		units.add(new TimeZoneSimpleSubUnit("UTC-08", new String[] { "cist",
				"clipperton island standard time", "akdt", "alaska standard time", "pst",
				"pacific standard time" }, -800));

		units.add(new TimeZoneSimpleSubUnit("UTC-09", new String[] { "git", "gambier island time",
				"akst", "alaska standard time", "hadt", "hawaii aleutian daylight time" }, -900));

		units.add(new TimeZoneSimpleSubUnit("UTC-09:30", new String[] { "mit",
				"marquesas islands time" }, -930));

		units.add(new TimeZoneSimpleSubUnit("UTC-10", new String[] { "ckt", "cook island time",
				"hast", "hawaii aleutian standard time", "hst", "hawaii standard time", "taht",
				"tahiti time" }, -1000));

		units.add(new TimeZoneSimpleSubUnit("UTC-11",
				new String[] { "sst", "samoa standard time" }, -1100));

		units.add(new TimeZoneSimpleSubUnit("UTC-12", new String[] { "bit", "baker island time" },
				-1200));

	}

	private double normaliseTime(String time)
	{
		StringBuilder builder = new StringBuilder(time);

		while (builder.length() < 5)
			builder.append("0");

		return Double.parseDouble(builder.toString());

	}

	@Override
	public String toString()
	{
		return "time zones";
	}
}
