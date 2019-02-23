package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

import java.util.function.BiFunction;

public class Multiply extends SimpleBinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "*", "times", "multiply", "mult" };
	}

	@Override
	public int getPrecedence()
	{
		return 4;
	}

	@Override
	public String toLongString()
	{
		return "multiply";
	}

	@Override
	protected BiFunction<NodeNumber, NodeNumber, NodeConstant> getBiFunc() {
		return NodeNumber::multiply;
	}

	@Override
	public String toString()
	{
		return "*";
	}
}
