package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;

public abstract class NodeNumber extends NodeConstant implements Cloneable
{
	@Override
	public NodeConstant add(NodeConstant arg2)
	{
		// TODO Auto-generated method stub
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d + ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();
			Matrix m2 = new Matrix(d);

			m = m2.add(m);

			return new NodeMatrix(m);
		}
		else if (arg2 instanceof NodeVector)
		{
			Vector v = ((NodeVector) arg2).asDoubleVector();
			Vector v2 = new Vector(d);

			v = v2.add(v);

			return new NodeVector(v);
		}
		else
		{
			return new NodeDouble(d + ((NodeBoolean) arg2).doubleValue());
		}
	}

	@Override
	protected abstract Object clone();

	@Override
	public NodeConstant divide(NodeConstant arg2)
	{
		// TODO Auto-generated method stub
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d / ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();
			Matrix m2 = new Matrix(d);

			m = m2.divide(m);

			return new NodeMatrix(m);
		}
		else if (arg2 instanceof NodeVector)
		{
			Vector v = ((NodeVector) arg2).asDoubleVector();
			Vector v2 = new Vector(d);

			v = v2.divide(v);

			return new NodeVector(v);
		}
		else
		{
			return new NodeDouble(d / ((NodeBoolean) arg2).doubleValue());
		}
	}

	@Override
	public NodeConstant multiply(NodeConstant arg2)
	{
		// TODO Auto-generated method stub
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d * ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();
			Matrix m2 = new Matrix(d);

			m = m2.multiply(m);

			return new NodeMatrix(m);
		}
		else if (arg2 instanceof NodeVector)
		{
			Vector v = ((NodeVector) arg2).asDoubleVector();
			Vector v2 = new Vector(d);

			v = v2.multiply(v);

			return new NodeVector(v);
		}
		else
		{
			return new NodeDouble(d * ((NodeBoolean) arg2).doubleValue());
		}
	}

	@Override
	public NodeConstant pow(NodeConstant arg2)
	{
		// TODO Auto-generated method stub
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(Math.pow(d, ((NodeDouble) arg2).doubleValue()));
		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();
			Matrix m2 = new Matrix(d);

			m = m2.pow(m);

			return new NodeMatrix(m);
		}
		else if (arg2 instanceof NodeVector)
		{
			Vector v = ((NodeVector) arg2).asDoubleVector();
			Vector v2 = new Vector(d);

			v = v2.pow(v);

			return new NodeVector(v);
		}
		else
		{
			return new NodeDouble(Math.pow(d, ((NodeBoolean) arg2).doubleValue()));
		}
	}

	@Override
	public NodeConstant subtract(NodeConstant arg2)
	{
		// TODO Auto-generated method stub
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d - ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();
			Matrix m2 = new Matrix(d);

			m = m2.subtract(m);

			return new NodeMatrix(m);
		}
		else if (arg2 instanceof NodeVector)
		{
			Vector v = ((NodeVector) arg2).asDoubleVector();
			Vector v2 = new Vector(d);

			v = v2.subtract(v);

			return new NodeVector(v);
		}
		else
		{
			return new NodeDouble(d - ((NodeBoolean) arg2).doubleValue());
		}
	}
}
