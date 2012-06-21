package uk.co.raharrison.mathengine.unitconversion.units;

import java.util.Arrays;

public abstract class SubUnit
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

	public boolean isMatch(String s)
	{
		if (singular.equals(s) || plural.equals(s))
			return true;

		if (Arrays.asList(aliases).contains(s))
			return true;

		return false;
	}

	@Override
	public String toString()
	{
		return plural;
	}
}
