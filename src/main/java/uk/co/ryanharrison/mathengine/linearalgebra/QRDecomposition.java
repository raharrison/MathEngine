package uk.co.ryanharrison.mathengine.linearalgebra;

import uk.co.ryanharrison.mathengine.utils.MathUtils;

/**
 * Immutable implementation of QR decomposition using Householder reflections.
 * <p>
 * The QR decomposition is a matrix factorization that expresses a matrix <b>A</b> as the product
 * of an orthogonal matrix <b>Q</b> and an upper triangular matrix <b>R</b>. Mathematically:
 * </p>
 * <pre>
 * A = Q * R
 * </pre>
 * <p>
 * where:
 * </p>
 * <ul>
 *     <li><b>Q</b> is an m-by-n orthogonal matrix (Q<sup>T</sup>Q = I)</li>
 *     <li><b>R</b> is an n-by-n upper triangular matrix</li>
 * </ul>
 * <p>
 * For an m-by-n matrix A with m ≥ n:
 * </p>
 * <ul>
 *     <li>Q has dimensions m-by-n (economy-sized)</li>
 *     <li>R has dimensions n-by-n</li>
 *     <li>The columns of Q form an orthonormal basis for the column space of A</li>
 * </ul>
 *
 * <h2>Mathematical Properties:</h2>
 * <ul>
 *     <li>The decomposition always exists, even for rank-deficient matrices</li>
 *     <li>Q is orthogonal: Q<sup>T</sup>Q = I (columns are orthonormal)</li>
 *     <li>R is upper triangular with diagonal elements ≥ 0 (by convention)</li>
 *     <li>The matrix has full rank if and only if all diagonal elements of R are non-zero</li>
 * </ul>
 *
 * <h2>Primary Use Cases:</h2>
 * <ul>
 *     <li>Solving least squares problems (minimizing ||A*X - B||)</li>
 *     <li>Computing orthonormal bases for matrix column spaces</li>
 *     <li>Eigenvalue computations (QR algorithm)</li>
 *     <li>Numerical stability in solving linear systems</li>
 * </ul>
 *
 * <h2>Implementation:</h2>
 * <p>
 * This implementation uses Householder reflections to compute the QR decomposition.
 * Householder reflections are numerically stable transformations that introduce zeros
 * below the diagonal. The decomposition is computed at construction time and all
 * results are cached.
 * </p>
 * <p>
 * The matrix Q is not stored explicitly but is represented through Householder vectors,
 * which allows for efficient computation when needed. The matrix R's diagonal is stored
 * separately for numerical precision.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Create a matrix and decompose it
 * Matrix A = new Matrix(new double[][]{
 *     {12, -51, 4},
 *     {6, 167, -68},
 *     {-4, 24, -41}
 * });
 * QRDecomposition qr = new QRDecomposition(A);
 *
 * // Access decomposition components
 * Matrix Q = qr.getQ();  // Orthogonal matrix
 * Matrix R = qr.getR();  // Upper triangular matrix
 * Matrix H = qr.getH();  // Householder vectors
 *
 * // Solve least squares problem A*X = B
 * Matrix B = new Matrix(new double[][]{{1}, {2}, {3}});
 * if (qr.isFullRank()) {
 *     Matrix X = qr.solve(B);  // Minimizes ||A*X - B||
 * }
 *
 * // Verify decomposition: A should equal Q*R
 * Matrix reconstructed = Q.times(R);
 * }</pre>
 *
 * @author Ryan Harrison
 * @see <a href="http://math.nist.gov/javanumerics/jama/">JAMA: Java Matrix Package</a>
 */
public final class QRDecomposition {

    /**
     * Internal storage of the QR decomposition and Householder vectors.
     * <p>
     * The upper triangular part (above diagonal) contains R, while the lower triangular
     * part (at and below diagonal) contains the essential parts of the Householder vectors
     * used to represent Q. The actual diagonal of R is stored separately in {@link #rDiagonal}.
     * </p>
     */
    private final double[][] qr;

    /**
     * Number of rows in the original matrix.
     */
    private final int rowCount;

    /**
     * Number of columns in the original matrix.
     */
    private final int columnCount;

    /**
     * Diagonal elements of the R matrix.
     * <p>
     * Stored separately for numerical precision. By convention, diagonal elements are
     * non-negative (the sign is adjusted during decomposition).
     * </p>
     */
    private final double[] rDiagonal;

    /**
     * Constructs a QR decomposition of the specified matrix using Householder reflections.
     * <p>
     * The decomposition is computed immediately during construction using Householder
     * transformations. The input matrix is deep-copied and not modified. This algorithm
     * is numerically stable and works for both full-rank and rank-deficient matrices.
     * </p>
     * <p>
     * <b>Time Complexity:</b> O(m*n²) for an m-by-n matrix
     * </p>
     *
     * @param matrix the matrix to decompose (must have m ≥ n, i.e., at least as many rows as columns)
     * @throws IllegalArgumentException if matrix is null
     */
    public QRDecomposition(Matrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }

