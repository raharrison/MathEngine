package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.operators.unary.TrigOperator;

public final class Cosine extends TrigOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "cos", "cosine" };
	}

	@Override
	public NodeConstant getResult(NodeConstant num, AngleUnit unit)
	{
		double result = Math.cos(super.radiansTo(num.getTransformer().toNodeNumber().doubleValue(), unit));

		return NodeFactory.createNodeNumberFrom(result);
	}

	@Override
	public String toLongString()
	{
		return "cosine";
	}

	@Override
	public String toString()
	{
		return "cos";
	}
}
