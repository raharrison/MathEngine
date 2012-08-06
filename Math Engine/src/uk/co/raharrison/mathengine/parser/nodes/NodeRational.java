package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.Rational;
import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;

public class NodeRational extends NodeNumber
{
	private Rational value;

	public NodeRational(double value)
	{
		this.value = new Rational(value);
	}

	public NodeRational(int numerator, int denominator)
	{
		this.value = new Rational(numerator, denominator);
	}

	public NodeRational(Rational rational)
	{
		this.value = rational;
	}

	@Override
	protected Object clone()
	{
		return value.clone();
	}

	@Override
	public int compareTo(NodeConstant o)
	{
		if (o instanceof NodeRational)
		{
			return value.compareTo(((NodeRational) o).getValue());
		}
		else
		{
			return new NodeDouble(doubleValue()).compareTo(o);
		}
	}

	@Override
	public double doubleValue()
	{
		return value.doubleValue();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeRational)
		{
			return value.equals(((NodeRational) object).getValue());
		}

		return false;
	}

	public Rational getValue()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	@Override
	public String toString()
	{
		return value.toString();
	}

	@Override
	public NodeConstant add(NodeConstant arg2)
	{
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d + ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeRational)
		{
			return new NodeRational(value.add(((NodeRational) arg2).value));
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
			return new NodeDouble(d + (arg2).doubleValue());
		}
	}

	@Override
	public NodeConstant divide(NodeConstant arg2)
	{
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d / ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeRational)
		{
			return new NodeRational(value.divide(((NodeRational) arg2).value));
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
			return new NodeDouble(d / (arg2).doubleValue());
		}
	}

	@Override
	public NodeConstant multiply(NodeConstant arg2)
	{
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d * ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeRational)
		{
			return new NodeRational(value.multiply(((NodeRational) arg2).value));
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
			return new NodeDouble(d * (arg2).doubleValue());
		}
	}

	@Override
	public NodeConstant pow(NodeConstant arg2)
	{
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(Math.pow(d, ((NodeDouble) arg2).doubleValue()));
		}
		else if (arg2 instanceof NodeRational)
		{
			return new NodeDouble(Math.pow(d, (arg2).doubleValue()));
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
			return new NodeDouble(Math.pow(d, (arg2).doubleValue()));
		}
	}

	@Override
	public NodeConstant subtract(NodeConstant arg2)
	{
		double d = doubleValue();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeDouble(d - ((NodeDouble) arg2).doubleValue());
		}
		else if (arg2 instanceof NodeRational)
		{
			return new NodeRational(value.subtract(((NodeRational) arg2).value));
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
			return new NodeDouble(d - (arg2).doubleValue());
		}
	}
}
