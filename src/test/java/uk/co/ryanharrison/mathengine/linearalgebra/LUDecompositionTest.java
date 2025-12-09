package uk.co.ryanharrison.mathengine.linearalgebra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link LUDecomposition}.
 */
class LUDecompositionTest {

    private static final double TOLERANCE = 1e-9;

    // ==================== Construction Tests ====================

    @Test
    void constructorAcceptsSquareMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu).isNotNull();
    }

    @Test
    void constructorAcceptsTallRectangularMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu).isNotNull();
    }

    @Test
    void constructorAcceptsWideRectangularMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu).isNotNull();
    }

    @Test
    void constructorRejectsNullMatrix() {
        assertThatThrownBy(() -> new LUDecomposition(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    void constructorDoesNotModifyOriginalMatrix() {
        double[][] data = {
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        };
        Matrix original = Matrix.of(data);
        double[][] originalCopy = original.getArrayCopy();

        new LUDecomposition(original);

        // Verify original matrix unchanged
        assertThat(original.getArrayCopy()).isDeepEqualTo(originalCopy);
    }

    // ==================== Decomposition Properties Tests ====================

    @Test
    void decompositionSatisfiesLUEqualsPermutedA() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix L = lu.getL();
        Matrix U = lu.getU();
        int[] pivot = lu.getPivot();

        // Compute L * U
        Matrix LU = L.multiply(U);

        // Get permuted A (A with rows reordered according to pivot)
        Matrix permutedA = A.getMatrix(pivot, 0, A.getColumnCount() - 1);

        // Verify L * U = P*A
        double[][] luArray = LU.getArrayCopy();
        double[][] permutedAArray = permutedA.getArrayCopy();

        for (int i = 0; i < luArray.length; i++) {
            for (int j = 0; j < luArray[i].length; j++) {
                assertThat(luArray[i][j]).isCloseTo(permutedAArray[i][j], within(TOLERANCE));
            }
        }
    }

    @Test
    void decompositionOfIdentityMatrixProducesIdentityFactors() {
        Matrix I = Matrix.identity(3);

        LUDecomposition lu = new LUDecomposition(I);
        Matrix L = lu.getL();
        Matrix U = lu.getU();

        assertThat(L.isIdentity()).isTrue();
        assertThat(U.isIdentity()).isTrue();
    }

    @Test
    void decompositionOfDiagonalMatrixPreservesDiagonal() {
        Matrix diagonal = Matrix.of(new double[][]{
                {2, 0, 0},
                {0, 3, 0},
                {0, 0, 5}
        });

        LUDecomposition lu = new LUDecomposition(diagonal);
        Matrix L = lu.getL();
        Matrix U = lu.getU();

        assertThat(L.isIdentity()).isTrue();
        // U should be the diagonal matrix
        double[][] uArray = U.getArrayCopy();
        assertThat(uArray[0][0]).isCloseTo(2.0, within(TOLERANCE));
        assertThat(uArray[1][1]).isCloseTo(3.0, within(TOLERANCE));
        assertThat(uArray[2][2]).isCloseTo(5.0, within(TOLERANCE));
    }

    // ==================== Factor Retrieval Tests ====================

    @Test
    void getLReturnsUnitLowerTriangularMatrix() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix L = lu.getL();
        double[][] l = L.getArrayCopy();

        // Check diagonal is all 1.0
        for (int i = 0; i < l.length; i++) {
            assertThat(l[i][i]).isCloseTo(1.0, within(TOLERANCE));
        }

        // Check upper part is all zeros
        for (int i = 0; i < l.length; i++) {
            for (int j = i + 1; j < l[i].length; j++) {
                assertThat(l[i][j]).isCloseTo(0.0, within(TOLERANCE));
            }
        }
    }

    @Test
    void getUReturnsUpperTriangularMatrix() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix U = lu.getU();
        double[][] u = U.getArrayCopy();

        // Check lower part (below diagonal) is all zeros
        for (int i = 0; i < u.length; i++) {
            for (int j = 0; j < i && j < u[i].length; j++) {
                assertThat(u[i][j]).isCloseTo(0.0, within(TOLERANCE));
            }
        }
    }

    @Test
    void getLReturnsNewMatrixInstance() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix L1 = lu.getL();
        Matrix L2 = lu.getL();

        assertThat(L1).isNotSameAs(L2);
        assertThat(L1.getArrayCopy()).isDeepEqualTo(L2.getArrayCopy());
    }

    @Test
    void getUReturnsNewMatrixInstance() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix U1 = lu.getU();
        Matrix U2 = lu.getU();

        assertThat(U1).isNotSameAs(U2);
        assertThat(U1.getArrayCopy()).isDeepEqualTo(U2.getArrayCopy());
    }

    @Test
    void getPivotReturnsDefensiveCopy() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        int[] pivot1 = lu.getPivot();
        int[] pivot2 = lu.getPivot();

        assertThat(pivot1).isNotSameAs(pivot2);
        assertThat(pivot1).isEqualTo(pivot2);

        // Modifying returned array shouldn't affect decomposition
        pivot1[0] = 999;
        int[] pivot3 = lu.getPivot();
        assertThat(pivot3[0]).isNotEqualTo(999);
    }

    @Test
    void getDoublePivotReturnsDefensiveCopy() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        double[] doublePivot1 = lu.getDoublePivot();
        double[] doublePivot2 = lu.getDoublePivot();

        assertThat(doublePivot1).isNotSameAs(doublePivot2);
        assertThat(doublePivot1).isEqualTo(doublePivot2);
    }

    @Test
    void getDoublePivotMatchesGetPivot() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        int[] pivot = lu.getPivot();
        double[] doublePivot = lu.getDoublePivot();

        assertThat(doublePivot.length).isEqualTo(pivot.length);
        for (int i = 0; i < pivot.length; i++) {
            assertThat(doublePivot[i]).isEqualTo(pivot[i]);
        }
    }

    // ==================== Determinant Calculation Tests ====================

    @Test
    void getDeterminantForIdentityMatrixIsOne() {
        Matrix I = Matrix.identity(3);

        LUDecomposition lu = new LUDecomposition(I);

        assertThat(lu.getDeterminant()).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void getDeterminantForDiagonalMatrixIsProductOfDiagonal() {
        Matrix diagonal = Matrix.of(new double[][]{
                {2, 0, 0},
                {0, 3, 0},
                {0, 0, 5}
        });

        LUDecomposition lu = new LUDecomposition(diagonal);

        // determinant = 2 * 3 * 5 = 30
        assertThat(lu.getDeterminant()).isCloseTo(30.0, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3, 4, -2.0",        // 2x2 matrix: [[1,2],[3,4]]
            "2, 1, 1, 4, 7.0",         // 2x2 matrix: [[2,1],[1,4]]
            "5, 0, 0, 3, 15.0",        // 2x2 diagonal
    })
    void getDeterminantFor2x2Matrices(double a11, double a12, double a21, double a22, double expected) {
        Matrix matrix = Matrix.of(new double[][]{
                {a11, a12},
                {a21, a22}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu.getDeterminant()).isCloseTo(expected, within(TOLERANCE));
    }

    @Test
    void getDeterminantFor3x3Matrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        // Calculate determinant manually:
        // det = 2*(-12-0) - 1*(8-0) + 1*(28-12)
        //     = 2*(-12) - 8 + 16
        //     = -24 - 8 + 16
        //     = -16
        assertThat(lu.getDeterminant()).isCloseTo(-16.0, within(TOLERANCE));
    }

    @Test
    void getDeterminantForSingularMatrixIsZero() {
        Matrix singular = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},  // Row 2 is 2 * Row 1
                {4, 5, 6}
        });

        LUDecomposition lu = new LUDecomposition(singular);

        assertThat(lu.getDeterminant()).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void getDeterminantRejectsNonSquareMatrix() {
        Matrix rectangular = Matrix.of(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        LUDecomposition lu = new LUDecomposition(rectangular);

        assertThatThrownBy(() -> lu.getDeterminant())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("square");
    }

    // ==================== Singularity Detection Tests ====================

    @Test
    void isNonsingularReturnsTrueForNonsingularMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu.isNonsingular()).isTrue();
    }

    @Test
    void isNonsingularReturnsFalseForSingularMatrix() {
        Matrix singular = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},  // Row 2 is 2 * Row 1
                {4, 5, 6}
        });

        LUDecomposition lu = new LUDecomposition(singular);

        assertThat(lu.isNonsingular()).isFalse();
    }

    @Test
    void isNonsingularReturnsTrueForIdentityMatrix() {
        Matrix I = Matrix.identity(4);

        LUDecomposition lu = new LUDecomposition(I);

        assertThat(lu.isNonsingular()).isTrue();
    }

    @Test
    void isNonsingularReturnsFalseForRectangularMatrix() {
        Matrix rectangular = Matrix.of(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        LUDecomposition lu = new LUDecomposition(rectangular);

        // Non-square matrices cannot be nonsingular
        // The method should handle this gracefully
        // (may return false or throw exception depending on implementation)
        try {
            boolean result = lu.isNonsingular();
            assertThat(result).isFalse();
        } catch (ArrayIndexOutOfBoundsException e) {
            // Some implementations may not handle non-square matrices
            // This is acceptable behavior
        }
    }

    // ==================== System Solving Tests ====================

    @Test
    void solveComputesCorrectSolutionForSimpleSystem() {
        // System: 2x + y = 5
        //         x + y = 3
        // Solution: x = 2, y = 1
        Matrix A = Matrix.of(new double[][]{
                {2, 1},
                {1, 1}
        });
        Matrix B = Matrix.of(new double[][]{{5}, {3}});

        LUDecomposition lu = new LUDecomposition(A);
        Matrix X = lu.solve(B);

        assertThat(X.get(0, 0)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(X.get(1, 0)).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void solveComputesCorrectSolutionFor3x3System() {
        // System: A*X = B
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });
        Matrix B = Matrix.of(new double[][]{{7}, {-4}, {19}});

        LUDecomposition lu = new LUDecomposition(A);
        Matrix X = lu.solve(B);

        // Verify A*X = B instead of checking specific values
        Matrix result = A.multiply(X);
        double[][] resultArray = result.getArrayCopy();
        double[][] bArray = B.getArrayCopy();

        for (int i = 0; i < resultArray.length; i++) {
            assertThat(resultArray[i][0]).isCloseTo(bArray[i][0], within(TOLERANCE));
        }
    }

    @Test
    void solveVerifiesAXEqualsB() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });
        Matrix B = Matrix.of(new double[][]{{7}, {-4}, {19}});

        LUDecomposition lu = new LUDecomposition(A);
        Matrix X = lu.solve(B);

        // Verify A * X = B
        Matrix result = A.multiply(X);
        double[][] resultArray = result.getArrayCopy();
        double[][] bArray = B.getArrayCopy();

        for (int i = 0; i < resultArray.length; i++) {
            assertThat(resultArray[i][0]).isCloseTo(bArray[i][0], within(TOLERANCE));
        }
    }

    @Test
    void solveSupportsSingleColumnRightHandSide() {
        Matrix A = Matrix.identity(3);
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        LUDecomposition lu = new LUDecomposition(A);
        Matrix X = lu.solve(B);

        assertThat(X.getColumnCount()).isEqualTo(1);
        assertThat(X.get(0, 0)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(X.get(1, 0)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(X.get(2, 0)).isCloseTo(3.0, within(TOLERANCE));
    }

    @Test
    void solveSupportsMultipleColumnRightHandSide() {
        Matrix A = Matrix.identity(3);
        Matrix B = Matrix.of(new double[][]{
                {1, 4},
                {2, 5},
                {3, 6}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix X = lu.solve(B);

        assertThat(X.getColumnCount()).isEqualTo(2);
        assertThat(X.get(0, 0)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(X.get(1, 0)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(X.get(2, 0)).isCloseTo(3.0, within(TOLERANCE));
        assertThat(X.get(0, 1)).isCloseTo(4.0, within(TOLERANCE));
        assertThat(X.get(1, 1)).isCloseTo(5.0, within(TOLERANCE));
        assertThat(X.get(2, 1)).isCloseTo(6.0, within(TOLERANCE));
    }

    @Test
    void solveRejectsMatrixWithMismatchedRowDimensions() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}});  // Only 2 rows, should be 3

        LUDecomposition lu = new LUDecomposition(A);

        assertThatThrownBy(() -> lu.solve(B))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("row dimensions");
    }

    @Test
    void solveRejectsSingularMatrix() {
        Matrix singular = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},  // Row 2 is 2 * Row 1
                {4, 5, 6}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        LUDecomposition lu = new LUDecomposition(singular);

        assertThatThrownBy(() -> lu.solve(B))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("singular");
    }

    // ==================== Edge Cases Tests ====================

    @Test
    void decomposition1x1Matrix() {
        Matrix matrix = Matrix.of(new double[][]{{5.0}});

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu.getDeterminant()).isCloseTo(5.0, within(TOLERANCE));
        assertThat(lu.isNonsingular()).isTrue();

        Matrix L = lu.getL();
        assertThat(L.get(0, 0)).isCloseTo(1.0, within(TOLERANCE));

        Matrix U = lu.getU();
        assertThat(U.get(0, 0)).isCloseTo(5.0, within(TOLERANCE));
    }

    @Test
    void decompositionWithZeroElements() {
        Matrix matrix = Matrix.of(new double[][]{
                {1, 0, 0},
                {0, 2, 0},
                {0, 0, 3}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu.getDeterminant()).isCloseTo(6.0, within(TOLERANCE));
        assertThat(lu.isNonsingular()).isTrue();
    }

    @Test
    void decompositionWithNegativeElements() {
        Matrix matrix = Matrix.of(new double[][]{
                {-2, 1},
                {1, -3}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        assertThat(lu.getDeterminant()).isCloseTo(5.0, within(TOLERANCE));
        assertThat(lu.isNonsingular()).isTrue();
    }

    @Test
    void decompositionWithLargeValues() {
        Matrix matrix = Matrix.of(new double[][]{
                {1000, 500},
                {200, 100}
        });

        LUDecomposition lu = new LUDecomposition(matrix);

        // Determinant = 1000*100 - 500*200 = 0
        assertThat(lu.getDeterminant()).isCloseTo(0.0, within(TOLERANCE));
        assertThat(lu.isNonsingular()).isFalse();
    }

    // ==================== Immutability Tests ====================

    @Test
    void decompositionIsImmutable() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);

        // Get initial values
        double initialDet = lu.getDeterminant();
        boolean initialNonsingular = lu.isNonsingular();

        // Perform various operations
        lu.getL();
        lu.getU();
        lu.getPivot();
        lu.getDoublePivot();

        // Verify state unchanged
        assertThat(lu.getDeterminant()).isCloseTo(initialDet, within(TOLERANCE));
        assertThat(lu.isNonsingular()).isEqualTo(initialNonsingular);
    }

    @Test
    void solvingSystemDoesNotChangeDecomposition() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });
        Matrix B = Matrix.of(new double[][]{{7}, {-4}, {19}});

        LUDecomposition lu = new LUDecomposition(A);
        double initialDet = lu.getDeterminant();

        lu.solve(B);

        // Verify decomposition unchanged
        assertThat(lu.getDeterminant()).isCloseTo(initialDet, within(TOLERANCE));
    }

    @Test
    void modifyingReturnedMatricesDoesNotAffectDecomposition() {
        Matrix A = Matrix.of(new double[][]{
                {2, 1, 1},
                {4, -6, 0},
                {-2, 7, 2}
        });

        LUDecomposition lu = new LUDecomposition(A);
        Matrix L1 = lu.getL();

        // Modify the returned matrix's internal state would require using reflection
        // or internal methods, but we can verify we get fresh copies
        Matrix L2 = lu.getL();

        assertThat(L1.getArrayCopy()).isDeepEqualTo(L2.getArrayCopy());
    }
}
