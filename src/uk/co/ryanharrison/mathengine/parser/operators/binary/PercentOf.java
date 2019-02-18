package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class PercentOf extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "%of", "percentof" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return "percent of";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		// TODO: implement
		return null;
		//return arg1.multiply(arg2.divide(NodeFactory.createNodeNumberFrom(100.0)));

		// final double percent = arg1.doubleValue();
		//
		// NodeConstant result = arg2.applyDeterminable(new Determinable()
		// {
		// @Override
		// public NodeNumber getResult(NodeNumber number)
		// {
		// return NodeFactory.createNodeNumberFrom(number.multiply(
		// new NodeDouble(percent / 100.0)).doubleValue());
		// }
		// });
		//
		// return result;
	}

	@Override
	public String toString()
	{
		return "% of";
	}
}
