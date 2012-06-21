package uk.co.raharrison.mathengine.linearalgebra;

public final class Matrix implements Cloneable
{
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

	private double[][] elements;

	private int m, n;

	public Matrix(double d)
	{
		this.m = 1;
		this.n = 1;
		this.elements = new double[1][1];
		elements[0][0] = d;
	}

	public Matrix(double vals[], int m)
	{
		this.m = m;
		n = m != 0 ? vals.length / m : 0;
		if (m * n != vals.length)
		{
			throw new IllegalArgumentException("Array length must be a multiple of m.");
		}
		elements = new double[m][n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				elements[i][j] = vals[i + j * m];
			}
		}
	}

	public Matrix(double[] vector)
	{
		this.m = vector.length;
		this.n = 1;
		this.elements = new double[m][n];

		for (int i = 0; i < m; i++)
		{
			elements[i][0] = vector[i];
		}
	}

	public Matrix(double[][] matrix)
	{
		m = matrix.length;
		n = matrix[0].length;
		for (int i = 0; i < m; i++)
		{
			if (matrix[i].length != n)
			{
				throw new IllegalArgumentException("All rows must have the same length.");
			}
		}

		this.elements = matrix.clone();
	}

	protected Matrix(double[][] A, int m, int n)
	{
		this.elements = A;
		this.m = m;
		this.n = n;
	}

	public Matrix(int n)
	{
		this.m = n;
		this.n = n;
		this.elements = new double[n][n];
	}

	public Matrix(int m, int n)
	{
		this.m = m;
		this.n = n;
		elements = new double[m][n];
	}

	public Matrix(int m, int n, double s)
	{
		this.m = m;
		this.n = n;
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
		this.m = 1;
		this.n = v.getSize();
		this.elements = new double[m][n];

		for (int i = 0; i < n; i++)
		{
			elements[0][i] = v.get(i);
		}
	}

	public Matrix add(double d)
	{
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] + d;
			}
		}
		return X;
	}

	public Matrix add(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] + B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix add(Vector v)
	{
		normalizeVectorSize(v);
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] / B.elements[i][j];
			}
		}
		return X;
	}

	private void checkMatrixDimensions(Matrix B)
	{
		if (B.m != m || B.n != n)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] / d;
			}
		}
		return X;
	}

	public Matrix divide(Matrix B)
	{
		normalizeMatrixSizes(B);
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] / B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix divide(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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

			if (this.m != a.m || this.n != a.n)
			{
				return false;
			}

			int m = a.m;
			int n = a.n;

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
		double[][] copy = new double[m][n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				copy[i][j] = elements[i][j];
			}
		}
		return copy;
	}

	public int getColumnCount()
	{
		return n;
	}

	// Operators

	public double[] getColumnPackedCopy()
	{
		double[] vals = new double[m * n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				vals[i + j * m] = elements[i][j];
			}
		}
		return vals;
	}

	public double[][] getElements()
	{
		return this.elements;
	}

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
		return m;
	}

	public double[] getRowPackedCopy()
	{
		double[] vals = new double[m * n];
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				vals[i * n + j] = elements[i][j];
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
		return solve(getIdentityNxN(m));
	}

	public boolean isIdentity()
	{
		if (!this.isSquare())
		{
			throw new UnsupportedOperationException("Matrix is not square");
		}

		int m = this.m;
		int n = this.n;
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
		return this.n == this.m;
	}

	public boolean isSymmetric()
	{
		return this.equals(this.transpose());
	}

	public Matrix multiply(double d)
	{
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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

		if (B.m != n)
		{
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}
		Matrix X = new Matrix(m, B.n);
		double[][] C = X.getElements();
		double[] Bcolj = new double[n];
		for (int j = 0; j < B.n; j++)
		{
			for (int k = 0; k < n; k++)
			{
				Bcolj[k] = B.elements[k][j];
			}
			for (int i = 0; i < m; i++)
			{
				double[] Arowi = elements[i];
				double s = 0;
				for (int k = 0; k < n; k++)
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

		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] * v.get(j);
			}
		}

		return X;

	}

	public double norm1()
	{
		double f = 0;
		for (int j = 0; j < n; j++)
		{
			double s = 0;
			for (int i = 0; i < m; i++)
			{
				s += Math.abs(elements[i][j]);
			}
			f = Math.max(f, s);
		}
		return f;
	}

	private void normalizeMatrixSizes(Matrix b)
	{
		if (this.m == b.m && this.n == b.n)
			return;

		// m == rows
		int longest = Math.max(this.m, b.m);

		if (this.m != longest)
		{
			double[][] results = new double[longest][n];

			for (int i = 0; i < this.m; i++)
			{
				for (int j = 0; j < this.n; j++)
				{
					results[i][j] = this.elements[i][j];
				}
			}

			this.elements = results;
			this.m = longest;
		}
		else
		{
			double[][] results = new double[longest][b.n];

			for (int i = 0; i < b.m; i++)
			{
				for (int j = 0; j < b.n; j++)
				{
					results[i][j] = b.elements[i][j];
				}
			}

			b.elements = results;
			b.m = longest;
		}

		longest = Math.max(this.n, b.n);

		if (this.n != longest)
		{
			double[][] results = new double[this.m][longest];

			for (int i = 0; i < this.m; i++)
			{
				for (int j = 0; j < this.n; j++)
				{
					results[i][j] = this.elements[i][j];
				}
			}

			this.elements = results;
			this.n = longest;
		}
		else
		{
			double[][] results = new double[b.m][longest];

			for (int i = 0; i < b.m; i++)
			{
				for (int j = 0; j < b.n; j++)
				{
					results[i][j] = b.elements[i][j];
				}
			}

			b.elements = results;
			b.n = longest;
		}
	}

	private void normalizeVectorSize(Vector b)
	{
		if (this.n == b.getSize())
			return;

		int longest = Math.max(this.n, b.getSize());

		if (this.n != longest)
		{
			for (int i = 0; i < m; i++)
			{
				double[] results = new double[longest];

				for (int j = 0; j < this.n; j++)
				{
					results[i] = this.elements[i][j];
				}

				this.elements[i] = results;
			}

			this.n = longest;
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = Math.pow(elements[i][j], d);
			}
		}
		return X;
	}

	public Matrix pow(Matrix matrix)
	{
		normalizeMatrixSizes(matrix);
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = Math.pow(elements[i][j], matrix.elements[i][j]);
			}
		}
		return X;
	}

	public Matrix pow(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		return m == n ? new LUDecomposition(this).solve(B) : new QRDecomposition(this).solve(B);
	}

	public Matrix subtract(double d)
	{
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] - B.elements[i][j];
			}
		}
		return X;
	}

	public Matrix subtract(Vector v)
	{
		normalizeVectorSize(v);

		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = elements[i][j] - v.get(j);
			}
		}

		return X;

	}

	public double sum()
	{
		double sum = 0;

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
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

		int m = this.m;
		int n = this.n;

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
		for (int i = 0; i < Math.min(m, n); i++)
		{
			t += elements[i][i];
		}
		return t;
	}

	public Matrix transpose()
	{
		Matrix x = new Matrix(n, m);
		double[][] c = x.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				c[j][i] = elements[i][j];
			}
		}
		return x;
	}

	public Matrix uminus()
	{
		Matrix X = new Matrix(m, n);
		double[][] C = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				C[i][j] = -elements[i][j];
			}
		}
		return X;
	}
}