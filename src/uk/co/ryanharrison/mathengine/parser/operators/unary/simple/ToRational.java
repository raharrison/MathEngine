package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeRational;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

public class ToRational extends NumberOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "torational", "rat", "rational", "frac" };
	}

	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public NodeConstant getResult(NodeConstant number)
	{
		return new NodeRational(number.getTransformer().toNodeNumber().doubleValue());
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "rational";
	}
}
