package uk.co.raharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements NodeMath, Comparable<NodeConstant>
{
	public abstract double doubleValue();

	@Override
	public abstract boolean equals(Object object);
	
	@Override
	public abstract String toString();
}
