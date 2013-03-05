package uk.co.raharrison.mathengine.parser.operators.binary.logical;

import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class LessThan extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "<", "lessthan" };
	}

	@Override
	public int getPrecedence()
	{
		return 7;
	}

	@Override
	public String toLongString()
	{
		return "less than";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		int c = arg1.compareTo(arg2);

		return new NodeBoolean(c < 0);
	}

	@Override
	public String toString()
	{
		return "<";
	}
}
