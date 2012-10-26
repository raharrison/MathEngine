package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodePercent;
import uk.co.raharrison.mathengine.parser.operators.Determinable;
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
		return arg1.applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return new NodePercent(number.doubleValue());
			}
		});
	}

	@Override
	public String toString()
	{
		return "%";
	}
}
