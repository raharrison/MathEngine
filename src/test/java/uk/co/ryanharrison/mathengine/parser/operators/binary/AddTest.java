package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

class AddTest {

    private final Add add = new Add();
    private final NodeDouble d1 = new NodeDouble(23);
    private final NodeDouble d2 = new NodeDouble(45);
    private final NodeRational r1 = new NodeRational(1, 8);
    private final NodeRational r2 = new NodeRational(3, 8);
    private final NodeVector v1 = new NodeVector(new Node[]{d1, d2});
    private final NodeVector v2 = new NodeVector(new Node[]{r1, r2});
    private final NodePercent p1 = new NodePercent(25);
    private final NodePercent p2 = new NodePercent(50);
    private final NodeMatrix m1 = new NodeMatrix(new Node[][]{{d1, d2}, {d2, d1}});
    private final NodeMatrix m2 = new NodeMatrix(new Node[][]{{r1, r2}, {r2, r1}});

    /// /// double
    @Test
    void addTwoDoubles() {
        NodeConstant result = add.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + 45);
    }

    @Test
    void addDoubleAndRational() {
        NodeConstant result = add.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + (1 / 8d));
    }

    @Test
    void addDoubleAndVector() {
        NodeConstant result = add.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(23 + 45));
    }

    @Test
    void addDoubleAndPercent() {
        NodeConstant result = add.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * 1.25);
    }

    @Test
    void addDoubleAndMatrix() {
        NodeConstant result = add.toResult(d1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 23), new NodeDouble(23 + 45)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 45), new NodeDouble(23 + 23)}, atIndex(1));
    }

    /// /// rational
    @Test
    void addTwoRationals() {
        NodeConstant result = add.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5);
    }

    @Test
    void addRationalAndDouble() {
        NodeConstant result = add.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + (1 / 8d));
    }

    @Test
    void addRationalAndVector() {
        NodeConstant result = add.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + (1 / 8d)), new NodeDouble(45 + (1 / 8d)));
    }

    @Test
    void addRationalAndPercent() {
        NodeConstant result = add.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1 / 8d) * 1.25);
    }

    @Test
    void addRationalAndMatrix() {
        NodeConstant result = add.toResult(r1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble((1 / 8d) + 23), new NodeDouble((1 / 8d) + 45)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble((1 / 8d) + 45), new NodeDouble((1 / 8d) + 23)}, atIndex(1));
    }

    /// /// vector
    @Test
    void addTwoVectorsSameType() {
        NodeConstant result = add.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(45 + 45));
    }

    @Test
    void addTwoVectorsDifferentType() {
        NodeConstant result = add.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + (1 / 8d)), new NodeDouble(45 + (3 / 8d)));
    }

    @Test
    void addVectorAndDouble() {
        NodeConstant result = add.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(45 + 23));
    }

    @Test
    void addVectorAndRational() {
        NodeConstant result = add.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((2 / 8d)), new NodeRational((4 / 8d)));
    }

    @Test
    void addVectorAndPercent() {
        NodeConstant result = add.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1 / 8d * 1.25)), new NodeRational((3 / 8d * 1.25)));
    }

    @Test
    void addVectorAndMatrix() {
        NodeConstant result = add.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 23), new NodeDouble(45 + 45)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 45), new NodeDouble(45 + 23)}, atIndex(1));
    }

    /// /// percent
    @Test
    void addTwoPercentages() {
        NodeConstant result = add.toResult(p1, p2);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.75);
    }

    @Test
    void addPercentAndDouble() {
        NodeConstant result = add.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(45.50);
    }

    @Test
    void addPercentAndRational() {
        NodeConstant result = add.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.625);
    }

    @Test
    void addPercentAndVector() {
        NodeConstant result = add.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1 / 8d + 0.25)), new NodeRational((3 / 8d + 0.25)));
    }

    @Test
    void addPercentAndMatrix() {
        NodeConstant result = add.toResult(p1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(1 / 8d + 0.25), new NodeRational(3 / 8d + 0.25)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(3 / 8d + 0.25), new NodeRational(1 / 8d + 0.25)}, atIndex(1));
    }

    /// /// matrix
    @Test
    void addTwoMatrixSameType() {
        NodeConstant result = add.toResult(m1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 23), new NodeDouble(45 + 45)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(45 + 45), new NodeDouble(23 + 23)}, atIndex(1));
    }

    @Test
    void addTwoMatrixDifferentType() {
        NodeConstant result = add.toResult(m1, m2);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 1 / 8d), new NodeDouble(45 + 3 / 8d)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(45 + 3 / 8d), new NodeDouble(23 + 1 / 8d)}, atIndex(1));
    }

    @Test
    void addMatrixAndDouble() {
        NodeConstant result = add.toResult(m1, d1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 23), new NodeDouble(45 + 23)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(45 + 23), new NodeDouble(23 + 23)}, atIndex(1));
    }

    @Test
    void addMatrixAndRational() {
        NodeConstant result = add.toResult(m2, r1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(1 / 8d + 1 / 8d), new NodeRational(3 / 8d + 1 / 8d)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(3 / 8d + 1 / 8d), new NodeRational(1 / 8d + 1 / 8d)}, atIndex(1));
    }

    @Test
    void addMatrixAndPercent() {
        NodeConstant result = add.toResult(m2, p1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(1 / 8d * 1.25), new NodeRational(3 / 8d * 1.25)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeRational(3 / 8d * 1.25), new NodeRational(1 / 8d * 1.25)}, atIndex(1));
    }

    @Test
    void addMatrixAndVector() {
        NodeConstant result = add.toResult(v1, m1);

        assertThat(result).isInstanceOf(NodeMatrix.class);
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(23 + 23), new NodeDouble(45 + 45)}, atIndex(0));
        assertThat(((NodeMatrix) result).getValues()).contains(
                new Node[]{new NodeDouble(45 + 23), new NodeDouble(23 + 45)}, atIndex(1));
    }

}