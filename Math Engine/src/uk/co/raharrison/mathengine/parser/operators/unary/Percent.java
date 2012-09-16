package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodePercent;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class Percent extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "percent", "%" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public String toLongString()
	{
		return "percent";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeNumber)
		{
			return new NodePercent(arg1.doubleValue());
		}
		else
		{
			throw new RuntimeException("Invalid percentage value");
		}
	}

	@Override
	public String toString()
	{
		return "%";
	}
}
