package uk.co.raharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements NodeMath, Comparable<NodeConstant>
{
	public abstract double asDouble();

	@Override
	public abstract boolean equals(Object object);
}
