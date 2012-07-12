package uk.co.raharrison.mathengine.parser.operators.unary;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public final class Sum extends UnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if (arg1 instanceof NodeDouble || arg1 instanceof NodeBoolean)
		{
			return new NodeDouble(arg1.doubleValue());
		}
		else
		{
			// TODO: Allow vectors inside vectors

			if (arg1 instanceof NodeVector)
			{
				return new NodeDouble(((NodeVector) arg1).asDoubleVector().sum());
			}
			else if (arg1 instanceof NodeMatrix)
			{
				Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();

				return new NodeDouble(m.sum());
			}

			throw new UnsupportedOperationException("Not implemented");
		}
	}
}