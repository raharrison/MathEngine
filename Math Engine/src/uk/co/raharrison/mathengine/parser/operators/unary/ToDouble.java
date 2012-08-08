package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.operators.Determinable;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class ToDouble extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeDouble)
			return arg1;

		return arg1.applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return new NodeDouble(number.doubleValue());
			}
		});
	}
}
