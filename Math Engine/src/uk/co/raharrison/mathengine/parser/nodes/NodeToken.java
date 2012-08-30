package uk.co.raharrison.mathengine.parser.nodes;

public class NodeToken extends Node
{
	private String variable;

	public NodeToken(String variable)
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
	public String toString()
	{
		return variable;
	}
}
