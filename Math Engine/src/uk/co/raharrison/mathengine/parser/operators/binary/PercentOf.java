package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Determinable;

public class PercentOf extends BinaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		// TODO : Implement multiply percent values in vector or matrix
		if (!(arg1 instanceof NodeNumber))
			throw new IllegalArgumentException("Illegal argument to operator percent of");

		final double percent = arg1.doubleValue();
		
		NodeConstant result = arg2.applyDeterminable(new Determinable()
		{	
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return NodeFactory.createNodeNumberFrom(number.multiply(new NodeDouble(percent / 100.0)).doubleValue());
			}
		});
		
		return result;
	}
	
	@Override
	public String toString()
	{
		return "% of";
	}
}
