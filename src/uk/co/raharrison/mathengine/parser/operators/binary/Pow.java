package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Pow extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "^", "pow", "power", "powerof", "powof", "topowerof", "topowof" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return "power";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		return arg1.pow(arg2);
	}

	@Override
	public String toString()
	{
		return "^";
	}
}
