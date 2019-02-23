package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import org.xml.sax.SAXException;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleSubUnit;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnitGroup;
import uk.co.ryanharrison.mathengine.unitconversion.units.SubUnit;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Currency extends SimpleUnitGroup
{
	private Date lastUpdated;
	
	private static final String primaryUrl = "http://openexchangerates.org/api/latest.json?app_id=05a4f48b8a8b4ac394d2ea8db7f1a0c6";
	private static final String secondaryUrl = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";
	
	private final CurrencyProvider primaryProvider;
	private final CurrencyProvider secondaryProvider;

	public Currency()
	{
		this(false);
	}

	public Currency(boolean downloadCurrencies)
	{
		super();
		
		GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("bst"), Locale.ENGLISH);
		//Thurs Aug 02 19:02:11 BST 2012
		calendar.set(2012, Calendar.AUGUST, 2, 18, 2, 11);
		
		lastUpdated = calendar.getTime();
		
		primaryProvider = new JsonCurrencyProvider(primaryUrl);
		secondaryProvider = new XmlCurrencyProvider(secondaryUrl);

		if (downloadCurrencies)
		{
			update();
		}
	}
	
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	
	@Override
	public void update()
	{
		try
		{
			updateCurrencies(primaryProvider);
		}
		catch (IOException | SAXException | ParserConfigurationException | ParseException e)
		{
			try
			{	
				updateCurrencies(secondaryProvider);
			}
			catch (ParserConfigurationException | SAXException | IOException | ParseException exc)
			{
				throw new RuntimeException("Unable to update currencies", exc);
			}
		}
	}
	
	private void updateCurrencies(CurrencyProvider provider) throws SAXException, IOException, ParseException, ParserConfigurationException
	{
		List<Unit> currencies = null;
		
		provider.download();
		currencies = provider.process();
		
		if(currencies.size() == 0)
			throw new IOException("Unable to download currencies");
		
		this.lastUpdated = provider.getLastUpdated();
		
		for (Unit unit : currencies)
		{
			updateCurrencyUnit(unit.getUnit(), unit.getRate());
		}
	}
	
	private void updateCurrencyUnit(String currency, double rate)
	{
		for (SubUnit unit : units)
		{
			if(unit.isMatch(currency))
			{
				((SimpleSubUnit) unit).setConversion(rate);
			}
		}
	}
}
