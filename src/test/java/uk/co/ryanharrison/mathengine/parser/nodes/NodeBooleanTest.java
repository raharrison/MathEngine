package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NodeBooleanTest {

    private NodeBoolean a = new NodeBoolean(true);
    private NodeBoolean b = new NodeBoolean(false);

    @Test
    void getValue() {
        assertThat(a.getValue()).isTrue();
        assertThat(b.getValue()).isFalse();
    }

    @Test
    void doubleValue() {
        assertThat(a.doubleValue()).isEqualTo(1);
        assertThat(b.doubleValue()).isEqualTo(0);
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeDouble(1))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeBoolean(true));
        assertThat(a.hashCode()).isEqualTo(new NodeBoolean(true).hashCode());
    }

    @Test
    void booleanString() {
        assertThat(a.toString()).isEqualTo("true");
        assertThat(b.toString()).isEqualTo("false");
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(new NodeBoolean(true))).isZero();
        assertThat(a.compareTo(b)).isPositive();
        assertThat(b.compareTo(a)).isNegative();

        assertThat(a.compareTo(new NodeRational(5))).isNegative();
        assertThat(a.compareTo(new NodeRational(1))).isZero();
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeBoolean(true));
    }

    @Test
    void applyFuncs() {
        assertThat(a.applyUniFunc(a -> a.add(new NodeDouble(2)))).isEqualTo(new NodeDouble(3));
        assertThat(a.applyBiFunc(new NodeDouble(2), NodeNumber::add)).isEqualTo(new NodeDouble(3));
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[]{a}));
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][]{{a}}));
    }

    @Test
    void add() {
        assertThat(a.add(new NodeDouble(2))).isEqualTo(new NodeDouble(3));
        assertThat(b.add(new NodeDouble(3))).isEqualTo(new NodeDouble(3));
    }

    @Test
    void subtract() {
        assertThat(a.subtract(new NodeDouble(2))).isEqualTo(new NodeDouble(-1));
        assertThat(b.subtract(new NodeDouble(3))).isEqualTo(new NodeDouble(-3));
    }

    @Test
    void multiply() {
        assertThat(a.multiply(new NodeDouble(2))).isEqualTo(new NodeDouble(2));
        assertThat(b.multiply(new NodeDouble(3))).isEqualTo(new NodeDouble(0));
    }

    @Test
    void divide() {
        assertThat(a.divide(new NodeDouble(2))).isEqualTo(new NodeDouble(0.5));
        assertThat(b.divide(new NodeDouble(3))).isEqualTo(new NodeDouble(0));
    }

    @Test
    void pow() {
        assertThat(a.pow(new NodeDouble(2))).isEqualTo(new NodeDouble(1));
        assertThat(b.pow(new NodeDouble(3))).isEqualTo(new NodeDouble(0));
    }
}