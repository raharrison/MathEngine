package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public final class Sum extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "sum", "sumnation" };
	}

	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		return arg1.toNodeNumber();
	}

	@Override
	public String toString()
	{
		return "sum";
	}
}