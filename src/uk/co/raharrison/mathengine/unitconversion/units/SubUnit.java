package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SubUnit implements Matchable<String>
{
	private List<String> aliases;
	private String singular;
	private String plural;

	public SubUnit()
	{
		this.aliases = new ArrayList<>();
	}

	public void addAlias(String alias)
	{
		aliases.add(alias);
	}

	public List<String> getAliases()
	{
		return this.aliases;
	}

	public String getPlural()
	{
		return this.plural;
	}

	public String getSingular()
	{
		return this.singular;
	}

	public Set<String> getUniqueAliases()
	{
		Set<String> uniqueAliases = new HashSet<>();

		uniqueAliases.add(singular);
		if (!singular.equalsIgnoreCase(plural))
			uniqueAliases.add(plural);

		for (String alias : this.aliases)
		{
			uniqueAliases.add(alias);
		}

		return uniqueAliases;
	}

	@Override
	public boolean isMatch(String s)
	{
		if (singular.equalsIgnoreCase(s) || plural.equalsIgnoreCase(s))
			return true;

		if (aliases.contains(s.toLowerCase()))
			return true;

		return false;
	}

	public void setPlural(String plural)
	{
		this.plural = plural;
	}

	public void setSingular(String singular)
	{
		this.singular = singular;
	}

	@Override
	public String toString()
	{
		return plural;
	}
}
