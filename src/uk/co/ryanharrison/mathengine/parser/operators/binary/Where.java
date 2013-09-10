package uk.co.ryanharrison.mathengine.parser.operators.binary;

import java.util.ArrayList;
import java.util.List;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class Where extends BinaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if(!(arg1 instanceof NodeVector))
			throw new IllegalArgumentException("First argument must be a vector");
		
		if(!(arg2 instanceof NodeFunction))
			throw new IllegalArgumentException("Second argument must be a predicate function with one argument");
		
		NodeVector vector = (NodeVector) arg1;
		NodeFunction predicate = (NodeFunction) arg2;
		
		if(predicate.getVariables().length != 1)
			throw new IllegalArgumentException("Predicate function must have one argument");
		
		List<NodeConstant> results = new ArrayList<NodeConstant>();
		Node[] vecVals = vector.getValues();
		
		for (int i = 0; i < vecVals.length; i++)
		{
			NodeConstant constant = (NodeConstant) vecVals[i];
			if(predicate.evaluate(constant).getTransformer().toNodeNumber().doubleValue() == 1.0)
			{
				results.add((NodeConstant) vecVals[i]);
			}
		}
		
		return new NodeVector(results.toArray(new Node[results.size()]));
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "where" };
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
		return "where";
	}
}
