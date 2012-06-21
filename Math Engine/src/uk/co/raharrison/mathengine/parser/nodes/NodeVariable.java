package uk.co.raharrison.mathengine.parser.nodes;

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

	public void setVariable(String variable)
	{
		this.variable = variable;
	}

}
