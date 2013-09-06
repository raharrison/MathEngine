package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;

public class If extends VectorOperator
{
	@Override
	protected NodeConstant calculateResultFromVector(NodeVector arg1)
	{
		if (arg1.getSize() != 3)
			throw new IllegalArgumentException(
					"Expected three arguments to operator if");

		if (arg1.getValues()[0].getTransformer().toNodeNumber().doubleValue() == 1.0)
			return (NodeConstant) arg1.getValues()[1];
		else
			return (NodeConstant) arg1.getValues()[2];
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "if" };
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
		return "if";
	}
}
