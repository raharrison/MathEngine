package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.MathUtils;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;

public class Factorial extends SimpleUnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeNumber getResult(NodeNumber number)
	{
		return NodeFactory.createNodeNumberFrom(MathUtils.factorial(number.doubleValue()));
	}
	
	@Override
	public String toString()
	{
		return "!";
	}
}
