package uk.co.raharrison.mathengine.unitconversion.units.simple.currency;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.raharrison.mathengine.unitconversion.units.ConversionParams;
import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleSubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleUnitGroup;

public class Currency extends SimpleUnitGroup
{
	private Date lastUpdated;

	private static final String url = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";

	public Currency()
	{
		this(false);
	}

	public Currency(boolean downloadCurrencies)
	{
		super();
		lastUpdated = new GregorianCalendar(2012, Calendar.JULY, 5).getTime();

		if (downloadCurrencies)
		{
			update();
		}
	}

	@Override
	protected double doConversion(ConversionParams params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (CurrencySimpleSubUnit) params.getFrom()) != null
				&& (to = (CurrencySimpleSubUnit) params.getTo()) != null)
		{
			return amount * to.getConversion() / from.getConversion();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}

	private Document download(String path) throws ParserConfigurationException,
			MalformedURLException, SAXException, IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(new URL(path).openStream());

		return doc;
	}

	@Override
	protected void fillUnits()
	{
		units.add(new CurrencySimpleSubUnit("U.S dollar", "U.S dollars",
				new String[] { "usd", "$" }, 1.2426));
		units.add(new CurrencySimpleSubUnit("Japanese yen", "Japanese yen", new String[] { "jpy" },
				99.14));
		units.add(new CurrencySimpleSubUnit("Bulgarian lev", "Bulgarian levove",
				new String[] { "bgn" }, 1.9558));
		units.add(new CurrencySimpleSubUnit("Czech koruna", "Czech koruny", new String[] { "czk" },
				25.457));
		units.add(new CurrencySimpleSubUnit("Danish krone", "Danish kroner",
				new String[] { "dkk" }, 7.4367));
		units.add(new CurrencySimpleSubUnit("British pound", "British pounds", new String[] {
				"gbp", "£", "british pound", "british pounds", "pound sterling", "pounds sterling",
				"pound", "pounds" }, 0.7984));
		units.add(new CurrencySimpleSubUnit("Hungarian forint", "Hungarian forint",
				new String[] { "huf" }, 285.95));
		units.add(new CurrencySimpleSubUnit("Lithuanian litas", "Lithuanian litai",
				new String[] { "ltl" }, 3.4528));
		units.add(new CurrencySimpleSubUnit("Latvian lat", "Latvian lati", new String[] { "lvl" },
				0.6959));
		units.add(new CurrencySimpleSubUnit("Polish zloty", "Polish zloty", new String[] { "pln" },
				4.206));
		units.add(new CurrencySimpleSubUnit("Romanian leu", "Romanian lei", new String[] { "ron" },
				4.4865));
		units.add(new CurrencySimpleSubUnit("Swedish krona", "Swedish kronor",
				new String[] { "sek" }, 8.6177));
		units.add(new CurrencySimpleSubUnit("Swiss franc", "Swiss francs", new String[] { "chf" },
				1.2012));
		units.add(new CurrencySimpleSubUnit("Norwegian krone", "Norwegian kroner",
				new String[] { "nok" }, 7.476));
		units.add(new CurrencySimpleSubUnit("Croatian kuna", "Croatian kuna",
				new String[] { "hrk" }, 7.4963));
		units.add(new CurrencySimpleSubUnit("Russian ruble", "Russian rubles",
				new String[] { "rub" }, 40.351));
		units.add(new CurrencySimpleSubUnit("Turkish lira", "Turkish lira", new String[] { "try" },
				2.2467));
		units.add(new CurrencySimpleSubUnit("Australian dollar", "Australian dollars",
				new String[] { "aud" }, 1.2071));
		units.add(new CurrencySimpleSubUnit("Brazil real", "Brazil reais", new String[] { "brl" },
				2.5116));
		units.add(new CurrencySimpleSubUnit("Canadian dollar", "Canadian dollars",
				new String[] { "cad" }, 1.258));
		units.add(new CurrencySimpleSubUnit("Chinese yuan", "Chinese yuan", new String[] { "cny" },
				7.8992));
		units.add(new CurrencySimpleSubUnit("Hong Kong dollar", "Hong Kong dollars",
				new String[] { "hkd" }, 9.636));
		units.add(new CurrencySimpleSubUnit("Indonesian rupiah", "Indonesian rupiah",
				new String[] { "idr" }, 11639.91));
		units.add(new CurrencySimpleSubUnit("Israeli shekel", "Israeli shekalim",
				new String[] { "ils" }, 4.8683));
		units.add(new CurrencySimpleSubUnit("Indian rupee", "Indian rupees",
				new String[] { "inr" }, 68.284));
		units.add(new CurrencySimpleSubUnit("South Korean won", "South Korean won",
				new String[] { "krw" }, 1409.68));
		units.add(new CurrencySimpleSubUnit("Mexican peso", "Mexican pesos",
				new String[] { "mxn" }, 16.5626));
		units.add(new CurrencySimpleSubUnit("Malaysian ringgit", "Malaysian ringgit",
				new String[] { "myr" }, 3.9313));
		units.add(new CurrencySimpleSubUnit("New Zealand dollar", "New Zealand dollars",
				new String[] { "nzd" }, 1.5448));
		units.add(new CurrencySimpleSubUnit("Philippine peso", "Philippine pesos",
				new String[] { "php" }, 51.822));
		units.add(new CurrencySimpleSubUnit("Singapore dollar", "Singapore dollars",
				new String[] { "sgd" }, 1.5695));
		units.add(new CurrencySimpleSubUnit("Thai baht", "Thai baht", new String[] { "thb" },
				39.192));
		units.add(new CurrencySimpleSubUnit("South African rand", "South African rand",
				new String[] { "zar" }, 10.0836));
		units.add(new CurrencySimpleSubUnit("Euro", "Euros", new String[] { "eur", "€" }, 1.0));
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	private void processXML(Document doc) throws DOMException, ParseException,
			CloneNotSupportedException
	{
		doc.getDocumentElement().normalize();

		NodeList elements = doc.getElementsByTagName("Cube");

		for (int i = 0; i < elements.getLength(); i++)
		{
			Node node = elements.item(i);

			NamedNodeMap map = node.getAttributes();
			int length = map.getLength();

			if (length == 1)
			{
				lastUpdated = new SimpleDateFormat("yyyy-MM-dd").parse(map.getNamedItem("time")
						.getNodeValue());
			}
			else if (length == 2)
			{
				double rate = Double.parseDouble(map.getNamedItem("rate").getNodeValue());
				String currency = map.getNamedItem("currency").getNodeValue();
				
				updateCurrencyUnit(currency, rate);
			}
		}
	}

	@Override
	public String toString()
	{
		return "currency";
	}

	public void update()
	{
		try
		{
			Document doc = download(url);
			processXML(doc);
		}
		catch (ParserConfigurationException | SAXException | IOException | ParseException
				| DOMException | CloneNotSupportedException e)
		{
			throw new UnsupportedOperationException("Unable to update currencies", e);
		}
	}

	private void updateCurrencyUnit(String currency, double rate)
	{
		for (SubUnit unit : units)
		{
			if(unit.isMatch(currency))
			{
				((CurrencySimpleSubUnit) unit).setConversion(rate);
			}
		}
	}
}
