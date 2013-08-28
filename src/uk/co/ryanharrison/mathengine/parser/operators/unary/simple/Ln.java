package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

public class Ln extends SimpleUnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "ln", "nlog", "naturallog", "natlog" };
	}

	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public NodeNumber getResult(NodeConstant number)
	{
		return NodeFactory.createNodeNumberFrom(Math.log(number.doubleValue()));
	}

	@Override
	public String toLongString()
	{
		return "natural logarithm";
	}

	@Override
	public String toString()
	{
		return "ln";
	}
}
