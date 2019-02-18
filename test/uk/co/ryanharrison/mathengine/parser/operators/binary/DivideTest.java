package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static org.assertj.core.api.Assertions.assertThat;

public class DivideTest {

    private Divide divide = new Divide();
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
    public void divideTwoDoubles() {
        NodeConstant result = divide.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 / 45d);
    }

    @Test
    public void divideDoubleAndRational() {
        NodeConstant result = divide.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 / (1/8d));
    }

    @Test
    public void divideDoubleAndVector() {
        NodeConstant result = divide.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(23 / 45d));
    }

    @Test
    public void divideDoubleAndPercent() {
        NodeConstant result = divide.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(23 * (1 / 0.25));
    }

    ////// rational
    @Test
    public void divideTwoRationals() {
        NodeConstant result = divide.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / (3/8d));
    }

    @Test
    public void divideRationalAndDouble() {
        NodeConstant result = divide.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / 23);
    }

    @Test
    public void divideRationalAndVector() {
        NodeConstant result = divide.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble((1/8d) / 23), new NodeDouble((1/8d) / 45));
    }

    @Test
    public void divideRationalAndPercent() {
        NodeConstant result = divide.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo((1/8d) / 0.25);
    }

    ////// vector
    @Test
    public void divideTwoVectorsSameType() {
        NodeConstant result = divide.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(1));
    }

    @Test
    public void divideTwoVectorsDifferentType() {
        NodeConstant result = divide.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(23 / (1/8d)), new NodeDouble(45 / (3/8d)));
    }

    @Test
    public void divideVectorAndDouble() {
        NodeConstant result = divide.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(1), new NodeDouble(45 / 23d));
    }

    @Test
    public void divideVectorAndRational() {
        NodeConstant result = divide.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(1), new NodeRational((3/8d) / (1/8d)));
    }

    @Test
    public void divideVectorAndPercent() {
        NodeConstant result = divide.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational((1/8d) / 0.25), new NodeRational((3/8d) / 0.25));
    }

    ////// percent
    @Test
    public void divideTwoPercentages() {
        NodeConstant result = divide.toResult(p2, p1);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.02);
    }

    @Test
    public void dividePercentAndDouble() {
        NodeConstant result = divide.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(0.5 / 45);
    }

    @Test
    public void dividePercentAndRational() {
        NodeConstant result = divide.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeRational.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(4.0);
    }

    @Test
    public void dividePercentAndVector() {
        NodeConstant result = divide.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeRational(0.25 / (1/8d)), new NodeRational(0.25 / (3/8d)));
    }

}