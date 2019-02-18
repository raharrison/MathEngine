package uk.co.ryanharrison.mathengine.parser.operators.binary;

import org.junit.Test;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import static java.lang.Math.pow;
import static org.assertj.core.api.Assertions.assertThat;

public class PowTest {

    private Pow pow = new Pow();
    private NodeDouble d1 = new NodeDouble(5);
    private NodeDouble d2 = new NodeDouble(3);
    private NodeRational r1 = new NodeRational(6, 8);
    private NodeRational r2 = new NodeRational(4, 8);
    private NodeVector v1 = new NodeVector(new Node[] { d1, d2 });
    private NodeVector v2 = new NodeVector(new Node[] { r1, r2 });
    private NodePercent p1 = new NodePercent(25);
    private NodePercent p2 = new NodePercent(50);

    ////// double
    @Test
    public void powTwoDoubles() {
        NodeConstant result = pow.toResult(d1, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(5, 3));
    }

    @Test
    public void powDoubleAndRational() {
        NodeConstant result = pow.toResult(d1, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(5, (6/8d)));
    }

    @Test
    public void powDoubleAndVector() {
        NodeConstant result = pow.toResult(d1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(5, 5)), new NodeDouble(pow(5, 3)));
    }

    @Test
    public void powDoubleAndPercent() {
        NodeConstant result = pow.toResult(d1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(5, 0.25));
    }

    ////// rational
    @Test
    public void powTwoRationals() {
        NodeConstant result = pow.toResult(r1, r2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow((6/8d), (4/8d)));
    }

    @Test
    public void powRationalAndDouble() {
        NodeConstant result = pow.toResult(r1, d1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(6/8d, 5));
    }

    @Test
    public void powRationalAndVector() {
        NodeConstant result = pow.toResult(r1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(6/8d, 5)), new NodeDouble(pow(6/8d, 3)));
    }

    @Test
    public void powRationalAndPercent() {
        NodeConstant result = pow.toResult(r1, p1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(6/8d, 0.25));
    }

    ////// vector
    @Test
    public void powTwoVectorsSameType() {
        NodeConstant result = pow.toResult(v1, v1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(5, 5)), new NodeDouble(pow(3, 3)));
    }

    @Test
    public void powTwoVectorsDifferentType() {
        NodeConstant result = pow.toResult(v1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(5, 6/8d)), new NodeDouble(pow(3, 4/8d)));
    }

    @Test
    public void powVectorAndDouble() {
        NodeConstant result = pow.toResult(v1, d1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(5, 5)), new NodeDouble(pow(3, 5)));
    }

    @Test
    public void powVectorAndRational() {
        NodeConstant result = pow.toResult(v2, r1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(6/8d, 6/8d)), new NodeDouble(pow(4/8d, 6/8d)));
    }

    @Test
    public void powVectorAndPercent() {
        NodeConstant result = pow.toResult(v2, p1);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(6/8d, 0.25)), new NodeDouble(pow(4/8d, 0.25)));
    }

    ////// percent
    @Test
    public void powTwoPercentages() {
        NodeConstant result = pow.toResult(p1, p2);

        assertThat(result).isInstanceOf(NodePercent.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(0.25, 0.50));
    }

    @Test
    public void powPercentAndDouble() {
        NodeConstant result = pow.toResult(p2, d2);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(0.5, 3));
    }

    @Test
    public void powPercentAndRational() {
        NodeConstant result = pow.toResult(p2, r1);

        assertThat(result).isInstanceOf(NodeDouble.class);
        assertThat(result.getTransformer().toNodeNumber().doubleValue()).isEqualTo(pow(0.5, 6/8d));
    }

    @Test
    public void powPercentAndVector() {
        NodeConstant result = pow.toResult(p1, v2);

        assertThat(result).isInstanceOf(NodeVector.class);
        assertThat(result.getTransformer().toNodeVector().getValues()).containsOnly(new NodeDouble(pow(0.25, 6/8d)), new NodeDouble(pow(0.25, 4/8d)));
    }

}