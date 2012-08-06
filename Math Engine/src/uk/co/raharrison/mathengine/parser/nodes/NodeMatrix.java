package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.parser.operators.Determinable;

public class NodeMatrix extends NodeConstant implements NodeMath
{
	private Node[][] values;

	// TODO : Implement custom Matrix class to handle both doubles and Rationals
	public NodeMatrix(Matrix matrix)
	{
		values = new Node[matrix.getRowCount()][matrix.getColumnCount()];

		for (int i = 0; i < matrix.getRowCount(); i++)
		{
			for (int j = 0; j < matrix.getColumnCount(); j++)
			{
				values[i][j] = new NodeDouble(matrix.get(i, j));
			}
		}
	}

	public NodeMatrix(Node[][] values)
	{
		this.values = values;
	}

	@Override
	public NodeConstant add(NodeConstant arg1)
	{
		Matrix m = asDoubleMatrix();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeMatrix(m.add(((NodeDouble) arg1).doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return new NodeMatrix(m.add(((NodeVector) arg1).asDoubleVector()));
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix mat = ((NodeMatrix) arg1).asDoubleMatrix();

			m = m.add(mat);
			return new NodeMatrix(m);
		}
		else
		{
			return new NodeMatrix(m.add((arg1).doubleValue()));
		}
	}

	@Override
	public NodeMatrix applyDeterminable(Determinable deter)
	{
		NodeConstant[][] results = new NodeConstant[values.length][values[0].length];

		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < values[0].length; j++)
			{
				if (values[i][j] instanceof NodeNumber)
				{
					results[i][j] = deter.getResult((NodeNumber) values[i][j]);
				}
				else
				{
					results[i][j] = ((NodeConstant) values[i][j]).applyDeterminable(deter);
				}
			}
		}

		return new NodeMatrix(results);
	}

	public Matrix asDoubleMatrix()
	{
		NodeDouble[][] a = asDoubles();

		double[][] v = new double[a.length][a[0].length];

		for (int i = 0; i < v.length; i++)
		{
			for (int j = 0; j < v[0].length; j++)
			{
				v[i][j] = a[i][j].doubleValue();
			}
		}

		return new Matrix(v);
	}

	// TODO : Change to return a 2d array of NodeNumber objects
	public NodeDouble[][] asDoubles()
	{
		NodeDouble[][] vals = new NodeDouble[values.length][values[0].length];

		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < values[0].length; j++)
			{
				if (values[i][j] instanceof NodeRational)
					vals[i][j] = new NodeDouble(((NodeRational) values[i][j]).doubleValue());
				else
					vals[i][j] = ((NodeDouble) values[i][j]).clone();
			}
		}

		return vals;
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		double sum = asDoubleMatrix().sum();

		if (cons instanceof NodeMatrix)
		{
			return Double.compare(sum, ((NodeMatrix) cons).asDoubleMatrix().sum());
		}
		else
		{
			return new NodeDouble(sum).compareTo(cons);
		}
	}

	@Override
	public NodeConstant divide(NodeConstant arg1)
	{
		Matrix m = asDoubleMatrix();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeMatrix(m.divide(((NodeDouble) arg1).doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return new NodeMatrix(m.divide(((NodeVector) arg1).asDoubleVector()));
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix mat = ((NodeMatrix) arg1).asDoubleMatrix();

			m = m.divide(mat);
			return new NodeMatrix(m);
		}
		else
		{
			return new NodeMatrix(m.divide((arg1).doubleValue()));
		}
	}

	@Override
	public double doubleValue()
	{
		if (values.length == 1 && values[0].length == 1)
		{
			if (values[0][0] instanceof NodeNumber)
			{
				return ((NodeNumber) values[0][0]).doubleValue();
			}
		}

		throw new UnsupportedOperationException("Cannot convert matrix to double");
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeMatrix)
		{
			return this.asDoubleMatrix().equals(((NodeMatrix) object).asDoubleMatrix());
		}

		return false;
	}

	public int getColumnCount()
	{
		return values[0].length;
	}

	public int getRowCount()
	{
		return values.length;
	}

	public Node[][] getValues()
	{
		return values;
	}

	@Override
	public int hashCode()
	{
		return values.hashCode();
	}

	@Override
	public NodeConstant multiply(NodeConstant arg1)
	{
		Matrix m = asDoubleMatrix();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeMatrix(m.multiply(((NodeDouble) arg1).doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{
			return new NodeMatrix(m.multiply(((NodeVector) arg1).asDoubleVector()));
		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix mat = ((NodeMatrix) arg1).asDoubleMatrix();

			m = m.multiply(mat);
			return new NodeMatrix(m);
		}
		else
		{
			return new NodeMatrix(m.multiply((arg1).doubleValue()));
		}
	}

	@Override
	public NodeConstant pow(NodeConstant arg2)
	{
		Matrix m = asDoubleMatrix();

		if (arg2 instanceof NodeDouble)
		{
			return new NodeMatrix(m.pow(((NodeDouble) arg2).doubleValue()));
		}
		else if (arg2 instanceof NodeVector)
		{

			return new NodeMatrix(m.pow(((NodeVector) arg2).asDoubleVector()));

		}
		else if (arg2 instanceof NodeMatrix)
		{
			Matrix mat = ((NodeMatrix) arg2).asDoubleMatrix();

			m = m.pow(mat);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeMatrix(m.pow((arg2).doubleValue()));
		}
	}

	@Override
	public NodeConstant subtract(NodeConstant arg1)
	{
		Matrix m = asDoubleMatrix();

		if (arg1 instanceof NodeDouble)
		{
			return new NodeMatrix(m.subtract(((NodeDouble) arg1).doubleValue()));
		}
		else if (arg1 instanceof NodeVector)
		{

			return new NodeMatrix(m.subtract(((NodeVector) arg1).asDoubleVector()));

		}
		else if (arg1 instanceof NodeMatrix)
		{
			Matrix mat = ((NodeMatrix) arg1).asDoubleMatrix();

			m = m.subtract(mat);
			return new NodeMatrix(m);

		}
		else
		{
			return new NodeMatrix(m.subtract((arg1).doubleValue()));
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		String tmp;

		int m = this.values.length;
		int n = this.values[0].length;

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				tmp = j == 0 ? "\n" : "\t";

				tmp += this.values[i][j].toString();
				builder.append(tmp);
			}
		}

		return builder.toString().trim();
	}
}
