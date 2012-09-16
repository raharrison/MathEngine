package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Multiply extends BinaryOperator
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
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if (arg1 instanceof NodeMatrix && arg2 instanceof NodeMatrix)
			return ((NodeMatrix) arg1).multiplyMatrix((NodeMatrix) arg2);

		return arg1.multiply(arg2);
	}

	@Override
	public String toString()
	{
		return "*";
	}
}
