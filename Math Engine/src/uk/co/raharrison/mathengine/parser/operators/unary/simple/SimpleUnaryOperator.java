package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public abstract class SimpleUnaryOperator extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 2;
	}

	public abstract double getResult(double x);

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeVector)
		{
			NodeDouble[] vals = ((NodeVector) arg1).asDoubles();

			for (int i = 0; i < vals.length; i++)
			{
				vals[i].setValue(getResult(vals[i].doubleValue()));
			}

			return new NodeVector(vals);
		}
		else if (arg1 instanceof NodeMatrix)
		{
			NodeDouble[][] vals = ((NodeMatrix) arg1).asDoubles();

			for (int i = 0; i < vals.length; i++)
			{
				for (int j = 0; j < vals[0].length; j++)
				{
					vals[i][j].setValue(getResult(vals[i][j].doubleValue()));
				}
			}

			return new NodeMatrix(vals);
		}
		else
		{
			return new NodeDouble(getResult(arg1.doubleValue()));
		}
	}
}
