package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.Determinable;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public class Log extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeNumber || arg1 instanceof NodeBoolean)
		{
			return new NodeDouble(Math.log10(arg1.doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return arg1.applyDeterminable(new Determinable()
			{
				@Override
				public NodeNumber getResult(NodeNumber number)
				{
					return new NodeDouble(Math.log10(number.doubleValue()));
				}
			});
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();

			if (m.getColumnCount() == 2)
			{
				NodeDouble[] results = new NodeDouble[m.getRowCount()];

				for (int i = 0; i < results.length; i++)
				{
					results[i] = new NodeDouble(Math.log10(m.get(i, 1)) / Math.log10(m.get(i, 0)));
				}

				return new NodeVector(results);
			}
			else
			{
				throw new IllegalArgumentException("Matrix must have two columns");
			}
		}
		else
		{
			throw new UnsupportedOperationException("Unsupported arguments to operator 'log'");
		}
	}
	
	@Override
	public String toString()
	{
		return "log";
	}
}
