package uk.co.raharrison.mathengine.parser.operators;

public abstract class Operator
{
	// TODO : Implement correct precedence structure for all operators
	public abstract int getPrecedence();
	
	@Override
	public abstract String toString();
}
