package uk.co.raharrison.mathengine.parser.operators.binary.logical;

import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Xor extends BinaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 9;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if (arg1 instanceof NodeBoolean || arg1 instanceof NodeDouble)
		{
			if (arg2 instanceof NodeBoolean || arg2 instanceof NodeDouble)
			{
				boolean result = arg1.asDouble() == 1 ^ arg2.asDouble() == 1;

				return new NodeBoolean(result);
			}
		}

		throw new IllegalArgumentException("Must have two logical arguments to operator 'xor'");
	}
}