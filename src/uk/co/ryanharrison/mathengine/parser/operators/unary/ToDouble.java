package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.Determinable;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public class ToDouble extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "d", "todouble", "double", "num" };
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
		if (arg1 instanceof NodeDouble)
			return arg1;

		return arg1.applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return new NodeDouble(number.getTransformer().toNodeNumber().doubleValue());
			}
		});
	}

	@Override
	public String toString()
	{
		return "double";
	}
}
