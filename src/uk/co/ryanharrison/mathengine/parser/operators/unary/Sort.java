package uk.co.ryanharrison.mathengine.parser.operators.unary;

import java.util.Arrays;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public class Sort extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "sort", "arrange" };
	}

	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (!(arg1 instanceof NodeVector))
		{
			return arg1;
		}

		Node[] elements = ((NodeVector) arg1).getValues();

		Node[] results = elements.clone();
		Arrays.sort(results);
		return new NodeVector(results);
	}

	@Override
	public String toString()
	{
		return "sort";
	}
}
