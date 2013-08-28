package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Utils;

public class NodeVariable extends Node
{
	private String variable;

	public NodeVariable(String variable)
	{
		this.variable = variable;
	}

	public String getVariable()
	{
		return variable;
	}

	@Override
	public int hashCode()
	{
		return variable.hashCode();
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		return NodeFactory.createNodeNumberFrom(Utils.stringToNum(variable));
	}

	@Override
	public String toString()
	{
		return variable;
	}
}
