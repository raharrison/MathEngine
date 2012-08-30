package uk.co.raharrison.mathengine.parser.operators.unary;

import java.util.Arrays;

import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class Sort extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (!(arg1 instanceof NodeVector))
		{
			return arg1;
		}

		Node[] elements = ((NodeVector) arg1).getValues();

		Arrays.sort(elements);
		return new NodeVector(elements);
	}
	
	@Override
	public String toString()
	{
		return "sort";
	}
}
