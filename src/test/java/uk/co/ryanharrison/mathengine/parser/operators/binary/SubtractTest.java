package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;

class SubtractTest {

    private Subtract subtract= new Subtract();
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
    void subtractTwoDoubles() {
        NodeConstant result = subtract.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 - 45);
    }

    @Test
    void subtractDoubleAndRational() {
        NodeConstant result = subtract.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 - (1/8d));
    }

    @Test
    void subtractDoubleAndVector() {
        NodeConstant result = subtract.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(0), new NodeDouble(23 - 45));
    }

    @Test
    void subtractDoubleAndPercent() {
        NodeConstant result = subtract.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * (1 - 0.25));
    }

    @Test
    void subtractDoubleAndMatrix() {
        NodeConstant result = subtract.toResult(d1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(0), new NodeDouble(23 - 45) },
                new Node[] {new NodeDouble(23 - 45), new NodeDouble(0) });
    }

    ////// rational
    @Test
    void subtractTwoRationals() {
        NodeConstant result = subtract.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(-2/8d);
    }

    @Test
    void subtractRationalAndDouble() {
        NodeConstant result = subtract.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) - 23);
    }

    @Test
    void subtractRationalAndVector() {
        NodeConstant result = subtract.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble((1/8d) - 23), new NodeDouble((1/8d) - 45));
    }

    @Test
    void subtractRationalAndPercent() {
        NodeConstant result = subtract.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) * (1- 0.25));
    }

    @Test
    void subtractRationalAndMatrix() {
        NodeConstant result = subtract.toResult(r1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble((1/8d) - 23), new NodeDouble((1/8d) - 45) },
                new Node[] {new NodeDouble((1/8d) - 45), new NodeDouble((1/8d) - 23) });
    }

    ////// vector
    @Test
    void subtractTwoVectorsSameType() {
        NodeConstant result = subtract.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(0), new NodeDouble(0));
    }

    @Test
    void subtractTwoVectorsDifferentType() {
        NodeConstant result = subtract.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 - (1/8d)), new NodeDouble(45 - (3/8d)));
    }

    @Test
    void subtractVectorAndDouble() {
        NodeConstant result = subtract.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(0), new NodeDouble(45 - 23));
    }

    @Test
    void subtractVectorAndRational() {
        NodeConstant result = subtract.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(0), new NodeRational((2/8d)));
    }

    @Test
    void subtractVectorAndPercent() {
        NodeConstant result = subtract.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d * (1- 0.25))), new NodeRational((3/8d * (1 - 0.25))));
    }

    @Test
    void subtractVectorAndMatrix() {
        NodeConstant result = subtract.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(0), new NodeDouble(0) },
                new Node[] {new NodeDouble(23 - 45), new NodeDouble(45 - 23) });
    }

    ////// percent
    @Test
    void subtractTwoPercentages() {
        NodeConstant result = subtract.toResult(p2, p1);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.25);
    }

    @Test
    void subtractPercentAndDouble() {
        NodeConstant result = subtract.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5 - 45);
    }

    @Test
    void subtractPercentAndRational() {
        NodeConstant result = subtract.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.375);
    }

    @Test
    void subtractPercentAndVector() {
        NodeConstant result = subtract.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d - 0.25)), new NodeRational((3/8d - 0.25)));
    }

    @Test
    void subtractPercentAndMatrix() {
        NodeConstant result = subtract.toResult(p1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d - 0.25), new NodeRational(3/8d - 0.25) },
                new Node[] {new NodeRational(3/8d - 0.25), new NodeRational(1/8d - 0.25) });
    }

    ////// matrix
    @Test
    void subtractTwoMatrixSameType() {
        NodeConstant result = subtract.toResult(m1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(0), new NodeDouble(0) },
                new Node[] {new NodeDouble(0), new NodeDouble(0) });
    }

    @Test
    void subtractTwoMatrixDifferentType() {
        NodeConstant result = subtract.toResult(m1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(23 - 1/8d), new NodeDouble(45 - 3/8d) },
                new Node[] {new NodeDouble(45 - 3/8d), new NodeDouble(23 - 1/8d) });
    }

    @Test
    void subtractMatrixAndDouble() {
        NodeConstant result = subtract.toResult(m1, d1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(0), new NodeDouble(45 - 23) },
                new Node[] {new NodeDouble(45 - 23), new NodeDouble(0) });
    }

    @Test
    void subtractMatrixAndRational() {
        NodeConstant result = subtract.toResult(m2, r1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d - 1/8d), new NodeRational(3/8d - 1/8d) },
                new Node[] {new NodeRational(3/8d - 1/8d), new NodeRational(1/8d - 1/8d) });
    }

    @Test
    void subtractMatrixAndPercent() {
        NodeConstant result = subtract.toResult(m2, p1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeRational(1/8d * (1- 0.25)), new NodeRational(3/8d * (1- 0.25)) },
                new Node[] {new NodeRational(3/8d * (1- 0.25)), new NodeRational(1/8d * (1- 0.25)) });
    }

    @Test
    void subtractMatrixAndVector() {
        NodeConstant result = subtract.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).containsOnly(
                new Node[] {new NodeDouble(0), new NodeDouble(0) },
                new Node[] {new NodeDouble(23 - 45), new NodeDouble(45 - 23) });
    }

}