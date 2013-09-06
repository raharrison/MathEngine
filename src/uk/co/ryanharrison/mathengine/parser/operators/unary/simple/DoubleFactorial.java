package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.MathUtils;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

public class DoubleFactorial extends NumberOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "!!", "doublefac", "dfac", "doublefactorial" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeNumber getResult(NodeConstant number)
	{
		return NodeFactory.createNodeNumberFrom(MathUtils.doubleFactorial(number.doubleValue()));
	}

	@Override
	public String toLongString()
	{
		return "double factorial";
	}

	@Override
	public String toString()
	{
		return "!!";
	}
}
