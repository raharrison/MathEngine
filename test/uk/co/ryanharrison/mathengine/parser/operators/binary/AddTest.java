package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AddTest {

    private Add add = new Add();
    private NodeDouble d1 = new NodeDouble(23);
    private NodeDouble d2 = new NodeDouble(45);
    private NodeRational r1 = new NodeRational(1, 8);
    private NodeRational r2 = new NodeRational(3, 8);
    private NodeVector v1 = new NodeVector(new Node[] { d1, d2 });
    private NodeVector v2 = new NodeVector(new Node[] { r1, r2 });
    private NodePercent p1 = new NodePercent(25);
    private NodePercent p2 = new NodePercent(50);

    ////// double
    @Test
    public void addTwoDoubles() {
        NodeConstant result = add.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + 45);
    }

    @Test
    public void addDoubleAndRational() {
        NodeConstant result = add.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + (1/8d));
    }

    @Test
    public void addDoubleAndVector() {
        NodeConstant result = add.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(23 + 45));
    }

    @Test
    public void addDoubleAndPercent() {
        NodeConstant result = add.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * 1.25);
    }

    ////// rational
    @Test
    public void addTwoRationals() {
        NodeConstant result = add.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5);
    }

    @Test
    public void addRationalAndDouble() {
        NodeConstant result = add.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 + (1/8d));
    }

    @Test
    public void addRationalAndVector() {
        NodeConstant result = add.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + (1/8d)), new NodeDouble(45 + (1/8d)));
    }

    @Test
    public void addRationalAndPercent() {
        NodeConstant result = add.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) * 1.25);
    }

    ////// vector
    @Test
    public void addTwoVectorsSameType() {
        NodeConstant result = add.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(45 + 45));
    }

    @Test
    public void addTwoVectorsDifferentType() {
        NodeConstant result = add.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + (1/8d)), new NodeDouble(45 + (3/8d)));
    }

    @Test
    public void addVectorAndDouble() {
        NodeConstant result = add.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 + 23), new NodeDouble(45 + 23));
    }

    @Test
    public void addVectorAndRational() {
        NodeConstant result = add.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((2/8d)), new NodeRational((4/8d)));
    }

    @Test
    public void addVectorAndPercent() {
        NodeConstant result = add.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d * 1.25)), new NodeRational((3/8d * 1.25)));
    }

    ////// percent
    @Test
    public void addTwoPercentages() {
        NodeConstant result = add.toResult(p1, p2);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.75);
    }

    @Test
    public void addPercentAndDouble() {
        NodeConstant result = add.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(45.50);
    }

    @Test
    public void addPercentAndRational() {
        NodeConstant result = add.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.625);
    }

    @Test
    public void addPercentAndVector() {
        NodeConstant result = add.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d + 0.25)), new NodeRational((3/8d + 0.25)));
    }

}