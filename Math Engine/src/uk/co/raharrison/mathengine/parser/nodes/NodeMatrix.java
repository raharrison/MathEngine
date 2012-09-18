package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.parser.operators.Determinable;

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

	public NodeMatrix(uk.co.raharrison.mathengine.linearalgebra.Matrix matrix)
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
		return new NodeMatrix(toNodeMatrix().add(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		return new NodeMatrix(toNodeMatrix().add(arg2));
	}

	@Override
	public NodeConstant add(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return number.add(arg2).toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		return new NodeMatrix(toNodeMatrix().add(arg2.toNodeVector()));
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

	@Override
	public int compareTo(NodeConstant cons)
	{
		double sum = toNodeMatrix().sum().doubleValue();

		if (cons instanceof NodeMatrix)
		{
			return Double.compare(sum, ((NodeMatrix) cons).toNodeMatrix().sum().doubleValue());
		}
		else
		{
			return new NodeDouble(sum).compareTo(cons);
		}
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		return new NodeMatrix(toNodeMatrix().divide(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		return new NodeMatrix(toNodeMatrix().divide(arg2));
	}

	@Override
	public NodeConstant divide(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return number.divide(arg2).toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		return new NodeMatrix(toNodeMatrix().divide(arg2.toNodeVector()));
	}

	@Override
	public double doubleValue()
	{
		return toNodeMatrix().sum().doubleValue();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeMatrix)
		{
			return this.toNodeMatrix().equals(((NodeMatrix) object).toNodeMatrix());
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
	public NodeConstant multiply(NodeMatrix arg2)
	{
		return new NodeMatrix(toNodeMatrix().multiplyElement(arg2.toNodeMatrix()));
	}

	// Matrix multiplication
	public NodeConstant multiplyMatrix(NodeMatrix arg2)
	{
		return new NodeMatrix(toNodeMatrix().multiply(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		return new NodeMatrix(toNodeMatrix().multiply(arg2));
	}

	@Override
	public NodeConstant multiply(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return number.multiply(arg2).toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		return new NodeMatrix(toNodeMatrix().multiply(arg2.toNodeVector()));
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		return new NodeMatrix(toNodeMatrix().pow(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		return new NodeMatrix(toNodeMatrix().pow(arg2));
	}

	@Override
	public NodeConstant pow(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return number.pow(arg2).toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		return new NodeMatrix(toNodeMatrix().pow(arg2.toNodeVector()));
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(toNodeMatrix().subtract(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		return new NodeMatrix(toNodeMatrix().subtract(arg2));
	}

	@Override
	public NodeConstant subtract(final NodePercent arg2)
	{
		return applyDeterminable(new Determinable()
		{
			@Override
			public NodeNumber getResult(NodeNumber number)
			{
				return number.subtract(arg2).toNodeNumber();
			}
		});
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		return new NodeMatrix(toNodeMatrix().subtract(arg2.toNodeVector()));
	}

	public uk.co.raharrison.mathengine.linearalgebra.Matrix toDoubleMatrix()
	{
		NodeNumber[][] a = toNodeMatrix().getElements();

		double[][] v = new double[a.length][a[0].length];

		for (int i = 0; i < v.length; i++)
		{
			for (int j = 0; j < v[0].length; j++)
			{
				v[i][j] = a[i][j].doubleValue();
			}
		}

		return new uk.co.raharrison.mathengine.linearalgebra.Matrix(v);
	}

	public Matrix toNodeMatrix()
	{
		NodeNumber[][] results = new NodeNumber[values.length][values[0].length];
		for (int i = 0; i < results.length; i++)
			for (int j = 0; j < results[0].length; j++)
				results[i][j] = values[i][j].toNodeNumber();

		return new Matrix(results);
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		return toNodeMatrix().sum();
	}

	public NodeNumber[][] toNodeNumbers()
	{
		return toNodeMatrix().getElements();
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

	@Override
	public String toTypeString()
	{
		return "matrix";
	}
}
