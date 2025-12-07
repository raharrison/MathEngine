package uk.co.ryanharrison.mathengine.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link LinearRegressionModel}.
 * <p>
 * Tests cover construction, validation, statistical properties, regression accuracy,
 * edge cases, immutability, and object methods (equals, hashCode, toString).
 * </p>
 */
class LinearRegressionModelTest {

    // Test tolerances
    private static final double STRICT_TOLERANCE = 1e-9;    // For exact linear relationships
    private static final double RELAXED_TOLERANCE = 1e-6;   // For noisy data

    // ==================== Construction - Factory Methods ====================

    @Test
    void ofCreatesModelWithValidData() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 6.0, 8.0, 10.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model).isNotNull();
        assertThat(model.getIntercept()).isCloseTo(0.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void ofCreatesModelWithTwoDataPoints() {
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 5.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model).isNotNull();
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
        assertThat(model.getIntercept()).isCloseTo(1.0, within(STRICT_TOLERANCE));
    }

    // ==================== Construction - Builder Pattern ====================

    @Test
    void builderCreatesModelSuccessfully() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.builder()
                .xValues(x)
                .yValues(y)
                .build();

        assertThat(model).isNotNull();
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void builderAllowsMethodChaining() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel.Builder builder = LinearRegressionModel.builder();

        assertThat(builder.xValues(x)).isSameAs(builder);
        assertThat(builder.yValues(y)).isSameAs(builder);
    }

    @Test
    void builderThrowsWhenXValuesNotSet() {
        double[] y = {2.0, 4.0, 6.0};

        assertThatThrownBy(() ->
                LinearRegressionModel.builder()
                        .yValues(y)
                        .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("X values must be set");
    }

    @Test
    void builderThrowsWhenYValuesNotSet() {
        double[] x = {1.0, 2.0, 3.0};

        assertThatThrownBy(() ->
                LinearRegressionModel.builder()
                        .xValues(x)
                        .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Y values must be set");
    }

    @Test
    void builderThrowsWhenNeitherValueSet() {
        assertThatThrownBy(() -> LinearRegressionModel.builder().build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("X values must be set");
    }

    // ==================== Validation - Null Inputs ====================

    @Test
    void ofRejectsNullXValues() {
        double[] y = {1.0, 2.0, 3.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(null, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X values cannot be null");
    }

    @Test
    void ofRejectsNullYValues() {
        double[] x = {1.0, 2.0, 3.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Y values cannot be null");
    }

    @Test
    void ofRejectsBothNull() {
        assertThatThrownBy(() -> LinearRegressionModel.of(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void builderRejectsNullXValues() {
        assertThatThrownBy(() ->
                LinearRegressionModel.builder().xValues(null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X values cannot be null");
    }

    @Test
    void builderRejectsNullYValues() {
        assertThatThrownBy(() ->
                LinearRegressionModel.builder().yValues(null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Y values cannot be null");
    }

    // ==================== Validation - Empty Arrays ====================

    @Test
    void ofRejectsEmptyXArray() {
        double[] x = {};
        double[] y = {};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient x data points");
    }

    @Test
    void ofRejectsEmptyYArray() {
        double[] x = {};
        double[] y = {};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient");
    }

    // ==================== Validation - Single Data Point ====================

    @Test
    void ofRejectsSingleDataPoint() {
        double[] x = {1.0};
        double[] y = {2.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient")
                .hasMessageContaining("Required: 2");
    }

    // ==================== Validation - Length Mismatch ====================

    @ParameterizedTest
    @CsvSource({
            "2, 3",  // x shorter
            "5, 3",  // x longer
            "2, 10", // significantly different
    })
    void ofRejectsLengthMismatch(int xLength, int yLength) {
        double[] x = new double[xLength];
        double[] y = new double[yLength];

        for (int i = 0; i < xLength; i++) x[i] = i;
        for (int i = 0; i < yLength; i++) y[i] = i;

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("same length")
                .hasMessageContaining("X length: " + xLength)
                .hasMessageContaining("Y length: " + yLength);
    }

    // ==================== Validation - Non-Finite Values ====================

    @Test
    void ofRejectsNaNInXValues() {
        double[] x = {1.0, Double.NaN, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X value at index 1 is not finite");
    }

    @Test
    void ofRejectsNaNInYValues() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, Double.NaN, 6.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Y value at index 1 is not finite");
    }

    @Test
    void ofRejectsPositiveInfinityInXValues() {
        double[] x = {1.0, 2.0, Double.POSITIVE_INFINITY};
        double[] y = {2.0, 4.0, 6.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X value at index 2 is not finite");
    }

    @Test
    void ofRejectsNegativeInfinityInYValues() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, Double.NEGATIVE_INFINITY};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Y value at index 2 is not finite");
    }

    // ==================== Validation - Zero Variance in X ====================

    @Test
    void ofRejectsIdenticalXValues() {
        double[] x = {5.0, 5.0, 5.0, 5.0};
        double[] y = {1.0, 2.0, 3.0, 4.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("All x values are identical")
                .hasMessageContaining("variance = 0");
    }

    @Test
    void ofRejectsAllZeroXValues() {
        double[] x = {0.0, 0.0, 0.0};
        double[] y = {1.0, 2.0, 3.0};

        assertThatThrownBy(() -> LinearRegressionModel.of(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("variance = 0");
    }

    // ==================== Properties - Getters ====================

    @Test
    void getInterceptReturnsCorrectValue() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(3.0, within(STRICT_TOLERANCE));
    }

    @Test
    void getSlopeReturnsCorrectValue() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void getCoefficientsReturnsInterceptAndSlope() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double[] coeffs = model.getCoefficients();

        assertThat(coeffs).hasSize(2);
        assertThat(coeffs[0]).isCloseTo(3.0, within(STRICT_TOLERANCE));  // intercept
        assertThat(coeffs[1]).isCloseTo(2.0, within(STRICT_TOLERANCE));  // slope
    }

    @Test
    void getXValuesReturnsDefensiveCopy() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double[] returnedX = model.getXValues();

        assertThat(returnedX).isEqualTo(x);
        assertThat(returnedX).isNotSameAs(x);
    }

    @Test
    void getYValuesReturnsDefensiveCopy() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double[] returnedY = model.getYValues();

        assertThat(returnedY).isEqualTo(y);
        assertThat(returnedY).isNotSameAs(y);
    }

    @Test
    void getCoefficientsReturnsDefensiveCopy() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double[] coeffs1 = model.getCoefficients();
        double[] coeffs2 = model.getCoefficients();

        assertThat(coeffs1).isEqualTo(coeffs2);
        assertThat(coeffs1).isNotSameAs(coeffs2);
    }

    // ==================== Regression Accuracy - Perfect Lines ====================

    @Test
    void fitsLineThroughOriginPerfectly() {
        // y = 0.5x (perfect line through origin)
        double[] x = {0.0, 2.0, 4.0, 6.0, 8.0};
        double[] y = {0.0, 1.0, 2.0, 3.0, 4.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(0.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(0.5, within(STRICT_TOLERANCE));
    }

    @Test
    void fitsPositiveSlopeLinePerfectly() {
        // y = 2x + 3 (perfect line)
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {5.0, 7.0, 9.0, 11.0, 13.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(3.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void fitsNegativeSlopeLinePerfectly() {
        // y = -2x + 10 (perfect line with negative slope)
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {8.0, 6.0, 4.0, 2.0, 0.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(10.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(-2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void fitsHorizontalLinePerfectly() {
        // y = 5 (horizontal line, slope = 0)
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {5.0, 5.0, 5.0, 5.0, 5.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(5.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(0.0, within(STRICT_TOLERANCE));
    }

    @Test
    void fitsLineWithLargeSlope() {
        // y = 100x - 50 (steep line)
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {50.0, 150.0, 250.0, 350.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(-50.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(100.0, within(STRICT_TOLERANCE));
    }

    @Test
    void fitsLineWithSmallSlope() {
        // y = 0.001x + 1000 (nearly horizontal)
        double[] x = {0.0, 1000.0, 2000.0, 3000.0};
        double[] y = {1000.0, 1001.0, 1002.0, 1003.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(1000.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(0.001, within(STRICT_TOLERANCE));
    }

    // ==================== Regression Accuracy - Sample Data from Original Test ====================

    @Test
    void fitsOriginalTestDataCorrectly() {
        // Original test data from RegressionModelTest
        double[] x = {2, 3, 4, 5, 6, 8, 10, 11};
        double[] y = {21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Expected values from original test
        assertThat(model.getIntercept()).isCloseTo(9.476277128547583, within(RELAXED_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(4.193873121869783, within(RELAXED_TOLERANCE));
    }

    @Test
    void evaluatesOriginalTestDataCorrectly() {
        double[] x = {2, 3, 4, 5, 6, 8, 10, 11};
        double[] y = {21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Original test evaluation point
        double result = model.evaluateAt(12.74);
        assertThat(result).isCloseTo(62.90622070116862, within(RELAXED_TOLERANCE));
    }

    // ==================== Regression Accuracy - Noisy Data ====================

    @Test
    void fitsNoisyDataReasonably() {
        // y ≈ 2x + 1 with noise
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {3.1, 4.9, 7.2, 8.8, 11.1};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Should be close to y = 2x + 1
        assertThat(model.getSlope()).isCloseTo(2.0, within(0.1));
        assertThat(model.getIntercept()).isCloseTo(1.0, within(0.5));
    }

    // ==================== Statistical Properties - Line Passes Through Centroid ====================

    @Test
    void regressionLinePassesThroughCentroid() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 3.9, 6.2, 8.1, 9.9};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Calculate centroid (mean of x, mean of y)
        double xMean = 3.0;  // (1+2+3+4+5)/5
        double yMean = 6.04; // (2.1+3.9+6.2+8.1+9.9)/5

        // Verify the regression line passes through the centroid
        double predictedY = model.evaluateAt(xMean);
        assertThat(predictedY).isCloseTo(yMean, within(STRICT_TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 2.0, 3.0, 4.0, 5.0",     // Simple sequence
            "0.0, 1.0, 2.0, 3.0, 4.0",     // Starting from zero
            "-2.0, -1.0, 0.0, 1.0, 2.0",   // Negative values
            "10.0, 20.0, 30.0, 40.0, 50.0" // Larger values
    })
    void regressionLineAlwaysPassesThroughCentroidOfX(double x1, double x2, double x3, double x4, double x5) {
        double[] x = {x1, x2, x3, x4, x5};
        double[] y = {2.0, 4.0, 6.0, 8.0, 10.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        double xMean = (x1 + x2 + x3 + x4 + x5) / 5.0;
        double yMean = 6.0; // mean of y values

        double predictedY = model.evaluateAt(xMean);
        assertThat(predictedY).isCloseTo(yMean, within(STRICT_TOLERANCE));
    }

    // ==================== Evaluation ====================

    @Test
    void evaluateAtComputesCorrectValue() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.evaluateAt(0.0)).isCloseTo(3.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(1.0)).isCloseTo(5.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(2.0)).isCloseTo(7.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(10.0)).isCloseTo(23.0, within(STRICT_TOLERANCE));
    }

    @Test
    void evaluateAtWorksForNegativeX() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.evaluateAt(-1.0)).isCloseTo(1.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(-10.0)).isCloseTo(-17.0, within(STRICT_TOLERANCE));
    }

    @Test
    void evaluateAtWorksForFractionalX() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.evaluateAt(1.5)).isCloseTo(6.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(2.75)).isCloseTo(8.5, within(STRICT_TOLERANCE));
    }

    @Test
    void evaluateAtAllowsExtrapolation() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Extrapolate beyond data range
        assertThat(model.evaluateAt(100.0)).isCloseTo(203.0, within(STRICT_TOLERANCE));
        assertThat(model.evaluateAt(-100.0)).isCloseTo(-197.0, within(STRICT_TOLERANCE));
    }

    @Test
    void evaluateAtRejectsNaN() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThatThrownBy(() -> model.evaluateAt(Double.NaN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X value must be finite");
    }

    @Test
    void evaluateAtRejectsPositiveInfinity() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThatThrownBy(() -> model.evaluateAt(Double.POSITIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X value must be finite");
    }

    @Test
    void evaluateAtRejectsNegativeInfinity() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThatThrownBy(() -> model.evaluateAt(Double.NEGATIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("X value must be finite");
    }

    // ==================== Edge Cases ====================

    @Test
    void handlesNegativeXAndYValues() {
        double[] x = {-5.0, -3.0, -1.0, 1.0, 3.0};
        double[] y = {-8.0, -4.0, 0.0, 4.0, 8.0};  // y = 2x + 2

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(2.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    @Test
    void handlesVeryLargeValues() {
        double[] x = {1e10, 2e10, 3e10};
        double[] y = {1e10, 2e10, 3e10};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getSlope()).isCloseTo(1.0, within(RELAXED_TOLERANCE));
        assertThat(model.getIntercept()).isCloseTo(0.0, within(1e4)); // Relaxed for large numbers
    }

    @Test
    void handlesVerySmallValues() {
        double[] x = {1e-10, 2e-10, 3e-10};
        double[] y = {2e-10, 4e-10, 6e-10};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
        assertThat(model.getIntercept()).isCloseTo(0.0, within(1e-20));
    }

    @Test
    void handlesMixedPositiveAndNegativeSlope() {
        double[] x = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] y = {10.0, 7.0, 4.0, 1.0, -2.0};  // y = -3x + 4

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(4.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(-3.0, within(STRICT_TOLERANCE));
    }

    @Test
    void handlesDataWithAllNegativeY() {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {-2.0, -4.0, -6.0, -8.0};  // y = -2x

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getSlope()).isCloseTo(-2.0, within(STRICT_TOLERANCE));
        assertThat(model.getIntercept()).isCloseTo(0.0, within(STRICT_TOLERANCE));
    }

    @Test
    void handlesLargeDataset() {
        double[] x = new double[1000];
        double[] y = new double[1000];

        for (int i = 0; i < 1000; i++) {
            x[i] = i;
            y[i] = 2 * i + 5;  // y = 2x + 5
        }

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model.getIntercept()).isCloseTo(5.0, within(STRICT_TOLERANCE));
        assertThat(model.getSlope()).isCloseTo(2.0, within(STRICT_TOLERANCE));
    }

    // ==================== Equality and HashCode ====================

    @Test
    void equalsReturnsTrueForIdenticalModels() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model1 = LinearRegressionModel.of(x, y);
        LinearRegressionModel model2 = LinearRegressionModel.of(x, y);

        assertThat(model1).isEqualTo(model2);
    }

    @Test
    void equalsReturnsTrueForSameInstance() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model).isEqualTo(model);
    }

    @Test
    void equalsReturnsFalseForDifferentXValues() {
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x2 = {1.0, 2.0, 4.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model1 = LinearRegressionModel.of(x1, y);
        LinearRegressionModel model2 = LinearRegressionModel.of(x2, y);

        assertThat(model1).isNotEqualTo(model2);
    }

    @Test
    void equalsReturnsFalseForDifferentYValues() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y1 = {2.0, 4.0, 6.0};
        double[] y2 = {2.0, 4.0, 7.0};

        LinearRegressionModel model1 = LinearRegressionModel.of(x, y1);
        LinearRegressionModel model2 = LinearRegressionModel.of(x, y2);

        assertThat(model1).isNotEqualTo(model2);
    }

    @Test
    void equalsReturnsFalseForNull() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model).isNotEqualTo(null);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        assertThat(model).isNotEqualTo("not a model");
    }

    @Test
    void hashCodeIsConsistent() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        int hash1 = model.hashCode();
        int hash2 = model.hashCode();

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void hashCodeIsEqualForEqualModels() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model1 = LinearRegressionModel.of(x, y);
        LinearRegressionModel model2 = LinearRegressionModel.of(x, y);

        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
    }

    @Test
    void hashCodeIsDifferentForDifferentModels() {
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x2 = {1.0, 2.0, 4.0};
        double[] y = {2.0, 4.0, 6.0};

        LinearRegressionModel model1 = LinearRegressionModel.of(x1, y);
        LinearRegressionModel model2 = LinearRegressionModel.of(x2, y);

        // Note: Different objects might have same hash (collision), but this is unlikely
        assertThat(model1.hashCode()).isNotEqualTo(model2.hashCode());
    }

    // ==================== toString ====================

    @Test
    void toStringContainsIntercept() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        String str = model.toString();

        assertThat(str)
                .contains("3.0000")
                .contains("LinearRegressionModel");
    }

    @Test
    void toStringContainsSlope() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};  // y = 2x + 3

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        String str = model.toString();

        assertThat(str)
                .contains("2.0000")
                .contains("LinearRegressionModel");
    }

    @Test
    void toStringContainsDataPointCount() {
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.0, 4.0, 6.0, 8.0, 10.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        String str = model.toString();

        assertThat(str)
                .contains("n=5")
                .contains("LinearRegressionModel");
    }

    @Test
    void toStringHasCorrectFormat() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        String str = model.toString();

        // Format: LinearRegressionModel(y = a + b·x, n=count)
        assertThat(str).matches("LinearRegressionModel\\(y = [\\d\\.-]+ \\+ [\\d\\.-]+·x, n=\\d+\\)");
    }

    // ==================== Immutability ====================

    @Test
    void modifyingInputXArrayDoesNotAffectModel() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double originalIntercept = model.getIntercept();
        double originalSlope = model.getSlope();

        // Modify the original input array
        x[0] = 999.0;
        x[1] = 999.0;
        x[2] = 999.0;

        // Model should be unchanged
        assertThat(model.getIntercept()).isEqualTo(originalIntercept);
        assertThat(model.getSlope()).isEqualTo(originalSlope);
        assertThat(model.getXValues()).containsExactly(1.0, 2.0, 3.0);
    }

    @Test
    void modifyingInputYArrayDoesNotAffectModel() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double originalIntercept = model.getIntercept();
        double originalSlope = model.getSlope();

        // Modify the original input array
        y[0] = 999.0;
        y[1] = 999.0;
        y[2] = 999.0;

        // Model should be unchanged
        assertThat(model.getIntercept()).isEqualTo(originalIntercept);
        assertThat(model.getSlope()).isEqualTo(originalSlope);
        assertThat(model.getYValues()).containsExactly(5.0, 7.0, 9.0);
    }

    @Test
    void modifyingReturnedXArrayDoesNotAffectModel() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Get X values and modify them
        double[] returnedX = model.getXValues();
        returnedX[0] = 999.0;
        returnedX[1] = 999.0;
        returnedX[2] = 999.0;

        // Model should be unchanged
        assertThat(model.getXValues()).containsExactly(1.0, 2.0, 3.0);
    }

    @Test
    void modifyingReturnedYArrayDoesNotAffectModel() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        // Get Y values and modify them
        double[] returnedY = model.getYValues();
        returnedY[0] = 999.0;
        returnedY[1] = 999.0;
        returnedY[2] = 999.0;

        // Model should be unchanged
        assertThat(model.getYValues()).containsExactly(5.0, 7.0, 9.0);
    }

    @Test
    void modifyingReturnedCoefficientsDoesNotAffectModel() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);
        double originalIntercept = model.getIntercept();
        double originalSlope = model.getSlope();

        // Get coefficients and modify them
        double[] coeffs = model.getCoefficients();
        coeffs[0] = 999.0;
        coeffs[1] = 999.0;

        // Model should be unchanged
        assertThat(model.getIntercept()).isEqualTo(originalIntercept);
        assertThat(model.getSlope()).isEqualTo(originalSlope);
    }

    @Test
    void builderDefensivelyCopiesXArray() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel.Builder builder = LinearRegressionModel.builder()
                .xValues(x)
                .yValues(y);

        // Modify x after passing to builder
        x[0] = 999.0;

        LinearRegressionModel model = builder.build();

        // Model should have original values
        assertThat(model.getXValues()).containsExactly(1.0, 2.0, 3.0);
    }

    @Test
    void builderDefensivelyCopiesYArray() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel.Builder builder = LinearRegressionModel.builder()
                .xValues(x)
                .yValues(y);

        // Modify y after passing to builder
        y[0] = 999.0;

        LinearRegressionModel model = builder.build();

        // Model should have original values
        assertThat(model.getYValues()).containsExactly(5.0, 7.0, 9.0);
    }

    @Test
    void callingMethodsDoesNotChangeState() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {5.0, 7.0, 9.0};

        LinearRegressionModel model = LinearRegressionModel.of(x, y);

        double originalIntercept = model.getIntercept();
        double originalSlope = model.getSlope();

        // Call methods multiple times
        model.getIntercept();
        model.getSlope();
        model.getCoefficients();
        model.getXValues();
        model.getYValues();
        model.evaluateAt(5.0);
        model.evaluateAt(10.0);
        model.toString();
        model.hashCode();

        // Verify state unchanged
        assertThat(model.getIntercept()).isEqualTo(originalIntercept);
        assertThat(model.getSlope()).isEqualTo(originalSlope);
    }
}
