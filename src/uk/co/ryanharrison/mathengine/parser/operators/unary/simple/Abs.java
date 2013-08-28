package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

public class Abs extends SimpleUnaryOperator
{

	@Override
	public NodeNumber getResult(NodeConstant number)
	{
		return NodeFactory.createNodeNumberFrom(Math.abs(number.doubleValue()));
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "abs", "absolute" };
	}

	@Override
	public String toLongString()
	{
		return "aboslute";
	}

	@Override
	public String toString()
	{
		return "abs";
	}

}
