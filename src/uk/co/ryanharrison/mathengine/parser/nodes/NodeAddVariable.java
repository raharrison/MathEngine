package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Utils;

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
	public String toString()
	{
		return String.format("%s = %s", variable, node.toString());
	}

	@Override
	public NodeTransformer getTransformer()
	{
		if (this.transformer == null)
			this.transformer = new NodeAddVariableTransformer();

		return this.transformer;
	}

	@Override
	public Node copy() {
		return new NodeAddVariable(variable, node.copy());
	}

	private class NodeAddVariableTransformer implements NodeTransformer
	{

		@Override
		public NodeVector toNodeVector()
		{
			return new NodeVector(new Node[] { toNodeNumber() });
		}

		@Override
		public NodeNumber toNodeNumber()
		{
			return node
					.getTransformer()
					.toNodeNumber()
					.add(NodeFactory.createNodeNumberFrom(Utils
							.stringToNum(variable))).getTransformer()
					.toNodeNumber();
		}
	}
}
