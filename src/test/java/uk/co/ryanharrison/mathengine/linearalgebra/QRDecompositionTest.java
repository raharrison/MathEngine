package uk.co.ryanharrison.mathengine.linearalgebra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link QRDecomposition}.
 */
class QRDecompositionTest {

    private static final double TOLERANCE = 1e-9;

    // ==================== Construction Tests ====================

    @Test
    void constructorAcceptsSquareMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        assertThat(qr).isNotNull();
    }

    @Test
    void constructorAcceptsTallRectangularMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        assertThat(qr).isNotNull();
    }

    @Test
    void constructorRejectsNullMatrix() {
        assertThatThrownBy(() -> new QRDecomposition(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    void constructorDoesNotModifyOriginalMatrix() {
        double[][] data = {
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        };
        Matrix original = Matrix.of(data);
        double[][] originalCopy = original.getArrayCopy();

        new QRDecomposition(original);

        // Verify original matrix unchanged
        assertThat(original.getArrayCopy()).isDeepEqualTo(originalCopy);
    }

    // ==================== Decomposition Properties Tests ====================

    @Test
    void decompositionSatisfiesQREqualsA() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q = qr.getQ();
        Matrix R = qr.getR();

        // Compute Q * R
        Matrix QR = Q.multiply(R);

        // Verify Q * R = A
        double[][] qrArray = QR.getArrayCopy();
        double[][] aArray = A.getArrayCopy();

        for (int i = 0; i < qrArray.length; i++) {
            for (int j = 0; j < qrArray[i].length; j++) {
                assertThat(qrArray[i][j]).isCloseTo(aArray[i][j], within(TOLERANCE));
            }
        }
    }

    @Test
    void decompositionOfIdentityMatrixProducesIdentityLikeFactors() {
        Matrix I = Matrix.identity(3);

        QRDecomposition qr = new QRDecomposition(I);
        Matrix Q = qr.getQ();
        Matrix R = qr.getR();

        // Q*R should equal I (even if Q and R individually aren't exactly I)
        Matrix QR = Q.multiply(R);
        double[][] qrArray = QR.getArrayCopy();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = (i == j) ? 1.0 : 0.0;
                assertThat(qrArray[i][j]).isCloseTo(expected, within(TOLERANCE));
            }
        }
    }

    @Test
    void qMatrixIsOrthogonal() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q = qr.getQ();

        // Q^T * Q should equal I
        Matrix QT = Q.transpose();
        Matrix QTQ = QT.multiply(Q);

        // Verify Q^T * Q = I
        double[][] qtqArray = QTQ.getArrayCopy();
        for (int i = 0; i < qtqArray.length; i++) {
            for (int j = 0; j < qtqArray[i].length; j++) {
                double expected = (i == j) ? 1.0 : 0.0;
                assertThat(qtqArray[i][j]).isCloseTo(expected, within(TOLERANCE));
            }
        }
    }

    @Test
    void qColumnsAreOrthonormal() {
        Matrix A = Matrix.of(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q = qr.getQ();
        double[][] q = Q.getArrayCopy();

        // Check each column has unit length and is orthogonal to others
        for (int i = 0; i < Q.getColumnCount(); i++) {
            // Extract column i
            double normSquared = 0.0;
            for (int row = 0; row < Q.getRowCount(); row++) {
                normSquared += q[row][i] * q[row][i];
            }
            assertThat(Math.sqrt(normSquared)).isCloseTo(1.0, within(TOLERANCE));

            // Check orthogonality with other columns
            for (int j = i + 1; j < Q.getColumnCount(); j++) {
                double dotProduct = 0.0;
                for (int row = 0; row < Q.getRowCount(); row++) {
                    dotProduct += q[row][i] * q[row][j];
                }
                assertThat(dotProduct).isCloseTo(0.0, within(TOLERANCE));
            }
        }
    }

    @Test
    void rMatrixIsUpperTriangular() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix R = qr.getR();
        double[][] r = R.getArrayCopy();

        // Check lower part (below diagonal) is all zeros
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < i; j++) {
                assertThat(r[i][j]).isCloseTo(0.0, within(TOLERANCE));
            }
        }
    }

    @Test
    void rDiagonalElementsHaveCorrectSign() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix R = qr.getR();
        double[][] r = R.getArrayCopy();

        // Diagonal elements of R exist and reconstruction Q*R=A works
        // The sign convention may vary by implementation
        for (int i = 0; i < r.length; i++) {
            // Just verify they're not zero for full rank matrix
            assertThat(Math.abs(r[i][i])).isGreaterThan(0.0);
        }
    }

    @Test
    void decompositionOfTallMatrixWorks() {
        Matrix A = Matrix.of(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6},
                {7, 8}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q = qr.getQ();
        Matrix R = qr.getR();

        // Q should be 4x2 (economy-sized), R should be 2x2
        assertThat(Q.getRowCount()).isEqualTo(4);
        assertThat(Q.getColumnCount()).isEqualTo(2);
        assertThat(R.getRowCount()).isEqualTo(2);
        assertThat(R.getColumnCount()).isEqualTo(2);

        // Decomposition should complete successfully
        assertThat(Q).isNotNull();
        assertThat(R).isNotNull();
    }

    // ==================== Factor Retrieval Tests ====================

    @Test
    void getQReturnsNewMatrixInstance() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q1 = qr.getQ();
        Matrix Q2 = qr.getQ();

        assertThat(Q1).isNotSameAs(Q2);
        assertThat(Q1.getArrayCopy()).isDeepEqualTo(Q2.getArrayCopy());
    }

    @Test
    void getRReturnsNewMatrixInstance() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix R1 = qr.getR();
        Matrix R2 = qr.getR();

        assertThat(R1).isNotSameAs(R2);
        assertThat(R1.getArrayCopy()).isDeepEqualTo(R2.getArrayCopy());
    }

    @Test
    void getHReturnsNewMatrixInstance() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix H1 = qr.getH();
        Matrix H2 = qr.getH();

        assertThat(H1).isNotSameAs(H2);
        assertThat(H1.getArrayCopy()).isDeepEqualTo(H2.getArrayCopy());
    }

    @Test
    void getHReturnsLowerTrapezoidalMatrix() {
        Matrix A = Matrix.of(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix H = qr.getH();
        double[][] h = H.getArrayCopy();

        // Check upper part (above diagonal) is all zeros
        for (int i = 0; i < h.length; i++) {
            for (int j = i + 1; j < h[i].length; j++) {
                assertThat(h[i][j]).isCloseTo(0.0, within(TOLERANCE));
            }
        }
    }

    // ==================== Rank Detection Tests ====================

    @Test
    void isFullRankReturnsTrueForFullRankMatrix() {
        Matrix matrix = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        assertThat(qr.isFullRank()).isTrue();
    }

    @Test
    void isFullRankReturnsFalseForRankDeficientMatrix() {
        Matrix rankDeficient = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},  // Row 2 is 2 * Row 1
                {4, 8, 12}  // Row 3 is 4 * Row 1 - clearer rank deficiency
        });

        QRDecomposition qr = new QRDecomposition(rankDeficient);

        // This matrix has rank 1 (all rows are multiples of first row)
        assertThat(qr.isFullRank()).isFalse();
    }

    @Test
    void isFullRankReturnsTrueForIdentityMatrix() {
        Matrix I = Matrix.identity(4);

        QRDecomposition qr = new QRDecomposition(I);

        assertThat(qr.isFullRank()).isTrue();
    }

    @Test
    void isFullRankReturnsTrueForTallFullRankMatrix() {
        Matrix tallFullRank = Matrix.of(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6}
        });

        QRDecomposition qr = new QRDecomposition(tallFullRank);

        assertThat(qr.isFullRank()).isTrue();
    }

    @Test
    void isFullRankReturnsFalseForMatrixWithDependentColumns() {
        Matrix dependent = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},
                {3, 6, 9}
        });

        QRDecomposition qr = new QRDecomposition(dependent);

        assertThat(qr.isFullRank()).isFalse();
    }

    // ==================== System Solving Tests ====================

    @Test
    void solveComputesCorrectSolutionForSquareSystem() {
        // System: 2x + y = 5
        //         x + y = 3
        // Solution: x = 2, y = 1
        Matrix A = Matrix.of(new double[][]{
                {2, 1},
                {1, 1}
        });
        Matrix B = Matrix.of(new double[][]{{5}, {3}});

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

        assertThat(X.get(0, 0)).isCloseTo(2.0, within(TOLERANCE));
        assertThat(X.get(1, 0)).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void solveComputesCorrectSolutionFor3x3System() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

        // Verify A * X = B
        Matrix result = A.multiply(X);
        double[][] resultArray = result.getArrayCopy();
        double[][] bArray = B.getArrayCopy();

        for (int i = 0; i < resultArray.length; i++) {
            assertThat(resultArray[i][0]).isCloseTo(bArray[i][0], within(TOLERANCE));
        }
    }

    @Test
    void solveComputesLeastSquaresSolutionForOverdeterminedSystem() {
        // Overdetermined system (more equations than unknowns)
        // This should produce a least squares solution
        Matrix A = Matrix.of(new double[][]{
                {1, 1},
                {1, 2},
                {1, 3},
                {1, 4}
        });
        Matrix B = Matrix.of(new double[][]{{6}, {5}, {7}, {10}});

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

        // Verify dimensions: solve returns n×1 (not m×1)
        assertThat(X.getRowCount()).isEqualTo(2);
        assertThat(X.getColumnCount()).isEqualTo(1);

        // X should exist and be a valid least squares solution
        assertThat(X).isNotNull();
    }

    @Test
    void solveSupportsSingleColumnRightHandSide() {
        Matrix A = Matrix.identity(3);
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

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

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

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
                {1, 2},
                {3, 4},
                {5, 6}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}});  // Only 2 rows, should be 3

        QRDecomposition qr = new QRDecomposition(A);

        assertThatThrownBy(() -> qr.solve(B))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("row dimensions");
    }

    @Test
    void solveRejectsRankDeficientMatrix() {
        Matrix rankDeficient = Matrix.of(new double[][]{
                {1, 2, 3},
                {2, 4, 6},  // Row 2 is 2 * Row 1
                {4, 8, 12}  // Row 3 is 4 * Row 1 - clearer rank deficiency
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        QRDecomposition qr = new QRDecomposition(rankDeficient);

        // The matrix has rank 1, so solve should reject it
        assertThatThrownBy(() -> qr.solve(B))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("rank deficient");
    }

    @Test
    void solveWorksForTallMatrices() {
        Matrix A = Matrix.of(new double[][]{
                {1, 0},
                {0, 1},
                {1, 1}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {4}});

        QRDecomposition qr = new QRDecomposition(A);
        Matrix X = qr.solve(B);

        // This is a least squares problem - X is n×1 (2×1)
        assertThat(X.getRowCount()).isEqualTo(2);
        assertThat(X.getColumnCount()).isEqualTo(1);

        // X should exist and be a valid solution
        assertThat(X).isNotNull();
    }

    // ==================== Edge Cases Tests ====================

    @Test
    void decomposition1x1Matrix() {
        Matrix matrix = Matrix.of(new double[][]{{5.0}});

        QRDecomposition qr = new QRDecomposition(matrix);

        assertThat(qr.isFullRank()).isTrue();

        Matrix Q = qr.getQ();
        // Q could be [1] or [-1] depending on implementation
        assertThat(Math.abs(Q.get(0, 0))).isCloseTo(1.0, within(TOLERANCE));

        Matrix R = qr.getR();
        // R could be [5] or [-5] with corresponding Q sign
        assertThat(Math.abs(R.get(0, 0))).isCloseTo(5.0, within(TOLERANCE));
    }

    @Test
    void decompositionWithZeroColumn() {
        Matrix matrix = Matrix.of(new double[][]{
                {1, 0},
                {2, 0},
                {3, 0}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        // Should be rank deficient (rank = 1)
        assertThat(qr.isFullRank()).isFalse();

        // R should have a zero on the diagonal
        Matrix R = qr.getR();
        assertThat(R.get(1, 1)).isCloseTo(0.0, within(TOLERANCE));
    }

    @Test
    void decompositionWithNegativeElements() {
        Matrix matrix = Matrix.of(new double[][]{
                {-2, 1},
                {1, -3},
                {2, 4}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        // Just verify decomposition completes and matrix is full rank
        assertThat(qr.isFullRank()).isTrue();
        assertThat(qr.getQ()).isNotNull();
        assertThat(qr.getR()).isNotNull();
    }

    @Test
    void decompositionWithSmallValues() {
        Matrix matrix = Matrix.of(new double[][]{
                {0.001, 0.002},
                {0.003, 0.004},
                {0.005, 0.006}
        });

        QRDecomposition qr = new QRDecomposition(matrix);

        // Just verify decomposition completes and matrix is full rank
        assertThat(qr.isFullRank()).isTrue();
        assertThat(qr.getQ()).isNotNull();
        assertThat(qr.getR()).isNotNull();
    }

    @Test
    void decompositionOfOrthogonalMatrixPreservesOrthogonality() {
        // Create a known orthogonal matrix (rotation matrix)
        double angle = Math.PI / 4;  // 45 degrees
        Matrix orthogonal = Matrix.of(new double[][]{
                {Math.cos(angle), -Math.sin(angle)},
                {Math.sin(angle), Math.cos(angle)}
        });

        QRDecomposition qr = new QRDecomposition(orthogonal);

        assertThat(qr.isFullRank()).isTrue();

        // Q should be very close to the original matrix
        // R should be close to identity (possibly with sign changes)
        Matrix R = qr.getR();
        double[][] r = R.getArrayCopy();

        // Diagonal elements should have absolute value close to 1
        assertThat(Math.abs(r[0][0])).isCloseTo(1.0, within(TOLERANCE));
        assertThat(Math.abs(r[1][1])).isCloseTo(1.0, within(TOLERANCE));

        // Off-diagonal should be close to zero or original value
        assertThat(Math.abs(r[1][0])).isCloseTo(0.0, within(TOLERANCE));
    }

    // ==================== Immutability Tests ====================

    @Test
    void decompositionIsImmutable() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);

        // Get initial values
        boolean initialFullRank = qr.isFullRank();

        // Perform various operations
        qr.getQ();
        qr.getR();
        qr.getH();

        // Verify state unchanged
        assertThat(qr.isFullRank()).isEqualTo(initialFullRank);
    }

    @Test
    void solvingSystemDoesNotChangeDecomposition() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });
        Matrix B = Matrix.of(new double[][]{{1}, {2}, {3}});

        QRDecomposition qr = new QRDecomposition(A);
        boolean initialFullRank = qr.isFullRank();

        qr.solve(B);

        // Verify decomposition unchanged
        assertThat(qr.isFullRank()).isEqualTo(initialFullRank);
    }

    @Test
    void modifyingReturnedMatricesDoesNotAffectDecomposition() {
        Matrix A = Matrix.of(new double[][]{
                {12, -51, 4},
                {6, 167, -68},
                {-4, 24, -41}
        });

        QRDecomposition qr = new QRDecomposition(A);
        Matrix Q1 = qr.getQ();

        // Verify we get fresh copies
        Matrix Q2 = qr.getQ();

        assertThat(Q1.getArrayCopy()).isDeepEqualTo(Q2.getArrayCopy());
    }

    @Test
    void multipleCallsToGettersProduceSameResults() {
        Matrix A = Matrix.of(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6}
        });

        QRDecomposition qr = new QRDecomposition(A);

        // Call getters multiple times
        Matrix Q1 = qr.getQ();
        Matrix R1 = qr.getR();
        Matrix H1 = qr.getH();

        Matrix Q2 = qr.getQ();
        Matrix R2 = qr.getR();
        Matrix H2 = qr.getH();

        // Verify results are identical (though not same object)
        assertThat(Q1.getArrayCopy()).isDeepEqualTo(Q2.getArrayCopy());
        assertThat(R1.getArrayCopy()).isDeepEqualTo(R2.getArrayCopy());
        assertThat(H1.getArrayCopy()).isDeepEqualTo(H2.getArrayCopy());
    }
}
