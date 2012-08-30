package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Subtract extends BinaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 6;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		return arg1.subtract(arg2);
	}

	@Override
	public String toString()
	{
		return "-";
	}
}
