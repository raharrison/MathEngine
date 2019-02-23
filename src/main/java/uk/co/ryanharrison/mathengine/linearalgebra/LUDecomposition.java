package uk.co.ryanharrison.mathengine.linearalgebra;

/**
 * Sourced from the <a href="http://math.nist.gov/javanumerics/jama/">JAMA
 * Library</a>.
 * <p>
 * 
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n.
 * <p>
 * The LU decomposition with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 * 
 * @see <a
 *      href="http://math.nist.gov/javanumerics/jama/">http://math.nist.gov/javanumerics/jama/</a>
 * 
 * @author Ryan Harrison
 * 
 * 
 * 
 */
public final class LUDecomposition
{
	/**
	 * Array for internal storage of decomposition.
	 * 
	 * @serial internal array storage.
	 */
	private double[][] LU;

	/**
	 * Row and column dimensions, and pivot sign.
	 * 
	 * @serial column dimension.
	 * @serial row dimension.
	 * @serial pivot sign.
	 */
	private int m, n, pivsign;

	/**
	 * Internal storage of pivot vector.
	 * 
	 * @serial pivot vector.
	 */
	private int[] piv;

	/**
	 * LU Decomposition
	 * 
	 * @param A
	 *            Rectangular matrix
	 * @return Structure to access L, U and piv.
	 */

	public LUDecomposition(Matrix A)
	{

		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.

		LU = A.getArrayCopy();
		m = A.getRowCount();
		n = A.getColumnCount();
		piv = new int[m];
		for (int i = 0; i < m; i++)
		{
			piv[i] = i;
		}
		pivsign = 1;
		double[] LUrowi;
		double[] LUcolj = new double[m];

		// Outer loop.

		for (int j = 0; j < n; j++)
		{

			// Make a copy of the j-th column to localize references.

			for (int i = 0; i < m; i++)
			{
				LUcolj[i] = LU[i][j];
			}

			// Apply previous transformations.

			for (int i = 0; i < m; i++)
			{
				LUrowi = LU[i];

				// Most of the time is spent in the following dot product.

				int kmax = Math.min(i, j);
				double s = 0.0;
				for (int k = 0; k < kmax; k++)
				{
					s += LUrowi[k] * LUcolj[k];
				}

				LUrowi[j] = LUcolj[i] -= s;
			}

			// Find pivot and exchange if necessary.

			int p = j;
			for (int i = j + 1; i < m; i++)
			{
				if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
				{
					p = i;
				}
			}
			if (p != j)
			{
				for (int k = 0; k < n; k++)
				{
					double t = LU[p][k];
					LU[p][k] = LU[j][k];
					LU[j][k] = t;
				}
				int k = piv[p];
				piv[p] = piv[j];
				piv[j] = k;
				pivsign = -pivsign;
			}

			// Compute multipliers.

			if (j < m & LU[j][j] != 0.0)
			{
				for (int i = j + 1; i < m; i++)
				{
					LU[i][j] /= LU[j][j];
				}
			}
		}
	}

	/**
	 * Determinant
	 * 
	 * @return det(A)
	 * @exception IllegalArgumentException
	 *                Matrix must be square
	 */

	public double getDeterminant()
	{
		if (m != n)
		{
			throw new IllegalArgumentException("Matrix must be square.");
		}
		double d = pivsign;
		for (int j = 0; j < n; j++)
		{
			d *= LU[j][j];
		}
		return d;
	}

	/**
	 * Return pivot permutation vector as a one-dimensional double array
	 * 
	 * @return (double) piv
	 */

	public double[] getDoublePivot()
	{
		double[] vals = new double[m];
		for (int i = 0; i < m; i++)
		{
			vals[i] = piv[i];
		}
		return vals;
	}

	/**
	 * Return lower triangular factor
	 * 
	 * @return L
	 */

	public Matrix getL()
	{
		Matrix X = new Matrix(m, n);
		double[][] L = X.getElements();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i > j)
				{
					L[i][j] = LU[i][j];
				}
				else if (i == j)
				{
					L[i][j] = 1.0;
				}
				else
				{
					L[i][j] = 0.0;
				}
			}
		}
		return X;
	}

	/**
	 * Return pivot permutation vector
	 * 
	 * @return piv
	 */

	public int[] getPivot()
	{
		int[] p = new int[m];
		for (int i = 0; i < m; i++)
		{
			p[i] = piv[i];
		}
		return p;
	}

	/**
	 * Return upper triangular factor
	 * 
	 * @return U
	 */

	public Matrix getU()
	{
		Matrix X = new Matrix(n, n);
		double[][] U = X.getElements();
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i <= j)
				{
					U[i][j] = LU[i][j];
				}
				else
				{
					U[i][j] = 0.0;
				}
			}
		}
		return X;
	}

	/**
	 * Is the matrix nonsingular?
	 * 
	 * @return true if U, and hence A, is nonsingular.
	 */

	public boolean isNonsingular()
	{
		for (int j = 0; j < n; j++)
		{
			if (LU[j][j] == 0)
				return false;
		}
		return true;
	}

	/**
	 * Solve A*X = B
	 * 
	 * @param B
	 *            A Matrix with as many rows as A and any number of columns.
	 * @return X so that L*U*X = B(piv,:)
	 * @exception IllegalArgumentException
	 *                Matrix row dimensions must agree.
	 * @exception RuntimeException
	 *                Matrix is singular.
	 */

	public Matrix solve(Matrix B)
	{
		if (B.getRowCount() != m)
		{
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		}
		if (!this.isNonsingular())
		{
			throw new RuntimeException("Matrix is singular.");
		}

		// Copy right hand side with pivoting
		int nx = B.getColumnCount();
		Matrix Xmat = B.getMatrix(piv, 0, nx - 1);
		double[][] X = Xmat.getElements();

		// Solve L*Y = B(piv,:)
		for (int k = 0; k < n; k++)
		{
			for (int i = k + 1; i < n; i++)
			{
				for (int j = 0; j < nx; j++)
				{
					X[i][j] -= X[k][j] * LU[i][k];
				}
			}
		}
		// Solve U*X = Y;
		for (int k = n - 1; k >= 0; k--)
		{
			for (int j = 0; j < nx; j++)
			{
				X[k][j] /= LU[k][k];
			}
			for (int i = 0; i < k; i++)
			{
				for (int j = 0; j < nx; j++)
				{
					X[i][j] -= X[k][j] * LU[i][k];
				}
			}
		}
		return Xmat;
	}
}
