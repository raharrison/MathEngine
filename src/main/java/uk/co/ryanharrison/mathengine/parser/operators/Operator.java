package uk.co.ryanharrison.mathengine.parser.operators;

public abstract class Operator
{
	public abstract String[] getAliases();

	// TODO : Implement correct precedence structure for all operators
	public abstract int getPrecedence();

	public abstract String toLongString();

	@Override
	public abstract String toString();
}
