package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.symbolic.Differentiator;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public class Diff extends UnaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		// FIXME : Putting the result of this into a function variable for
		// evaluation doesn't work
		if (!(arg1 instanceof NodeFunction))
			throw new IllegalArgumentException("Argument must be a function");

		NodeFunction func = (NodeFunction) arg1;

		if (func.getArgNum() != 1)
			throw new IllegalArgumentException("Function can only have one argument");

		if (!func.getVariables()[0].equals("x"))
			throw new IllegalArgumentException("Argument can only be 'x'");

		Differentiator differentiator = new Differentiator();
		Function derivative = differentiator.differentiate(func.toFunction(), true);

		return new NodeFunction(derivative);
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "diff", "differentiate", "derive" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "differentiate";
	}
}
