package uk.co.ryanharrison.mathengine.linearalgebra;

public final class Matrix implements Cloneable
{
	private double[][] elements;

	private int rows, columns;

	public Matrix(double d)
	{
		this.rows = 1;
		this.columns = 1;
		this.elements = new double[1][1];
		elements[0][0] = d;
	}

	public Matrix(double vals[], int m)
	{
		this.rows = m;
		columns = m != 0 ? vals.length / m : 0;
		if (m * columns != vals.length)
		{
			throw new IllegalArgumentException("Array length must be a multiple of m.");
		}
		elements = new double[m][columns];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				elements[i][j] = vals[i + j * m];
			}
		}
	}

	public Matrix(double[] vector)
	{
		this.rows = vector.length;
		this.columns = 1;
		this.elements = new double[rows][columns];

		for (int i = 0; i < rows; i++)
		{
			elements[i][0] = vector[i];
		}
	}

	public Matrix(double[][] matrix)
	{
		rows = matrix.length;
		columns = matrix[0].length;
		for (int i = 0; i < rows; i++)
		{
			if (matrix[i].length != columns)
			{
				throw new IllegalArgumentException("All rows must have the same length.");
			}
		}

		this.elements = matrix.clone();
	}

	protected Matrix(double[][] A, int m, int n)
	{
		this.elements = A;
		this.rows = m;
		this.columns = n;
	}

	public Matrix(int n)
	{
		this.rows = n;
		this.columns = n;
		this.elements = new double[n][n];
	}

	public Matrix(int m, int n)
	{
		this.rows = m;
		this.columns = n;
		elements = new double[m][n];
	}

	public Matrix(int m, int n, double s)
	{
		this.rows = m;
		this.columns = n;
		elements = new double[m][n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				elements[i][j] = s;
			}
		}
	}

	public Matrix(Matrix matrix)
	{
		this(matrix.elements.clone());
	}

	public Matrix(Vector v)
	{
		this.rows = 1;
		this.columns = v.getSize();
		this.elements = new double[rows][columns];

		for (int i = 0; i < columns; i++)
		{
			elements[0][i] = v.get(i);
		}
	}

	public Matrix add(double d)
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] + d;
			}
		}
		return X;
	}

	public Matrix add(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] + B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix add(Vector v)
	{
		normalizeVectorSize(v);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] + v.get(j);
			}
		}

		return X;

	}

	/**
	 * Element-by-element left division, C = A.\B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.\B
	 */

	public Matrix arrayLeftDivide(Matrix B)
	{
		checkMatrixDimensions(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = B.elements[i][j] / elements[i][j];
			}
		}
		return X;
	}

	/**
	 * Element-by-element multiplication, C = A.*B
	 * 
	 * @param B
	 *            another matrix
	 * @return A.*B
	 */

	public Matrix arrayMultiply(Matrix B)
	{
		checkMatrixDimensions(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] * B.elements[i][j];
			}
		}
		return X;
	}

	/**
	 * Element-by-element right division, C = A./B
	 * 
	 * @param B
	 *            another matrix
	 * @return A./B
	 */

	public Matrix arrayRightDivide(Matrix B)
	{
		checkMatrixDimensions(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] / B.elements[i][j];
			}
		}
		return X;
	}

	private void checkMatrixDimensions(Matrix B)
	{
		if (B.rows != rows || B.columns != columns)
		{
			throw new IllegalArgumentException("Matrix dimensions must agree.");
		}
	}

	@Override
	public Object clone()
	{
		return this.copy();
	}

	public Matrix copy()
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j];
			}
		}
		return X;
	}

	/**
	 * Matrix determinant
	 * 
	 * @return determinant
	 */

	public double determinant()
	{
		return new LUDecomposition(this).getDeterminant();
	}

	public Matrix divide(double d)
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] / d;
			}
		}
		return X;
	}

	public Matrix divide(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] / B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix divide(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] / v.get(j);
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
					if (!(a.elements[i][j] == this.elements[i][j]))
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

	public double get(int i, int j)
	{
		return elements[i][j];
	}

	public double[][] getArrayCopy()
	{
		double[][] copy = new double[rows][columns];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				copy[i][j] = elements[i][j];
			}
		}
		return copy;
	}

	public int getColumnCount()
	{
		return columns;
	}

	public double[] getColumnPackedCopy()
	{
		double[] vals = new double[rows * columns];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				vals[i + j * rows] = elements[i][j];
			}
		}
		return vals;
	}

	public double[][] getElements()
	{
		return this.elements;
	}

	// Operators

	/**
	 * LU Decomposition
	 * 
	 * @return LUDecomposition
	 * @see LUDecomposition
	 */

	public LUDecomposition getLUDecomposition()
	{
		return new LUDecomposition(this);
	}

	public Matrix getMatrix(int i0, int i1, int j0, int j1)
	{
		Matrix X = new Matrix(i1 - i0 + 1, j1 - j0 + 1);
		double[][] B = X.getElements();
		try
		{
			for (int i = i0; i <= i1; i++)
			{
				for (int j = j0; j <= j1; j++)
				{
					B[i - i0][j - j0] = elements[i][j];
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}

	public Matrix getMatrix(int[] r, int j0, int j1)
	{
		Matrix X = new Matrix(r.length, j1 - j0 + 1);
		double[][] B = X.getElements();
		try
		{
			for (int i = 0; i < r.length; i++)
			{
				for (int j = j0; j <= j1; j++)
				{
					B[i][j - j0] = elements[r[i]][j];
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}

	/**
	 * QR Decomposition
	 * 
	 * @return QRDecomposition
	 * @see QRDecomposition
	 */

	public QRDecomposition getQRDecomposition()
	{
		return new QRDecomposition(this);
	}

	public int getRowCount()
	{
		return rows;
	}

	public double[] getRowPackedCopy()
	{
		double[] vals = new double[rows * columns];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				vals[i * columns + j] = elements[i][j];
			}
		}
		return vals;
	}

	@Override
	public int hashCode()
	{
		return elements.hashCode();
	}

	/**
	 * Matrix inverse or pseudoinverse
	 * 
	 * @return inverse(A) if A is square, pseudoinverse otherwise.
	 */

	public Matrix inverse()
	{
		return solve(getIdentityNxN(rows));
	}

	public boolean isIdentity()
	{
		if (!this.isSquare())
		{
			throw new UnsupportedOperationException("Matrix is not square");
		}

		int m = this.rows;
		int n = this.columns;
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i == j && this.get(i, j) != 1)
					return false;
				if (i != j && this.get(i, j) != 0)
					return false;
			}
		}
		return true;
	}

	public boolean isSquare()
	{
		return this.columns == this.rows;
	}

	public boolean isSymmetric()
	{
		return this.equals(this.transpose());
	}

	public Matrix multiply(double d)
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] * d;
			}
		}
		return X;
	}

	/**
	 * Linear algebraic matrix multiplication, A * B
	 * 
	 * @param B
	 *            another matrix
	 * @return Matrix product, A * B
	 * @exception IllegalArgumentException
	 *                Matrix inner dimensions must agree.
	 */

	public Matrix multiply(Matrix B)
	{
		normalizeMatrixSizes(B);

		if (B.rows != columns)
		{
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}
		Matrix X = new Matrix(rows, B.columns);
		double[][] C = X.getElements();
		double[] Bcolj = new double[columns];
		for (int j = 0; j < B.columns; j++)
		{
			for (int k = 0; k < columns; k++)
			{
				Bcolj[k] = B.elements[k][j];
			}
			for (int i = 0; i < rows; i++)
			{
				double[] Arowi = elements[i];
				double s = 0;
				for (int k = 0; k < columns; k++)
				{
					s += Arowi[k] * Bcolj[k];
				}
				C[i][j] = s;
			}
		}
		return X;
	}

	public Matrix multiply(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] * v.get(j);
			}
		}

		return X;

	}

	public double norm1()
	{
		double f = 0;
		for (int j = 0; j < columns; j++)
		{
			double s = 0;
			for (int i = 0; i < rows; i++)
			{
				s += Math.abs(elements[i][j]);
			}
			f = Math.max(f, s);
		}
		return f;
	}

	private void normalizeMatrixSizes(Matrix b)
	{
		if (this.rows == b.rows && this.columns == b.columns)
			return;

		// m == rows
		int longest = Math.max(this.rows, b.rows);

		if (this.rows != longest)
		{
			double[][] results = new double[longest][columns];

			for (int i = 0; i < this.rows; i++)
			{
				for (int j = 0; j < this.columns; j++)
				{
					results[i][j] = this.elements[i][j];
				}
			}

			this.elements = results;
			this.rows = longest;
		}
		else
		{
			double[][] results = new double[longest][b.columns];

			for (int i = 0; i < b.rows; i++)
			{
				for (int j = 0; j < b.columns; j++)
				{
					results[i][j] = b.elements[i][j];
				}
			}

			b.elements = results;
			b.rows = longest;
		}

		longest = Math.max(this.columns, b.columns);

		if (this.columns != longest)
		{
			double[][] results = new double[this.rows][longest];

			for (int i = 0; i < this.rows; i++)
			{
				for (int j = 0; j < this.columns; j++)
				{
					results[i][j] = this.elements[i][j];
				}
			}

			this.elements = results;
			this.columns = longest;
		}
		else
		{
			double[][] results = new double[b.rows][longest];

			for (int i = 0; i < b.rows; i++)
			{
				for (int j = 0; j < b.columns; j++)
				{
					results[i][j] = b.elements[i][j];
				}
			}

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
				double[] results = new double[longest];

				for (int j = 0; j < this.columns; j++)
				{
					results[j] = this.elements[i][j];
				}

				this.elements[i] = results;
			}

			this.columns = longest;
		}
		else
		{
			double[] results = new double[longest];

			for (int i = 0; i < b.getSize(); i++)
			{
				results[i] = b.get(i);
			}

			b.setElements(results);
		}
	}

	public Matrix pow(double d)
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = Math.pow(elements[i][j], d);
			}
		}
		return X;
	}

	public Matrix pow(Matrix matrix)
	{
		normalizeMatrixSizes(matrix);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = Math.pow(elements[i][j], matrix.elements[i][j]);
			}
		}
		return X;
	}

	public Matrix pow(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = Math.pow(elements[i][j], v.get(j));
			}
		}

		return X;

	}

	public void set(int i, int j, double s)
	{
		elements[i][j] = s;
	}

	/**
	 * Solve A*X = B
	 * 
	 * @param B
	 *            right hand side
	 * @return solution if A is square, least squares solution otherwise
	 */

	public Matrix solve(Matrix B)
	{
		return rows == columns ? new LUDecomposition(this).solve(B) : new QRDecomposition(this)
				.solve(B);
	}

	public Matrix subtract(double d)
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] - d;
			}
		}
		return X;
	}

	/**
	 * C = A - B
	 * 
	 * @param B
	 *            another matrix
	 * @return A - B
	 */

	public Matrix subtract(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] - B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix subtract(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = elements[i][j] - v.get(j);
			}
		}

		return X;

	}

	public double sum()
	{
		double sum = 0;

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				sum += elements[i][j];
			}
		}
		return sum;
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

				tmp += Double.toString(this.elements[i][j]);
				builder.append(tmp);
			}
		}

		return builder.toString().trim();
	}

	public double trace()
	{
		double t = 0;
		for (int i = 0; i < Math.min(rows, columns); i++)
		{
			t += elements[i][i];
		}
		return t;
	}

	public Matrix transpose()
	{
		Matrix x = new Matrix(columns, rows);
		double[][] c = x.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				c[j][i] = elements[i][j];
			}
		}
		return x;
	}

	public Matrix uminus()
	{
		Matrix X = new Matrix(rows, columns);
		double[][] C = X.getElements();
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				C[i][j] = -elements[i][j];
			}
		}
		return X;
	}

	public static Matrix getIdentityNxN(int n)
	{
		Matrix m = new Matrix(n);
		for (int i = 0; i < n; i++)
		{
			m.set(i, i, 1);
		}
		return m;
	}

	public static Matrix random(int m, int n)
	{
		double[][] elements = new double[m][n];

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				elements[i][j] = Math.random();
			}
		}

		return new Matrix(elements);
	}
}