        // Deep copy the matrix to ensure immutability
        this.qr = matrix.getArrayCopy();
        this.rowCount = matrix.getRowCount();
        this.columnCount = matrix.getColumnCount();
        this.rDiagonal = new double[columnCount];

        // Main loop: compute Householder transformation for each column
        for (int k = 0; k < columnCount; k++) {

            // Compute 2-norm of k-th column (from k-th row onward)
            // Using MathUtils.hypot for numerical stability (avoids overflow/underflow)
            double columnNorm = 0.0;
            for (int i = k; i < rowCount; i++) {
                columnNorm = MathUtils.hypot(columnNorm, qr[i][k]);
            }

            if (columnNorm != 0.0) {
                // Form k-th Householder vector
                // Adjust sign to avoid cancellation errors
                if (qr[k][k] < 0.0) {
                    columnNorm = -columnNorm;
                }

                // Normalize the k-th column by the norm
                for (int i = k; i < rowCount; i++) {
                    qr[i][k] /= columnNorm;
                }
                qr[k][k] += 1.0;

                // Apply Householder transformation to remaining columns
                for (int j = k + 1; j < columnCount; j++) {
                    // Compute projection: s = -2 * (v^T * a) / (v^T * v)
                    // where v is the Householder vector and a is the j-th column
                    double dotProduct = 0.0;
                    for (int i = k; i < rowCount; i++) {
                        dotProduct += qr[i][k] * qr[i][j];
                    }
                    dotProduct = -dotProduct / qr[k][k];

                    // Apply the reflection: a = a + s*v
                    for (int i = k; i < rowCount; i++) {
                        qr[i][j] += dotProduct * qr[i][k];
                    }
                }
            }

            // Store the diagonal element of R (negated by convention)
            rDiagonal[k] = -columnNorm;
        }
    }

    /**
     * Returns the upper triangular factor R.
     * <p>
     * R is an n-by-n upper triangular matrix. By convention, all diagonal elements
     * are non-negative. R contains the same information as the upper part of the
     * original matrix after applying all Householder transformations.
     * </p>
     * <p>
     * <b>Time Complexity:</b> O(n²) to construct the matrix
     * </p>
     *
     * @return a new Matrix containing the upper triangular factor
     */
    public Matrix getR() {
        Matrix rMatrix = new Matrix(columnCount, columnCount);
        double[][] r = rMatrix.getElements();

        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i < j) {
                    // Above diagonal: copy from qr array
                    r[i][j] = qr[i][j];
                } else if (i == j) {
                    // Diagonal: use separately stored values
                    r[i][j] = rDiagonal[i];
                } else {
                    // Below diagonal: zero
                    r[i][j] = 0.0;
                }
            }
        }

        return rMatrix;
    }

    /**
     * Returns the orthogonal factor Q (economy-sized).
     * <p>
     * Q is an m-by-n orthogonal matrix, meaning its columns are orthonormal:
     * Q<sup>T</sup>Q = I. This is the "economy-sized" version where Q has the same
     * number of columns as the original matrix (n), rather than the full m-by-m
     * orthogonal matrix.
     * </p>
     * <p>
     * Q is computed from the Householder vectors stored in the lower part of the
     * qr array. This computation applies the Householder reflections in reverse order.
     * </p>
     * <p>
     * <b>Time Complexity:</b> O(m*n²) to construct the matrix
     * </p>
     *
     * @return a new Matrix containing the orthogonal factor
     */
    public Matrix getQ() {
        Matrix qMatrix = new Matrix(rowCount, columnCount);
        double[][] q = qMatrix.getElements();

        // Generate Q by applying Householder reflections in reverse order
        for (int k = columnCount - 1; k >= 0; k--) {
            // Initialize k-th column to zero
            for (int i = 0; i < rowCount; i++) {
                q[i][k] = 0.0;
            }
            q[k][k] = 1.0;

            // Apply all Householder reflections from k onward
            for (int j = k; j < columnCount; j++) {
                if (qr[k][k] != 0.0) {
                    // Compute projection: s = -2 * (v^T * q_j) / (v^T * v)
                    double dotProduct = 0.0;
                    for (int i = k; i < rowCount; i++) {
                        dotProduct += qr[i][k] * q[i][j];
                    }
                    dotProduct = -dotProduct / qr[k][k];

                    // Apply reflection: q_j = q_j + s*v
                    for (int i = k; i < rowCount; i++) {
                        q[i][j] += dotProduct * qr[i][k];
                    }
                }
            }
        }

        return qMatrix;
    }

    /**
     * Returns the Householder vectors used to represent Q.
     * <p>
     * This returns a lower trapezoidal m-by-n matrix whose columns contain the
     * essential parts of the Householder vectors. These vectors implicitly define
     * the orthogonal matrix Q through a sequence of reflections.
     * </p>
     * <p>
     * This is primarily useful for understanding the internal representation or
     * for advanced applications that want to work directly with Householder vectors.
     * </p>
     * <p>
     * <b>Time Complexity:</b> O(m*n) to construct the matrix
     * </p>
     *
     * @return a new Matrix containing the Householder vectors
     */
    public Matrix getH() {
        Matrix hMatrix = new Matrix(rowCount, columnCount);
        double[][] h = hMatrix.getElements();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i >= j) {
                    // At or below diagonal: copy Householder vector
                    h[i][j] = qr[i][j];
                } else {
                    // Above diagonal: zero
                    h[i][j] = 0.0;
                }
            }
        }

        return hMatrix;
    }

    /**
     * Determines whether the matrix has full rank.
     * <p>
     * A matrix has full rank if all its columns are linearly independent, which
     * occurs if and only if all diagonal elements of R are non-zero. Full rank
     * is required for unique solutions to least squares problems.
     * </p>
     * <p>
     * <b>Note:</b> This method uses exact equality comparison with 0.0. For matrices
     * with very small diagonal elements, numerical precision issues may affect the result.
     * Consider using a small tolerance threshold for practical applications.
     * </p>
     * <p>
     * <b>Time Complexity:</b> O(n) where n is the number of columns
     * </p>
     *
     * @return {@code true} if all diagonal elements of R are non-zero, {@code false} otherwise
     */
    public boolean isFullRank() {
        for (int j = 0; j < columnCount; j++) {
            if (rDiagonal[j] == 0.0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Solves the least squares problem A*X = B using the QR decomposition.
     * <p>
     * Given the matrix A (from which this decomposition was computed) and a right-hand
     * side matrix B, this method computes the matrix X that minimizes the 2-norm
     * ||A*X - B||. For full-rank matrices, this produces the exact solution. For
     * overdetermined systems (more equations than unknowns), it finds the best
     * approximation in the least squares sense.
     * </p>
     * <p>
     * The solution is computed in two steps:
     * </p>
     * <ol>
     *     <li>Compute Y = Q<sup>T</sup>*B by applying Householder reflections</li>
     *     <li>Solve R*X = Y for X using backward substitution</li>
     * </ol>
     * <p>
     * <b>Time Complexity:</b> O(m*n*k + n²*k) where m is the number of rows,
     * n is the number of columns, and k is the number of columns in B
     * </p>
     *
     * @param b the right-hand side matrix with the same number of rows as A and any number of columns
     * @return the solution matrix X that minimizes ||A*X - B||
     * @throws IllegalArgumentException if B has a different number of rows than A
     * @throws RuntimeException if the matrix is rank-deficient (does not have full rank)
     */
    public Matrix solve(Matrix b) {
        if (b.getRowCount() != rowCount) {
            throw new IllegalArgumentException(
                    "Matrix row dimensions must agree. Expected " + rowCount + " rows, got " + b.getRowCount()
            );
        }
        if (!isFullRank()) {
            throw new RuntimeException(
                    "Cannot solve system: matrix is rank deficient (has linearly dependent columns)"
            );
        }

        // Copy right-hand side
        int solutionColumns = b.getColumnCount();
        double[][] x = b.getArrayCopy();

        // Compute Y = Q^T * B by applying Householder transformations
        for (int k = 0; k < columnCount; k++) {
            for (int j = 0; j < solutionColumns; j++) {
                // Compute projection: s = -2 * (v^T * b_j) / (v^T * v)
                double dotProduct = 0.0;
                for (int i = k; i < rowCount; i++) {
                    dotProduct += qr[i][k] * x[i][j];
                }
                dotProduct = -dotProduct / qr[k][k];

                // Apply reflection: b_j = b_j + s*v
                for (int i = k; i < rowCount; i++) {
                    x[i][j] += dotProduct * qr[i][k];
                }
            }
        }

        // Solve R*X = Y using backward substitution
        for (int k = columnCount - 1; k >= 0; k--) {
            for (int j = 0; j < solutionColumns; j++) {
                x[k][j] /= rDiagonal[k];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < solutionColumns; j++) {
                    x[i][j] -= x[k][j] * qr[i][k];
                }
            }
        }

        // Return only the first n rows (the solution)
        return new Matrix(x, columnCount, solutionColumns).getMatrix(0, columnCount - 1, 0, solutionColumns - 1);
    }
}
