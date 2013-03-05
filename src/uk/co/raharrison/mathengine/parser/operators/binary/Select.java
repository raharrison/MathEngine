package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class Select extends BinaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if(!(arg1 instanceof NodeVector))
			throw new IllegalArgumentException("First argument must be a vector");
		
		if(!(arg2 instanceof NodeFunction))
			throw new IllegalArgumentException("Second argument must be a selector function with one argument");
		
		NodeVector vector = (NodeVector) arg1;
		NodeFunction selector = (NodeFunction) arg2;
		
		if(selector.getVariables().length != 1)
			throw new IllegalArgumentException("Selector function must have one argument");
		
		NodeConstant[] results = new NodeConstant[vector.getSize()];
		Node[] vecVals = vector.getValues();
		
		for (int i = 0; i < results.length; i++)
		{
			results[i] = selector.evaluate((NodeConstant) vecVals[i]);
		}
		
		return new NodeVector(results);
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "select" };
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
		return "select";
	}

}
