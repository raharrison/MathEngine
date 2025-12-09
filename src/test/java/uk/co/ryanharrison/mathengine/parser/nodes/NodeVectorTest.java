package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.linearalgebra.Vector;

import static org.assertj.core.api.Assertions.assertThat;

class NodeVectorTest {

    private NodeVector a = new NodeVector(new Node[]{new NodeDouble(12), new NodeDouble(43)});
    private NodeVector b = new NodeVector(new Node[]{new NodeDouble(75)});

    @Test
    void create() {
        assertThat(a.getValues()).hasSize(2);
        assertThat(a.getValues()).containsOnly(new NodeDouble(12), new NodeDouble(43));
        assertThat(a.getSize()).isEqualTo(2);

        assertThat(new NodeVector(Vector.of(12)).getValues()).containsOnly(new NodeDouble(12));
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(a.copy())).isZero();
        assertThat(a.compareTo(b)).isNegative();
        assertThat(b.compareTo(a)).isPositive();

        assertThat(a.compareTo(new NodeRational(5))).isPositive();
        assertThat(b.compareTo(new NodeRational(75))).isZero();
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(new NodeVector(new Node[]{}))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(b).isEqualTo(new NodeVector(new Node[]{new NodeDouble(75)}));
        assertThat(b.hashCode()).isEqualTo(new NodeVector(new Node[]{new NodeDouble(75)}).hashCode());
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber().doubleValue()).isEqualTo(12 + 43d);
        assertThat(b.getTransformer().toNodeNumber().doubleValue()).isEqualTo(75d);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(new NodeMatrix(new Node[][]{a.getValues()}));
    }

    @Test
    void copy() {
        assertThat(b.copy()).isEqualTo(new NodeVector(new Node[]{new NodeDouble(75)}));
    }

    @Test
    void applyBiFunc() {
        NodeVector res = a.applyBiFunc(new NodeDouble(12), NodeNumber::add);
        assertThat(res.getSize()).isEqualTo(2);
        assertThat(res.getValues()).containsOnly(new NodeDouble(24), new NodeDouble(55));
    }

    @Test
    void applyBiFuncFirstLongest() {
        NodeVector v1 = new NodeVector(new Node[]{new NodeDouble(12), new NodeDouble(43), new NodeDouble(19)});
        NodeVector v2 = new NodeVector(new Node[]{new NodeDouble(35), new NodeDouble(22)});

        NodeVector res = v1.applyBiFunc(v2, NodeNumber::add);
        assertThat(res.getSize()).isEqualTo(3);
        assertThat(res.getValues()).containsOnly(new NodeDouble(12 + 35), new NodeDouble(43 + 22), new NodeDouble(19));
    }

    @Test
    void applyBiFuncSecondLongest() {
        NodeVector v1 = new NodeVector(new Node[]{new NodeDouble(12), new NodeDouble(43), new NodeDouble(19)});
        NodeVector v2 = new NodeVector(new Node[]{new NodeDouble(35), new NodeDouble(22)});

        NodeVector res = v2.applyBiFunc(v1, NodeNumber::add);
        assertThat(res.getSize()).isEqualTo(3);
        assertThat(res.getValues()).containsOnly(new NodeDouble(12 + 35), new NodeDouble(43 + 22), new NodeDouble(19));
    }

    @Test
    void applyUniFunc() {
        NodeVector res = a.applyUniFunc((n) -> n.add(new NodeDouble(12)));
        assertThat(res.getSize()).isEqualTo(2);
        assertThat(res.getValues()).containsOnly(new NodeDouble(24), new NodeDouble(55));

        NodeVector nested = new NodeVector(new Node[]{b});
        NodeVector res2 = nested.applyUniFunc((n) -> n.add(new NodeDouble(12)));
        assertThat(res2.getSize()).isEqualTo(1);
        assertThat(res2.getValues()).containsOnly(new NodeVector(new Node[]{new NodeDouble(87)}));
    }

    @Test
    void resolve() {
        NodeVector res = a.resolve(n -> new NodeDouble(65));
        assertThat(res.getSize()).isEqualTo(2);
        assertThat(res.getValues()).containsOnly(new NodeDouble(65));
    }

    @Test
    void vectorString() {
        assertThat(a.toString()).isEqualTo("{ 12.0, 43.0 }");
        assertThat(b.toString()).isEqualTo("{ 75.0 }");
    }
}