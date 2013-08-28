package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.Determinable;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public class Log extends UnaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "log", "logarithm" };
	}

	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public String toLongString()
	{
		return "logarithm";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeNumber)
		{
			return new NodeDouble(Math.log10(arg1.doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return arg1.applyDeterminable(new Determinable()
			{
				@Override
				public NodeNumber getResult(NodeConstant number)
				{
					return new NodeDouble(Math.log10(number.doubleValue()));
				}
			});
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).toDoubleMatrix();

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
