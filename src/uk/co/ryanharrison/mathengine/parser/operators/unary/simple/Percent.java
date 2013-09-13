package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodePercent;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

public class Percent extends NumberOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "percent", "%" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeConstant getResult(NodeConstant number)
	{
		return new NodePercent(number.getTransformer().toNodeNumber().doubleValue());
	}

	@Override
	public String toLongString()
	{
		return "percent";
	}

	@Override
	public String toString()
	{
		return "%";
	}

}