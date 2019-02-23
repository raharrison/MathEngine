package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.Determinable;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public abstract class NumberOperator extends UnaryOperator implements Determinable
{
	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		NodeConstant result = arg1.applyDeterminable(this);

		return result;
	}
}
