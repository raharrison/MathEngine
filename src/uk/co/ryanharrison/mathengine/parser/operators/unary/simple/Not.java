package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;

public class Not extends SimpleUnaryOperator
{
	@Override
	public NodeConstant getResult(NodeConstant number)
	{
		boolean result = number.doubleValue() == 1.0;
		return new NodeBoolean(!result);
	}

	@Override
	public String[] getAliases()
	{
		return new String[] {"not", "invert"};
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "not";
	}
}
