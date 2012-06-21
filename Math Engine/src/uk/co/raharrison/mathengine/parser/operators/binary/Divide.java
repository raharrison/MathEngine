package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Divide extends BinaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 4;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		return arg1.divide(arg2);
	}

}
