package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class Divide extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "/", "div", "divide", "over" };
	}

	@Override
	public int getPrecedence()
	{
		return 4;
	}

	@Override
	public String toLongString()
	{
		return "divide";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		return arg1.divide(arg2);
	}

	@Override
	public String toString()
	{
		return "/";
	}
}
