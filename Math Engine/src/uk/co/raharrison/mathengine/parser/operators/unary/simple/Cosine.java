package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.operators.TrigOperator;

public final class Cosine extends TrigOperator
{
	@Override
	public NodeNumber getResult(NodeNumber num, AngleUnit unit)
	{
		double result = Math.cos(super.radiansTo(num.doubleValue(), unit));
		
		return NodeFactory.createNodeNumberFrom(result);
	}
	
	@Override
	public String toString()
	{
		return "cos";
	}
}
