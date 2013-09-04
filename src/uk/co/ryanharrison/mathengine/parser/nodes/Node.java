package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class Node
{
	public abstract NodeTransformer getNodeTransformer();
	
	@Override
	public abstract String toString();
}
