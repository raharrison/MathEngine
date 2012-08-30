package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;

public class Ln extends SimpleUnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public NodeNumber getResult(NodeNumber number)
	{
		return NodeFactory.createNodeNumberFrom(Math.log(number.doubleValue()));
	}
	
	@Override
	public String toString()
	{
		return "ln";
	}
}
