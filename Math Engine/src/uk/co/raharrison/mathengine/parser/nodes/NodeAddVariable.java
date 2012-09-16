package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.Utils;

public final class NodeAddVariable extends Node
{
	private String variable;
	private Node node;

	public NodeAddVariable(String variable, Node node)
	{
		this.variable = variable;
		this.node = node;
	}

	public Node getNode()
	{
		return node;
	}

	public String getVariable()
	{
		return variable;
	}

	@Override
	public int hashCode()
	{
		return variable.hashCode() + node.hashCode();
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		return node.toNodeNumber()
				.add(NodeFactory.createNodeNumberFrom(Utils.stringToNum(variable))).toNodeNumber();
	}

	@Override
	public String toString()
	{
		return String.format("%s := %s", variable, node.toString());
	}
}
