package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class Node
{
	public abstract NodeNumber toNodeNumber();

	@Override
	public abstract String toString();
}
