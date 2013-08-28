package uk.co.ryanharrison.mathengine.unitconversion.units.currency;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uk.co.ryanharrison.mathengine.MathUtils;
import uk.co.ryanharrison.mathengine.unitconversion.units.Conversion;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleSubUnit;
import uk.co.ryanharrison.mathengine.unitconversion.units.SimpleUnitGroup;
import uk.co.ryanharrison.mathengine.unitconversion.units.SubUnit;

public class Currency extends SimpleUnitGroup
{
	private Date lastUpdated;
	
	private static final String primaryUrl = "http://openexchangerates.org/api/latest.json";
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

	@Override
	protected double doConversion(Conversion params)
	{
		return MathUtils.round(super.doConversion(params), 2);
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
