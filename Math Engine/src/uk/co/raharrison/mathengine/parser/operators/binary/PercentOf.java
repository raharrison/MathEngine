package uk.co.raharrison.mathengine.parser.operators.binary;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;

public class PercentOf extends BinaryOperator
{
	// TODO: Implement getResult in an interface and pass to NodeConstants to
	// apply it to each Node value (.applyAction(Determinable.getResult()))

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	private NodeNumber getResult(double percent, double amount)
	{
		NodeNumber num = NodeFactory.createNodeNumberFrom(amount * (percent / 100.0));
		return num;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if (!(arg1 instanceof NodeNumber))
			throw new IllegalArgumentException("Illegal argument to operator percent of");

		double percent = arg1.doubleValue();

		if (arg2 instanceof NodeVector)
		{
			NodeDouble[] vals = ((NodeVector) arg2).asDoubles();
			NodeNumber[] newVals = new NodeNumber[vals.length];

			for (int i = 0; i < vals.length; i++)
			{
				newVals[i] = getResult(percent, vals[i].doubleValue());
			}

			return new NodeVector(newVals);
		}
		else if (arg2 instanceof NodeMatrix)
		{
			NodeDouble[][] vals = ((NodeMatrix) arg2).asDoubles();
			NodeNumber[][] newVals = new NodeNumber[vals.length][vals[0].length];

			for (int i = 0; i < vals.length; i++)
			{
				for (int j = 0; j < vals[0].length; j++)
				{
					newVals[i][j] = getResult(percent, vals[i][j].doubleValue());
				}
			}

			return new NodeMatrix(newVals);
		}
		else
		{
			return getResult(percent, arg2.doubleValue());
		}
	}
}
