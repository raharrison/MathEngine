package uk.co.raharrison.mathengine.unitconversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import uk.co.raharrison.mathengine.unitconversion.units.*;
import uk.co.raharrison.mathengine.unitconversion.units.currency.Currency;
import uk.co.raharrison.mathengine.unitconversion.units.timezones.TimeZones;

public class UnitHandler extends DefaultHandler
{
	private enum GroupType
	{
		SIMPLE, COMPLEX
	};
	
	private Map<String, UnitGroup> customGroups;

	private UnitGroup group;
	private SubUnit sub;
	private String temp;
	private List<UnitGroup> groups;

	private boolean isUnit;
	private boolean isAlias;
	private boolean isConversions;
	private String toUnit;
	private String variable;

	private GroupType groupType;
	
	public UnitHandler()
	{
		groups = new ArrayList<>();
		isUnit = false;
		isAlias = false;
		isConversions = false;
		groupType = GroupType.SIMPLE;
		
		customGroups = new HashMap<>();
		customGroups.put("currency", new Currency());
		customGroups.put("time zones", new TimeZones());
	}

	@Override
	public void characters(char[] buffer, int start, int length) throws SAXException
	{
		temp = new String(buffer, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equalsIgnoreCase("unit"))
		{
			group.addSubUnit(sub);

			if (groupType == GroupType.SIMPLE)
				sub = new SimpleSubUnit();
			else
				sub = new ComplexSubUnit();
		}
		else if (qName.equalsIgnoreCase("group"))
		{
			groups.add(group);
			isUnit = false;
			isAlias = false;
			isConversions = false;
		}
		else if (qName.equalsIgnoreCase("name"))
		{
			if(customGroups.containsKey(temp))
				group = customGroups.get(temp);
			
			group.setName(temp);
		}
		else if (isUnit)
		{
			if (qName.equalsIgnoreCase("singular"))
				sub.setSingular(temp);
			else if (qName.equalsIgnoreCase("plural"))
				sub.setPlural(temp);
			
			else if (isAlias)
			{
				if (qName.equalsIgnoreCase("alias"))
					sub.addAlias(temp);
				
				isAlias = false;
			}
			else if (this.groupType == GroupType.SIMPLE)
			{
				if (qName.equalsIgnoreCase("conversion"))
				{
					SimpleSubUnit unit = (SimpleSubUnit) sub;
					unit.setConversion(Double.parseDouble(temp));
				}
			}
			else
			{
				if (isConversions)
				{
					ComplexSubUnit unit = (ComplexSubUnit) sub;
					unit.addConversion(this.toUnit, temp);
					unit.setVariable(variable);
					isConversions = false;
				}
			}
		}

	}

	public List<UnitGroup> getGroups()
	{
		return groups;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		temp = "";
		if (qName.equalsIgnoreCase("group"))
		{
			String type = attributes.getValue("type");
			if (type.equalsIgnoreCase("complex"))
			{
				this.groupType = GroupType.COMPLEX;
				group = new ComplexUnitGroup();
			}
			else
			{
				this.groupType = GroupType.SIMPLE;
				group = new SimpleUnitGroup();
			}
		}
		else if (qName.equalsIgnoreCase("unit"))
		{
			if (groupType == GroupType.SIMPLE)
				sub = new SimpleSubUnit();
			else
				sub = new ComplexSubUnit();

			isUnit = true;
		}
		else if (qName.equalsIgnoreCase("aliases") || qName.equalsIgnoreCase("alias"))
		{
			isAlias = true;
		}
		else if (qName.equalsIgnoreCase("conversions"))
		{
			isConversions = true;
		}
		else if (qName.equalsIgnoreCase("conversion"))
		{
			isConversions = true;
			toUnit = attributes.getValue("to");
			variable = attributes.getValue("var");
		}
	}
}
