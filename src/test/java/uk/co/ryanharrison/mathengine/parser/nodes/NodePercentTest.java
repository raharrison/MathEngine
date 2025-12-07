package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NodePercentTest {

    private NodePercent a = new NodePercent(23.5);
    private NodePercent b = new NodePercent(-1.5);

    @Test
    void doubleValue() {
        assertThat(a.doubleValue()).isEqualTo(23.5 / 100);
        assertThat(b.doubleValue()).isEqualTo(-1.5 / 100);
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodePercent(1))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodePercent(23.5));
        assertThat(a.hashCode()).isEqualTo(new NodePercent(23.5).hashCode());
    }

    @Test
    void percentString() {
        assertThat(a.toString()).isEqualTo("23.5%");
        assertThat(b.toString()).isEqualTo("-1.5%");
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(new NodePercent(23.5))).isZero();
        assertThat(a.compareTo(b)).isPositive();
        assertThat(b.compareTo(a)).isNegative();

        assertThat(a.compareTo(new NodeRational(5))).isNegative();
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodePercent(23.5));
    }

    @Test
    void applyFuncs() {
        assertThat(a.applyUniFunc(a -> a.add(new NodePercent(2)))).isEqualTo(new NodePercent(25.5));
        assertThat(a.applyBiFunc(new NodePercent(2), NodeNumber::add)).isEqualTo(new NodePercent(25.5));
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[]{a}));
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][]{{a}}));
    }

    @Test
    void add() {
        assertThat(a.add(new NodePercent(2))).isEqualTo(new NodePercent(25.5));
        assertThat(b.add(new NodePercent(3))).isEqualTo(new NodePercent(1.5));
    }

    @Test
    void subtract() {
        assertThat(a.subtract(new NodePercent(2))).isEqualTo(new NodePercent(21.5));
        assertThat(b.subtract(new NodePercent(3))).isEqualTo(new NodePercent(-4.5));
    }

    @Test
    void multiply() {
        assertThat(a.multiply(new NodePercent(2))).isEqualTo(new NodePercent(0.47));
        assertThat(b.multiply(new NodePercent(2))).isEqualTo(new NodePercent(-0.03));
        assertThat(b.multiply(new NodePercent(2)).doubleValue()).isEqualTo(-0.0003);
    }

    @Test
    void divide() {
        assertThat(a.divide(new NodePercent(2)).doubleValue()).isEqualTo(11.75);
        assertThat(b.divide(new NodePercent(50)).doubleValue()).isEqualTo(-0.03);
    }

    @Test
    void pow() {
        assertThat(new NodePercent(10).pow(new NodePercent(2))).isEqualTo(new NodePercent(95.4992586021436));
        assertThat(new NodePercent(100).pow(new NodeDouble(2))).isEqualTo(new NodePercent(100));
    }
}