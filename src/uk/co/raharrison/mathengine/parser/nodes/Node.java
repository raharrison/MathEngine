package uk.co.raharrison.mathengine.parser.nodes;

public abstract class Node
{
	public abstract NodeNumber toNodeNumber();

	@Override
	public abstract String toString();
}
