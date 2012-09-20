package uk.co.raharrison.mathengine.parser.nodes;

final class Matrix
{
	private NodeNumber[][] elements;

	private int rows, columns;

	public Matrix(int m, int n)
	{
		this.rows = m;
		this.columns = n;
		elements = new NodeNumber[m][n];
	}

	public Matrix(NodeNumber d)
	{
		this.rows = 1;
		this.columns = 1;
		this.elements = new NodeNumber[1][1];
		elements[0][0] = d.clone();
	}

	public Matrix(NodeNumber[] vector)
	{
		this.rows = vector.length;
		this.columns = 1;
		this.elements = new NodeNumber[rows][columns];

		for (int i = 0; i < rows; i++)
		{
			elements[i][0] = vector[i].clone();
		}
	}

	public Matrix(NodeNumber[][] matrix)
	{
		rows = matrix.length;
		columns = rows == 0 ? 0 : matrix[0].length;
		for (int i = 0; i < rows; i++)
		{
			if (matrix[i].length != columns)
			{
				throw new IllegalArgumentException("All rows must have the same length.");
			}
		}

		this.elements = matrix.clone();
	}

	public Matrix(Vector v)
	{
		this.rows = 1;
		this.columns = v.getSize();
		this.elements = new NodeNumber[rows][columns];

		for (int i = 0; i < columns; i++)
		{
			elements[0][i] = v.get(i).clone();
		}
	}

