package uk.co.raharrison.mathengine.unitconversion.units.simple;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uk.co.raharrison.mathengine.unitconversion.units.Conversion;
import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.UnitGroup;

public abstract class SimpleUnitGroup extends UnitGroup
{
	@Override
	protected double doConversion(Conversion params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (SimpleSubUnit) params.getFrom()) != null
				&& (to = (SimpleSubUnit) params.getTo()) != null)
		{
			return amount * from.getConversion() / to.getConversion();
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}
	
	@Override
	public Element toXML(Document doc)
	{
		Element group = doc.createElement("group");
		group.setAttribute("type", "simple");
		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode(toString()));
		group.appendChild(name);
		
		for (SubUnit sub : this.units)
		{
			Element subUnit = doc.createElement("unit");
			
			Element singular = doc.createElement("singular");
			singular.appendChild(doc.createTextNode(sub.getBaseAliasSingular()));
			Element plural = doc.createElement("plural");
			plural.appendChild(doc.createTextNode(sub.getBaseAliasPlural()));
			subUnit.appendChild(singular);
			subUnit.appendChild(plural);
			
			Element aliases = doc.createElement("aliases");
			
			for (String str : sub.getAliases())
			{
				Element alias = doc.createElement("alias");
				alias.appendChild(doc.createTextNode(str));
				aliases.appendChild(alias);
			}
			
			subUnit.appendChild(aliases);
			
			if(sub instanceof SimpleSubUnit)
			{
				Element conversion = doc.createElement("conversion");
				conversion.appendChild(doc.createTextNode(Double.toString(((SimpleSubUnit) sub).conversion)));
				subUnit.appendChild(conversion);
				group.appendChild(subUnit);
			}
		}
		
		return group;
	}
}
