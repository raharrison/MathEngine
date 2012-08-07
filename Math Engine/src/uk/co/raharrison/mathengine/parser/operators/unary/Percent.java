package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodePercent;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class Percent extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 1;
	}
	
	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if(arg1 instanceof NodeNumber)
		{
			return new NodePercent(arg1.doubleValue());
		}
		else
		{
			throw new RuntimeException("Invalid percentage value");
		}
	}
}
