package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;

class MultiplyTest {

    private Multiply multiply = new Multiply();
    private NodeDouble d1 = new NodeDouble(23);
    private NodeDouble d2 = new NodeDouble(45);
    private NodeRational r1 = new NodeRational(1, 8);
    private NodeRational r2 = new NodeRational(3, 8);
    private NodeVector v1 = new NodeVector(new Node[] { d1, d2 });
    private NodeVector v2 = new NodeVector(new Node[] { r1, r2 });
    private NodePercent p1 = new NodePercent(25);
    private NodePercent p2 = new NodePercent(50);
    private NodeMatrix m1 = new NodeMatrix(new Node[][] { { d1, d2 }, { d2, d1 } });
    private NodeMatrix m2 = new NodeMatrix(new Node[][] { { r1, r2 }, { r2, r1 } });

    ////// double
    @Test
    void multiplyTwoDoubles() {
        NodeConstant result = multiply.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * 45d);
    }

    @Test
    void multiplyDoubleAndRational() {
        NodeConstant result = multiply.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * (1/8d));
    }

    @Test
    void multiplyDoubleAndVector() {
        NodeConstant result = multiply.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 * 23d), new NodeDouble(23 * 45d));
    }

    @Test
    void multiplyDoubleAndPercent() {
        NodeConstant result = multiply.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * (1 * 0.25));
    }

    @Test
    void multiplyDoubleAndMatrix() {
        NodeConstant result = multiply.toResult(d1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 * 23), new NodeDouble(23 * 45) },
                new Node[] {new NodeDouble(23 * 45), new NodeDouble(23 * 23) });
    }

    ////// rational
    @Test
    void multiplyTwoRationals() {
        NodeConstant result = multiply.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) * (3/8d));
    }

    @Test
    void multiplyRationalAndDouble() {
        NodeConstant result = multiply.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) * 23);
    }

    @Test
    void multiplyRationalAndVector() {
        NodeConstant result = multiply.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble((1/8d) * 23), new NodeDouble((1/8d) * 45));
    }

    @Test
    void multiplyRationalAndPercent() {
        NodeConstant result = multiply.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) * 0.25);
    }

    @Test
    void multiplyRationalAndMatrix() {
        NodeConstant result = multiply.toResult(r1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble((1/8d) * 23), new NodeDouble((1/8d) * 45) },
                new Node[] {new NodeDouble((1/8d) * 45), new NodeDouble((1/8d) * 23) });
    }

    ////// vector
    @Test
    void multiplyTwoVectorsSameType() {
        NodeConstant result = multiply.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 * 23), new NodeDouble(45 * 45));
    }

    @Test
    void multiplyTwoVectorsDifferentType() {
        NodeConstant result = multiply.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 * (1/8d)), new NodeDouble(45 * (3/8d)));
    }

    @Test
    void multiplyVectorAndDouble() {
        NodeConstant result = multiply.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 * 23d), new NodeDouble(45 * 23d));
    }

    @Test
    void multiplyVectorAndRational() {
        NodeConstant result = multiply.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d) * (1/8d)), new NodeRational((3/8d) * (1/8d)));
    }

    @Test
    void multiplyVectorAndPercent() {
        NodeConstant result = multiply.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d) * 0.25), new NodeRational((3/8d) * 0.25));
    }

    @Test
    void multiplyVectorAndMatrix() {
        NodeConstant result = multiply.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 * 23), new NodeDouble(45 * 45) },
                new Node[] {new NodeDouble(23 * 45), new NodeDouble(45 * 23) });
    }

    ////// percent
    @Test
    void multiplyTwoPercentages() {
        NodeConstant result = multiply.toResult(p2, p1);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5 * 0.25);
    }

    @Test
    void multiplyPercentAndDouble() {
        NodeConstant result = multiply.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5 * 45);
    }

    @Test
    void multiplyPercentAndRational() {
        NodeConstant result = multiply.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5 * (1/8d));
    }

    @Test
    void multiplyPercentAndVector() {
        NodeConstant result = multiply.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(0.25 * (1/8d)), new NodeRational(0.25 * (3/8d)));
    }

    @Test
    void multiplyPercentAndMatrix() {
        NodeConstant result = multiply.toResult(p1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d * 0.25), new NodeRational(3/8d * 0.25) },
                new Node[] {new NodeRational(3/8d * 0.25), new NodeRational(1/8d * 0.25) });
    }

    ////// matrix
    @Test
    void multiplyTwoMatrixSameType() {
        NodeMatrix a = new NodeMatrix(new Matrix(new double[][] {
                { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } }));
        NodeMatrix b = new NodeMatrix(new Matrix(new double[][] {
                { -5, 7, 3 }, { -2, 1, 3 }, { 9, 4.5, 2 } }));

        NodeConstant result = multiply.toResult(a, b);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(18), new NodeDouble(22.5), new NodeDouble(15) },
                new Node[] {new NodeDouble(24), new NodeDouble(60), new NodeDouble(39) },
                new Node[] {new NodeDouble(30), new NodeDouble(97.5), new NodeDouble(63) });
    }

    @Test
    void multiplyTwoMatrixDifferentType() {
        NodeConstant result = multiply.toResult(m1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(79/4d), new NodeDouble(57/4d) },
                new Node[] {new NodeDouble(57/4d), new NodeDouble(79/4d) });
    }

    @Test
    void multiplyMatrixAndDouble() {
        NodeConstant result = multiply.toResult(m1, d1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 * 23), new NodeDouble(45 * 23) },
                new Node[] {new NodeDouble(45 * 23), new NodeDouble(23 * 23) });
    }

    @Test
    void multiplyMatrixAndRational() {
        NodeConstant result = multiply.toResult(m2, r1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d * 1/8d), new NodeRational(3/8d * 1/8d) },
                new Node[] {new NodeRational(3/8d * 1/8d), new NodeRational(1/8d * 1/8d) });
    }

    @Test
    void multiplyMatrixAndPercent() {
        NodeConstant result = multiply.toResult(m2, p1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d * 0.25), new NodeRational(3/8d * 0.25) },
                new Node[] {new NodeRational(3/8d * 0.25), new NodeRational(1/8d * 0.25) });
    }

    @Test
    void multiplyMatrixAndVector() {
        NodeConstant result = multiply.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 * 23), new NodeDouble(45 * 45) },
                new Node[] {new NodeDouble(45 * 23), new NodeDouble(23 * 45) });
    }

}