package uk.co.raharrison.mathengine.parser.operators.binary.logical;

import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Xor extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "xor" };
	}

	@Override
	public int getPrecedence()
	{
		return 9;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if (arg1 instanceof NodeNumber)
		{
			if (arg2 instanceof NodeNumber)
			{
				boolean result = arg1.doubleValue() == 1 ^ arg2.doubleValue() == 1;

				return new NodeBoolean(result);
			}
		}

		throw new IllegalArgumentException("Must have two logical arguments to operator 'xor'");
	}

	@Override
	public String toString()
	{
		return "xor";
	}
}
