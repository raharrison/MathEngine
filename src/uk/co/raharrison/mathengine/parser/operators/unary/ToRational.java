package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodeRational;
import uk.co.raharrison.mathengine.parser.operators.Determinable;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class ToRational extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "torational", "rat", "rational", "frac" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeRational)
			return arg1;

		return arg1.applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return new NodeRational(number.doubleValue());
			}
		});
	}

	@Override
	public String toString()
	{
		return "rational";
	}
}
