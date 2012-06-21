package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;

public final class NodeVector extends NodeConstant implements NodeMath
{
	private Node[] values;

	public NodeVector(Node[] values)
	{
		this.values = values;
	}

	public NodeVector(Vector v)
	{
		values = new NodeDouble[v.getSize()];

		for (int i = 0; i < values.length; i++)
		{
			values[i] = new NodeDouble(v.get(i));
		}
	}

	@Override
	public NodeConstant add(NodeConstant arg1)
	{
		Vector v = asDoubleVector();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeVector(v.add(((NodeDouble) arg1).asDouble()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return new NodeVector(v.add(((NodeVector) arg1).asDoubleVector()));

		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();

			Matrix m2 = new Matrix(v);

			m = m2.add(m);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeVector(v.add(((NodeBoolean) arg1).asDouble()));
		}
	}

	@Override
	public double asDouble()
	{
		if (values.length == 1)
		{
			if (values[0] instanceof NodeDouble)
			{
				return ((NodeDouble) values[0]).asDouble();
			}
		}

		throw new UnsupportedOperationException("Cannot convert vector to double");
	}

	public NodeDouble[] asDoubles()
	{
		NodeDouble[] vals = new NodeDouble[values.length];

		for (int i = 0; i < values.length; i++)
		{
			vals[i] = (((NodeDouble) values[i]).clone());
		}

		return vals;
	}

	public Vector asDoubleVector()
	{
		NodeDouble[] a = asDoubles();

		double[] v = new double[a.length];

		for (int i = 0; i < v.length; i++)
		{
			v[i] = a[i].getValue();
		}

		return new Vector(v);
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		double sum = asDoubleVector().sum();

		if (cons instanceof NodeVector)
		{
			return Double.compare(sum, ((NodeVector) cons).asDoubleVector().sum());
		}
		else
		{
			return new NodeDouble(sum).compareTo(cons);
		}
	}

	@Override
	public NodeConstant divide(NodeConstant arg1)
	{
		Vector v = asDoubleVector();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeVector(v.divide(((NodeDouble) arg1).asDouble()));
		}
		else if (arg1 instanceof NodeVector)
		{

			return new NodeVector(v.divide(((NodeVector) arg1).asDoubleVector()));

		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();

			Matrix m2 = new Matrix(v);

			m = m2.divide(m);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeVector(v.divide(((NodeBoolean) arg1).asDouble()));
		}
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeVector)
		{
			return this.asDoubleVector().equals(((NodeVector) object).asDoubleVector());
		}

		return false;
	}

	public int getSize()
	{
		return values.length;
	}

	public Node[] getValues()
	{
		return values;
	}

	@Override
	public int hashCode()
	{
		return values.hashCode();
	}

	public boolean isAllDoubles()
	{
		for (Node c : values)
		{
			if (!(c instanceof NodeDouble))
				return false;
		}

		return true;
	}

	@Override
	public NodeConstant multiply(NodeConstant arg1)
	{
		Vector v = asDoubleVector();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeVector(v.multiply(((NodeDouble) arg1).asDouble()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return new NodeVector(v.multiply(((NodeVector) arg1).asDoubleVector()));
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();
			return new NodeMatrix(m.multiply(v));
		}
		else
		{
			return new NodeVector(v.multiply(((NodeBoolean) arg1).asDouble()));
		}
	}

	@Override
	public NodeConstant pow(NodeConstant arg2)
	{
		Vector v = asDoubleVector();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeVector(v.pow(((NodeDouble) arg2).asDouble()));
		}
		else if (arg2 instanceof NodeVector)
		{

			return new NodeVector(v.pow(((NodeVector) arg2).asDoubleVector()));

		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg2).asDoubleMatrix();

			Matrix m2 = new Matrix(v);

			m = m2.pow(m);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeVector(v.pow(((NodeBoolean) arg2).asDouble()));
		}
	}

	public void setValue(NodeConstant[] values)
	{
		this.values = values;
	}

	@Override
	public NodeConstant subtract(NodeConstant arg1)
	{
		Vector v = asDoubleVector();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeVector(v.subtract(((NodeDouble) arg1).asDouble()));
		}
		else if (arg1 instanceof NodeVector)
		{

			return new NodeVector(v.subtract(((NodeVector) arg1).asDoubleVector()));

		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix m = ((NodeMatrix) arg1).asDoubleMatrix();

			Matrix m2 = new Matrix(v);

			m = m2.subtract(m);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeVector(v.subtract(((NodeBoolean) arg1).asDouble()));
		}
	}

	@Override
	public String toString()
	{
		if (values.length == 0)
		{
			return "{}";
		}

		String str = "{";

		for (int i = 0; i < values.length - 1; i++)
		{
			str += values[i].toString() + ", ";
		}

		str += values[values.length - 1].toString() + "}";
		return str;
	}
}
