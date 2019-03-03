package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;

class DivideTest {

    private Divide divide = new Divide();
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
    void divideTwoDoubles() {
        NodeConstant result = divide.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 / 45d);
    }

    @Test
    void divideDoubleAndRational() {
        NodeConstant result = divide.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 / (1/8d));
    }

    @Test
    void divideDoubleAndVector() {
        NodeConstant result = divide.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(23 / 45d));
    }

    @Test
    void divideDoubleAndPercent() {
        NodeConstant result = divide.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * (1 / 0.25));
    }

    @Test
    void divideDoubleAndMatrix() {
        NodeConstant result = divide.toResult(d1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 / 23d), new NodeDouble(23 / 45d) },
                new Node[] {new NodeDouble(23 / 45d), new NodeDouble(23 / 23d) });
    }

    ////// rational
    @Test
    void divideTwoRationals() {
        NodeConstant result = divide.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / (3/8d));
    }

    @Test
    void divideRationalAndDouble() {
        NodeConstant result = divide.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / 23);
    }

    @Test
    void divideRationalAndVector() {
        NodeConstant result = divide.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble((1/8d) / 23), new NodeDouble((1/8d) / 45));
    }

    @Test
    void divideRationalAndPercent() {
        NodeConstant result = divide.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / 0.25);
    }

    @Test
    void divideRationalAndMatrix() {
        NodeConstant result = divide.toResult(r1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble((1/8d) / 23), new NodeDouble((1/8d) / 45) },
                new Node[] {new NodeDouble((1/8d) / 45), new NodeDouble((1/8d) / 23) });
    }

    ////// vector
    @Test
    void divideTwoVectorsSameType() {
        NodeConstant result = divide.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(1));
    }

    @Test
    void divideTwoVectorsDifferentType() {
        NodeConstant result = divide.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 / (1/8d)), new NodeDouble(45 / (3/8d)));
    }

    @Test
    void divideVectorAndDouble() {
        NodeConstant result = divide.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(45 / 23d));
    }

    @Test
    void divideVectorAndRational() {
        NodeConstant result = divide.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(1), new NodeRational((3/8d) / (1/8d)));
    }

    @Test
    void divideVectorAndPercent() {
        NodeConstant result = divide.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d) / 0.25), new NodeRational((3/8d) / 0.25));
    }

    @Test
    void divideVectorAndMatrix() {
        NodeConstant result = divide.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 / 23d), new NodeDouble(45 / 45d) },
                new Node[] {new NodeDouble(23 / 45d), new NodeDouble(45 / 23d) });
    }

    ////// percent
    @Test
    void divideTwoPercentages() {
        NodeConstant result = divide.toResult(p2, p1);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(2.0);
    }

    @Test
    void dividePercentAndDouble() {
        NodeConstant result = divide.toResult(p2, new NodeDouble(25));

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.02);
    }

    @Test
    void dividePercentAndRational() {
        NodeConstant result = divide.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(4.0);
    }

    @Test
    void dividePercentAndVector() {
        NodeConstant result = divide.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(0.25 / (1/8d)), new NodeRational(0.25 / (3/8d)));
    }

    @Test
    void dividePercentAndMatrix() {
        NodeConstant result = divide.toResult(p1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(0.25 / (1/8d)), new NodeRational(0.25 / (3/8d)) },
                new Node[] {new NodeRational(0.25 / (3/8d)), new NodeRational(0.25 / (1/8d)) });
    }

    ////// matrix
    @Test
    void divideTwoMatrixSameType() {
        NodeConstant result = divide.toResult(m1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23d / 23), new NodeDouble(45 / 45d) },
                new Node[] {new NodeDouble(45 / 45d), new NodeDouble(23d / 23) });
    }

    @Test
    void divideTwoMatrixDifferentType() {
        NodeConstant result = divide.toResult(m1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23d / (1/8d)), new NodeDouble(45d / (3/8d)) },
                new Node[] {new NodeDouble(45d / (3/8d)), new NodeDouble(23d / (1/8d)) });
    }

    @Test
    void divideMatrixAndDouble() {
        NodeConstant result = divide.toResult(m1, d1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 / 23d), new NodeDouble(45 / 23d) },
                new Node[] {new NodeDouble(45 / 23d), new NodeDouble(23 / 23d) });
    }

    @Test
    void divideMatrixAndRational() {
        NodeConstant result = divide.toResult(m2, r1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d / (1/8d)), new NodeRational(3/8d / (1/8d)) },
                new Node[] {new NodeRational(3/8d / (1/8d)), new NodeRational(1/8d / (1/8d)) });
    }

    @Test
    void divideMatrixAndPercent() {
        NodeConstant result = divide.toResult(m2, p1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d / 0.25), new NodeRational(3/8d / 0.25) },
                new Node[] {new NodeRational(3/8d / 0.25), new NodeRational(1/8d / 0.25) });
    }

    @Test
    void divideMatrixAndVector() {
        NodeConstant result = divide.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 / 23d), new NodeDouble(45 / 45d) },
                new Node[] {new NodeDouble(23 / 45d), new NodeDouble(45 / 23d) });
    }

}