package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeVariableTest {

    private NodeVariable a = new NodeVariable("a");
    private NodeVariable b = new NodeVariable("b");

    @Test
    void create() {
        assertThat(a.getVariable()).isEqualTo("a");
        assertThat(b.getVariable()).isEqualTo("b");
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeVariable("abc"))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeVariable("a"));
        assertThat(a.hashCode()).isEqualTo(new NodeVariable("a").hashCode());
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeVariable("a"));
    }

    @Test
    void transformer() {
        assertThrows(UnsupportedOperationException.class, () -> a.getTransformer().toNodeNumber());
    }

    @Test
    void variableToString() {
        assertThat(a.toString()).isEqualTo("a");
        assertThat(b.toString()).isEqualTo("b");
    }
}