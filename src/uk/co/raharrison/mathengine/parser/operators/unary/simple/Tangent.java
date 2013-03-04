package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.operators.unary.TrigOperator;

public class Tangent extends TrigOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "tan", "tangent" };
	}

	@Override
	public NodeConstant getResult(NodeConstant num, AngleUnit unit)
	{
		double result = Math.tan(super.radiansTo(num.doubleValue(), unit));

		return NodeFactory.createNodeNumberFrom(result);
	}

	@Override
	public String toLongString()
	{
		return "tangent";
	}

	@Override
	public String toString()
	{
		return "tan";
	}
}
