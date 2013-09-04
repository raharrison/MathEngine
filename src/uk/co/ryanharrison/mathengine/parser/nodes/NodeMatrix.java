package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.operators.Determinable;

public final class NodeMatrix extends NodeConstant
{
	private Node[][] values;

	public NodeMatrix(Matrix matrix)
	{
		values = new Node[matrix.getRowCount()][matrix.getColumnCount()];

		for (int i = 0; i < matrix.getRowCount(); i++)
		{
			for (int j = 0; j < matrix.getColumnCount(); j++)
			{
				values[i][j] = matrix.get(i, j).clone();
			}
		}
	}

	public NodeMatrix(Node[][] values)
	{
		this.values = values;
	}

	public NodeMatrix(uk.co.ryanharrison.mathengine.linearalgebra.Matrix matrix)
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

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().add(arg2.toMatrix()));
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		return new NodeMatrix(toMatrix().add(arg2));
	}

	@Override
	public NodeConstant add(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return number.add(arg2).getTransformer().toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		return new NodeMatrix(toMatrix().add(arg2.toVector()));
	}

	@Override
	public NodeMatrix applyDeterminable(Determinable deter)
	{
		NodeConstant[][] results = new NodeConstant[getRowCount()][getColumnCount()];

		for (int i = 0; i < results.length; i++)
		{
			for (int j = 0; j < results[0].length; j++)
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

	@Override
	public int compareTo(NodeConstant cons)
	{
		double sum = toMatrix().sum().doubleValue();

		if (cons instanceof NodeMatrix)
		{
			return Double.compare(sum, ((NodeMatrix) cons).toMatrix().sum().doubleValue());
		}
		else
		{
			return new NodeDouble(sum).compareTo(cons);
		}
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().divide(arg2.toMatrix()));
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		return new NodeMatrix(toMatrix().divide(arg2));
	}

	@Override
	public NodeConstant divide(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return number.divide(arg2).getTransformer().toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		return new NodeMatrix(toMatrix().divide(arg2.toVector()));
	}

	@Override
	public double doubleValue()
	{
		return toMatrix().sum().doubleValue();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeMatrix)
		{
			return this.toMatrix().equals(((NodeMatrix) object).toMatrix());
		}

		return false;
	}

	public int getColumnCount()
	{
		if (getRowCount() == 0)
			return 0;

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
	public NodeConstant multiply(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().multiplyElement(arg2.toMatrix()));
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		return new NodeMatrix(toMatrix().multiply(arg2));
	}

	@Override
	public NodeConstant multiply(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return number.multiply(arg2).getTransformer().toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		return new NodeMatrix(toMatrix().multiply(arg2.toVector()));
	}

	// Matrix multiplication
	public NodeConstant multiplyMatrix(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().multiply(arg2.toMatrix()));
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().pow(arg2.toMatrix()));
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		return new NodeMatrix(toMatrix().pow(arg2));
	}

	@Override
	public NodeConstant pow(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return number.pow(arg2).getTransformer().toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		return new NodeMatrix(toMatrix().pow(arg2.toVector()));
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(toMatrix().subtract(arg2.toMatrix()));
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		return new NodeMatrix(toMatrix().subtract(arg2));
	}

	@Override
	public NodeConstant subtract(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeConstant number)
			{
				return number.subtract(arg2).getTransformer().toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		return new NodeMatrix(toMatrix().subtract(arg2.toVector()));
	}

	public uk.co.ryanharrison.mathengine.linearalgebra.Matrix toDoubleMatrix()
	{
		NodeNumber[][] a = toMatrix().getElements();

		double[][] v = new double[a.length][a[0].length];

		for (int i = 0; i < v.length; i++)
		{
			for (int j = 0; j < v[0].length; j++)
			{
				v[i][j] = a[i][j].doubleValue();
			}
		}

		return new uk.co.ryanharrison.mathengine.linearalgebra.Matrix(v);
	}

	public Matrix toMatrix()
	{
		NodeNumber[][] results = new NodeNumber[getRowCount()][getColumnCount()];
		for (int i = 0; i < results.length; i++)
			for (int j = 0; j < results[0].length; j++)
				results[i][j] = values[i][j].getTransformer().toNodeNumber();

		return new Matrix(results);
	}

	public String toShortString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if (values.length == 0)
			return "[]";

		for (int i = 0; i < values.length; i++)
		{
			builder.append(new NodeVector(values[i]).toString());
		}

		builder.append("]");
		return builder.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		String tmp;

		int m = this.values.length;
		if (m == 0)
			return "[]";

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

	@Override
	public String toTypeString()
	{
		return "matrix";
	}

	@Override
	public NodeTransformer getTransformer()
	{
		if (this.transformer == null)
			this.transformer = new NodeMatrixTransformer();

		return this.transformer;
	}

	private class NodeMatrixTransformer implements NodeTransformer
	{

		@Override
		public NodeVector toNodeVector()
		{
			NodeVector[] vectors = new NodeVector[values.length];
			for (int i = 0; i < values.length; i++)
			{
				vectors[i] = new NodeVector(values[i]);
			}
			return new NodeVector(vectors);
		}

		@Override
		public NodeNumber toNodeNumber()
		{
			return toMatrix().sum();
		}
	}
}
