package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

public class Abs extends NumberOperator
{

	@Override
	public NodeNumber getResult(NodeConstant number)
	{
		return NodeFactory.createNodeNumberFrom(Math.abs(number.getTransformer().toNodeNumber().doubleValue()));
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
