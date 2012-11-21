package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubUnit implements Matchable<String>
{
	protected String[] aliases;
	protected String singular;
	protected String plural;

	public SubUnit(String singular, String plural, String[] aliases)
	{
		this.singular = singular;
		this.plural = plural;
		this.aliases = aliases;
	}

	public String getBaseAliasPlural()
	{
		return this.plural;
	}

	public String getBaseAliasSingular()
	{
		return this.singular;
	}
	
	public String[] getAliases()
	{
		return this.aliases;
	}

	public boolean isMatch(String s)
	{
		if (singular.equalsIgnoreCase(s) || plural.equalsIgnoreCase(s))
			return true;

		// TODO : Implement binary search to improve speed
		if (Arrays.asList(aliases).contains(s.toLowerCase()))
			return true;

		return false;
	}

	@Override
	public String toString()
	{
		return plural;
	}

	public List<String> getUniqueAliases()
	{
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add(singular);
		if(!singular.equalsIgnoreCase(plural))
			aliases.add(plural);
		
		for (String alias : this.aliases)
		{
			if(!aliases.contains(alias))
				aliases.add(alias);
		}
		return aliases;
	}
}
