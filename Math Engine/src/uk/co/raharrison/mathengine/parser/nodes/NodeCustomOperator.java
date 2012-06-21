package uk.co.raharrison.mathengine.parser.nodes;

public class NodeCustomOperator extends Node
{
	private String variable;

	public NodeCustomOperator(String variable)
	{
		this.variable = variable;
	}

	public String getCustomOperator()
	{
		return variable;
	}

	@Override
	public int hashCode()
	{
		return variable.hashCode();
	}
}
