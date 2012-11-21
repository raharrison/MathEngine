package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.operators.unary.TrigOperator;

public class Sine extends TrigOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "sin", "sine" };
	}

	@Override
	public NodeNumber getResult(NodeNumber num, AngleUnit unit)
	{
		double result = Math.sin(super.radiansTo(num.doubleValue(), unit));

		return NodeFactory.createNodeNumberFrom(result);
	}

	@Override
	public String toLongString()
	{
		return "sine";
	}

	@Override
	public String toString()
	{
		return "sin";
	}
}
