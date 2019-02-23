package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class Node
{
	NodeTransformer transformer;
	
	public abstract NodeTransformer getTransformer();

	public abstract Node copy();
	
	@Override
	public abstract String toString();
}
