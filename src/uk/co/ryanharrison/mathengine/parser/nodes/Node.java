package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class Node
{
	protected NodeTransformer transformer;
	
	public abstract NodeTransformer getTransformer();
	
	@Override
	public abstract String toString();
}