	public Matrix add(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].add(B.elements[i][j]));
			}
		}
		return X;
	}

	public Matrix add(NodeNumber d)
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].add(d));
			}
		}
		return X;
	}

	public Matrix add(Vector v)
	{
		normalizeVectorSize(v);
		Matrix X = new Matrix(rows, columns);

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].add(v.get(j)));
			}
		}
		return X;
	}

	@Override
	public Object clone()
	{
		return this.copy();
	}

	public Matrix copy()
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].clone());
			}
		}
		return X;
	}

	public Matrix divide(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].divide(B.elements[i][j]));
			}
		}
		return X;
	}

	public Matrix divide(NodeNumber d)
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].divide(d));
			}
		}
		return X;
	}

	public Matrix divide(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].divide(v.get(j)));
			}
		}

		return X;
	}

	@Override
	public boolean equals(Object arg)
	{
		if (arg instanceof Matrix)
		{
			Matrix a = (Matrix) arg;

			if (this.rows != a.rows || this.columns != a.columns)
			{
				return false;
			}

			int m = a.rows;
			int n = a.columns;

			for (int i = 0; i < m; i++)
			{
				for (int j = 0; j < n; j++)
				{
					if (!(a.elements[i][j].equals(this.elements[i][j])))
					{
						return false;
					}
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	public NodeNumber get(int i, int j)
	{
		return elements[i][j];
	}

	public int getColumnCount()
	{
		return columns;
	}

	public NodeNumber[] getColumnPackedCopy()
	{
		NodeNumber[] vals = new NodeNumber[rows * columns];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				vals[i + j * rows] = elements[i][j].clone();
			}
		}
		return vals;
	}

	public NodeNumber[][] getElements()
	{
		return this.elements;
	}

	public int getRowCount()
	{
		return rows;
	}

	@Override
	public int hashCode()
	{
		return elements.hashCode();
	}

	public Matrix multiply(Matrix B)
	{
		normalizeMatrixSizes(B);

		if (B.rows != columns)
		{
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}
		Matrix X = new Matrix(rows, B.columns);
		NodeNumber[] Bcolj = new NodeNumber[columns];
		for (int j = 0; j < B.columns; j++)
		{
			for (int k = 0; k < columns; k++)
			{
				Bcolj[k] = B.elements[k][j];
			}
			for (int i = 0; i < rows; i++)
			{
				NodeNumber[] Arowi = elements[i];
				NodeNumber s = NodeFactory.createZeroNumber();
				for (int k = 0; k < columns; k++)
				{
					s = s.add(Arowi[k].multiply(Bcolj[k])).toNodeNumber();
				}

				X.set(i, j, s);
			}
		}
		return X;
	}

	public Matrix multiplyElement(Matrix m)
	{
		normalizeMatrixSizes(m);
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].multiply(m.elements[i][j]));
			}
		}
		return X;
	}

	public Matrix multiply(NodeNumber d)
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].multiply(d));
			}
		}
		return X;
	}

	public Matrix multiply(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].multiply(v.get(j)));
			}
		}

		return X;

	}

	private void normalizeMatrixSizes(Matrix b)
	{
		if (this.rows == b.rows && this.columns == b.columns)
			return;

		// m == rows
		int longest = Math.max(this.rows, b.rows);

		if (this.rows != longest)
		{
			NodeNumber[][] results = new NodeNumber[longest][columns];

			for (int i = 0; i < this.rows; i++)
				for (int j = 0; j < this.columns; j++)
					results[i][j] = this.elements[i][j];

			for (int i = this.rows; i < longest; i++)
				for (int j = 0; j < columns; j++)
					results[i][j] = NodeFactory.createZeroNumber();

			this.elements = results;
			this.rows = longest;
		}
		else
		{
			NodeNumber[][] results = new NodeNumber[longest][b.columns];

			for (int i = 0; i < b.rows; i++)
				for (int j = 0; j < b.columns; j++)
					results[i][j] = b.elements[i][j];

			for (int i = b.rows; i < longest; i++)
				for (int j = 0; j < b.columns; j++)
					results[i][j] = NodeFactory.createZeroNumber();

			b.elements = results;
			b.rows = longest;
		}

		longest = Math.max(this.columns, b.columns);

		if (this.columns != longest)
		{
			NodeNumber[][] results = new NodeNumber[this.rows][longest];

			for (int i = 0; i < this.rows; i++)
				for (int j = 0; j < this.columns; j++)
					results[i][j] = this.elements[i][j];

			for (int i = 0; i < this.rows; i++)
				for (int j = this.columns; j < longest; j++)
					results[i][j] = NodeFactory.createZeroNumber();

			this.elements = results;
			this.columns = longest;
		}
		else
		{
			NodeNumber[][] results = new NodeNumber[b.rows][longest];

			for (int i = 0; i < b.rows; i++)
				for (int j = 0; j < b.columns; j++)
					results[i][j] = b.elements[i][j];

			for (int i = 0; i < b.rows; i++)
				for (int j = b.columns; j < longest; j++)
					results[i][j] = NodeFactory.createZeroNumber();

			b.elements = results;
			b.columns = longest;
		}
	}

	private void normalizeVectorSize(Vector b)
	{
		if (this.columns == b.getSize())
			return;

		int longest = Math.max(this.columns, b.getSize());

		if (this.columns != longest)
		{
			for (int i = 0; i < rows; i++)
			{
				NodeNumber[] results = new NodeNumber[longest];

				for (int j = 0; j < this.columns; j++)
					results[j] = this.elements[i][j];
				for (int j = this.columns; j < results.length; j++)
					results[j] = NodeFactory.createZeroNumber();

				this.elements[i] = results;
			}

			this.columns = longest;
		}
		else
		{
			NodeNumber[] results = new NodeNumber[longest];

			for (int i = 0; i < b.getSize(); i++)
				results[i] = b.get(i);
			for (int i = b.getSize(); i < results.length; i++)
				results[i] = NodeFactory.createZeroNumber();

			b.setElements(results);
		}
	}

	public Matrix pow(Matrix matrix)
	{
		normalizeMatrixSizes(matrix);
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].pow(matrix.elements[i][j]));
			}
		}
		return X;
	}

	public Matrix pow(NodeNumber d)
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].pow(d));
			}
		}
		return X;
	}

	public Matrix pow(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].pow(v.get(j)));
			}
		}

		return X;

	}

	public void set(int i, int j, NodeConstant constant)
	{
		elements[i][j] = constant.toNodeNumber();
	}

	public Matrix subtract(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].subtract(B.elements[i][j]));
			}
		}
		return X;
	}

	public Matrix subtract(NodeNumber d)
	{
		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].subtract(d));
			}
		}
		return X;
	}

	public Matrix subtract(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				X.set(i, j, elements[i][j].subtract(v.get(j)));
			}
		}

		return X;

	}

	public NodeNumber sum()
	{
		NodeNumber result = NodeFactory.createZeroNumber();
		for (int i = 0; i < elements.length; i++)
			for (int j = 0; j < elements[0].length; j++)
				result = result.add(elements[i][j]).toNodeNumber();

		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		String tmp;

		int m = this.rows;
		int n = this.columns;

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				tmp = j == 0 ? "\n" : "\t";

				tmp += this.elements[i][j].toString();
				builder.append(tmp);
			}
		}

		return builder.toString().trim();
	}
}
