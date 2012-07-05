package uk.co.raharrison.mathengine.unitconversion.units.simple;

import java.util.Date;
import java.util.Map;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;

public class Currency extends SimpleUnitGroup
{
	private Map<String, Double> currencies;
	private Date lastUpdated;

	private static final String url = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";
	
	public Currency()
	{
		this(false);
	}
	
	public Currency(boolean downloadCurrencies)
	{
		super();
	}

	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("U.S dollar", "U.S dollars", new String[] {"usd", "$"}, 1.2426));
		units.add(new SimpleSubUnit("Japanese yen", "Japanese yen", new String[] {"jpy"}, 99.14));
		units.add(new SimpleSubUnit("Bulgarian lev", "Bulgarian levove", new String[] {"bgn"}, 1.9558));
		units.add(new SimpleSubUnit("Czech koruna", "Czech koruny", new String[] {"czk"}, 25.457));
		units.add(new SimpleSubUnit("Danish krone", "Danish kroner", new String[] {"dkk"}, 7.4367));
		units.add(new SimpleSubUnit("British pound", "British pounds", new String[] {"gbp", "£", "british pound", "british pounds", "pound sterling", "pounds sterling"}, 0.7984));
		units.add(new SimpleSubUnit("Hungarian forint", "Hungarian forint", new String[] {"huf"}, 285.95));
		units.add(new SimpleSubUnit("Lithuanian litas", "Lithuanian litai", new String[] {"ltl"}, 3.4528));
		units.add(new SimpleSubUnit("Latvian lat", "Latvian lati", new String[] {"lvl"}, 0.6959));
		units.add(new SimpleSubUnit("Polish zloty", "Polish zloty", new String[] {"pln"}, 4.206));
		units.add(new SimpleSubUnit("Romanian leu", "Romanian lei", new String[] {"ron"}, 4.4865));
		units.add(new SimpleSubUnit("Swedish krona", "Swedish kronor", new String[] {"sek"}, 8.6177));
		units.add(new SimpleSubUnit("Swiss franc", "Swiss francs", new String[] {"chf"}, 1.2012));
		units.add(new SimpleSubUnit("Norwegian krone", "Norwegian kroner", new String[] {"nok"}, 7.476));
		units.add(new SimpleSubUnit("Croatian kuna", "Croatian kuna", new String[] {"hrk"}, 7.4963));
		units.add(new SimpleSubUnit("Russian ruble", "Russian rubles", new String[] {"rub"}, 40.351));
		units.add(new SimpleSubUnit("Turkish lira", "Turkish lira", new String[] {"try"}, 2.2467));
		units.add(new SimpleSubUnit("Australian dollar", "Australian dollars", new String[] {"aud"}, 1.2071));
		units.add(new SimpleSubUnit("Brazil real", "Brazil reais", new String[] {"brl"}, 2.5116));
		units.add(new SimpleSubUnit("Canadian dollar", "Canadian dollars", new String[] {"cad"}, 1.258));
		units.add(new SimpleSubUnit("Chinese yuan", "Chinese yuan", new String[] {"cny"}, 7.8992));
		units.add(new SimpleSubUnit("Hong Kong dollar", "Hong Kong dollars", new String[] {"hkd"}, 9.636));
		units.add(new SimpleSubUnit("Indonesian rupiah", "Indonesian rupiah", new String[] {"idr"}, 11639.91));
		units.add(new SimpleSubUnit("Israeli shekel", "Israeli shekalim", new String[] {"ils"}, 4.8683));
		units.add(new SimpleSubUnit("Indian rupee", "Indian rupees", new String[] {"inr"}, 68.284));
		units.add(new SimpleSubUnit("South Korean won", "South Korean won", new String[] {"krw"}, 1409.68));
		units.add(new SimpleSubUnit("Mexican peso", "Mexican pesos", new String[] {"mxn"}, 16.5626));
		units.add(new SimpleSubUnit("Malaysian ringgit", "Malaysian ringgit", new String[] {"myr"}, 3.9313));
		units.add(new SimpleSubUnit("New Zealand dollar", "New Zealand dollars", new String[] {"nzd"}, 1.5448));
		units.add(new SimpleSubUnit("Philippine peso", "Philippine pesos", new String[] {"php"}, 51.822));
		units.add(new SimpleSubUnit("Singapore dollar", "Singapore dollars", new String[] {"sgd"}, 1.5695));
		units.add(new SimpleSubUnit("Thai baht", "Thai baht", new String[] {"thb"}, 39.192));
		units.add(new SimpleSubUnit("South African rand", "South African rand", new String[] {"zar"}, 10.0836));
		units.add(new SimpleSubUnit("Euro", "Euros", new String[] {"eur", "$"}, 1.0));
	}
	
	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			return amount * to.getConversion() / from.getConversion();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}

	@Override
	public String toString()
	{
		return "currency";
	}

}
