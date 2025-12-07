package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NodeAddVariableTest {

    private NodeAddVariable a = new NodeAddVariable("var1", new NodeDouble(12));
    private NodeAddVariable b = new NodeAddVariable("var2", new NodeDouble(34));

    @Test
    void create() {
        assertThat(a.getNode()).isEqualTo(new NodeDouble(12));
        assertThat(a.getVariable()).isEqualTo("var1");
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeAddVariable("var1", new NodeDouble(13)))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeAddVariable("var1", new NodeDouble(12)));
        assertThat(a.hashCode()).isEqualTo(new NodeAddVariable("var1", new NodeDouble(12)).hashCode());
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeAddVariable("var1", new NodeDouble(12)));
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber()).isEqualTo(new NodeDouble(12));
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[]{new NodeDouble(12)}));
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][]{{new NodeDouble(12)}}));
    }

    @Test
    void addVariableToString() {
        assertThat(a.toString()).isEqualTo("var1 = 12.0");
        assertThat(b.toString()).isEqualTo("var2 = 34.0");
    }
}