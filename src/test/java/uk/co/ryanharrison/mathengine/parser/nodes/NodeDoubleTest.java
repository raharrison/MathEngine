package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NodeDoubleTest {

    private NodeDouble a = new NodeDouble(23.5);
    private NodeDouble b = new NodeDouble(-1.5);

    @Test
    void doubleValue() {
        assertThat(a.doubleValue()).isEqualTo(23.5);
        assertThat(b.doubleValue()).isEqualTo(-1.5);
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeDouble(1))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeDouble(23.5));
        assertThat(a.hashCode()).isEqualTo(new NodeDouble(23.5).hashCode());
    }

    @Test
    void doubleString() {
        assertThat(a.toString()).isEqualTo("23.5");
        assertThat(b.toString()).isEqualTo("-1.5");
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(new NodeDouble(23.5))).isZero();
        assertThat(a.compareTo(b)).isPositive();
        assertThat(b.compareTo(a)).isNegative();

        assertThat(a.compareTo(new NodeRational(5))).isPositive();
        assertThat(a.compareTo(new NodeRational(23.5))).isZero();
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeDouble(23.5));
    }

    @Test
    void applyFuncs() {
        assertThat(a.applyUniFunc(a -> a.add(new NodeDouble(2)))).isEqualTo(new NodeDouble(25.5));
        assertThat(a.applyBiFunc(new NodeDouble(2), NodeNumber::add)).isEqualTo(new NodeDouble(25.5));
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[]{a}));
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][]{{a}}));
    }

    @Test
    void add() {
        assertThat(a.add(new NodeDouble(2))).isEqualTo(new NodeDouble(25.5));
        assertThat(b.add(new NodeDouble(3))).isEqualTo(new NodeDouble(1.5));
    }

    @Test
    void subtract() {
        assertThat(a.subtract(new NodeDouble(2))).isEqualTo(new NodeDouble(21.5));
        assertThat(b.subtract(new NodeDouble(3))).isEqualTo(new NodeDouble(-4.5));
    }

    @Test
    void multiply() {
        assertThat(a.multiply(new NodeDouble(2))).isEqualTo(new NodeDouble(47.0));
        assertThat(b.multiply(new NodeDouble(3))).isEqualTo(new NodeDouble(-4.5));
    }

    @Test
    void divide() {
        assertThat(a.divide(new NodeDouble(2))).isEqualTo(new NodeDouble(23.5 / 2d));
        assertThat(b.divide(new NodeDouble(3))).isEqualTo(new NodeDouble(-1.5 / 3d));
    }

    @Test
    void pow() {
        assertThat(a.pow(new NodeDouble(2))).isEqualTo(new NodeDouble(23.5 * 23.5));
        assertThat(b.pow(new NodeDouble(3))).isEqualTo(new NodeDouble(-1.5 * -1.5 * -1.5));
    }
}