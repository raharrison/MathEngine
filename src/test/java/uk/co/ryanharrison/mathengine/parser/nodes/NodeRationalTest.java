package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NodeRationalTest {

    private NodeRational a = new NodeRational(23.5);
    private NodeRational b = new NodeRational(-1.5);

    @Test
    void doubleValue() {
        assertThat(a.doubleValue()).isEqualTo(23.5);
        assertThat(b.doubleValue()).isEqualTo(-1.5);
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeRational(1))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeRational(23.5));
        assertThat(a.hashCode()).isEqualTo(new NodeRational(23.5).hashCode());
        assertThat(a).isNotEqualTo(new NodeDouble(12));
    }

    @Test
    void doubleString() {
        assertThat(a.toString()).isEqualTo("47/2");
        assertThat(b.toString()).isEqualTo("-3/2");
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(new NodeRational(23.5))).isZero();
        assertThat(a.compareTo(b)).isPositive();
        assertThat(b.compareTo(a)).isNegative();

        assertThat(a.compareTo(new NodeRational(5))).isPositive();
        assertThat(a.compareTo(new NodeRational(23.5))).isZero();
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeRational(23.5));
    }

    @Test
    void applyFuncs() {
        assertThat(a.applyUniFunc(a -> a.add(new NodeRational(2)))).isEqualTo(new NodeRational(25.5));
        assertThat(a.applyBiFunc(new NodeRational(2), NodeNumber::add)).isEqualTo(new NodeRational(25.5));
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[] { a }));
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][] { { a } }));
    }

    @Test
    void add() {
        assertThat(a.add(new NodeRational(2))).isEqualTo(new NodeRational(25.5));
        assertThat(b.add(new NodeRational(3))).isEqualTo(new NodeRational(1.5));
    }

    @Test
    void subtract() {
        assertThat(a.subtract(new NodeRational(2))).isEqualTo(new NodeRational(21.5));
        assertThat(b.subtract(new NodeRational(3))).isEqualTo(new NodeRational(-4.5));
    }

    @Test
    void multiply() {
        assertThat(a.multiply(new NodeRational(2))).isEqualTo(new NodeRational(47.0));
        assertThat(b.multiply(new NodeRational(3))).isEqualTo(new NodeRational(-4.5));
    }

    @Test
    void divide() {
        assertThat(a.divide(new NodeRational(2))).isEqualTo(new NodeRational(23.5/2d));
        assertThat(b.divide(new NodeRational(3))).isEqualTo(new NodeRational(-1.5/3d));
    }

    @Test
    void pow() {
        assertThat(a.pow(new NodeRational(2))).isEqualTo(new NodeRational(23.5*23.5));
        assertThat(b.pow(new NodeRational(3))).isEqualTo(new NodeRational(-1.5*-1.5*-1.5));
    }
}