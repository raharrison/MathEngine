package uk.co.ryanharrison.mathengine.parser.operators.binary.logical;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class NotEquals extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "!=", "<>", "notequal", "notequals" };
	}

	@Override
	public int getPrecedence()
	{
		return 8;
	}

	@Override
	public String toLongString()
	{
		return "not equals";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		boolean c = arg1.equals(arg2);

		return new NodeBoolean(!c);
	}

	@Override
	public String toString()
	{
		return "!=";
	}
}
