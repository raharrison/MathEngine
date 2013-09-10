package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.MathUtils;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

public class Factorial extends NumberOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "!", "factorial", "fac" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeNumber getResult(NodeConstant number)
	{
		return NodeFactory.createNodeNumberFrom(MathUtils.factorial(number.getTransformer().toNodeNumber().doubleValue()));
	}

	@Override
	public String toLongString()
	{
		return "factorial";
	}

	@Override
	public String toString()
	{
		return "!";
	}
}
