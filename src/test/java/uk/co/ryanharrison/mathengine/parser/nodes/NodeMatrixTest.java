package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;

import static org.assertj.core.api.Assertions.assertThat;

class NodeMatrixTest {

    private NodeMatrix a = new NodeMatrix(new Node[][] {
            { new NodeDouble(12), new NodeDouble(43)},
            { new NodeDouble(24), new NodeDouble(7)}});

    private NodeMatrix b = new NodeMatrix(new Node[][] {
            { new NodeDouble(77), new NodeDouble(56)},
            { new NodeDouble(11), new NodeDouble(65)}});

    private NodeMatrix c = new NodeMatrix(new Matrix(new double[][] {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 } }));

    @Test
    void create() {
        assertThat(a.rowCount()).isEqualTo(2);
        assertThat(a.colCount()).isEqualTo(2);
        assertThat(a.getValues()).hasSize(2);
        assertThat(a.getValues()).contains(new Node[] { new NodeDouble(12), new NodeDouble(43)});
        assertThat(a.getValues()).contains(new Node[] { new NodeDouble(24), new NodeDouble(7)});
        assertThat(c.getValues()).hasSize(3);
    }

    @Test
    void compareTo() {
        assertThat(a.compareTo(a.copy())).isZero();
        assertThat(a.compareTo(b)).isNegative();
        assertThat(b.compareTo(a)).isPositive();

        assertThat(a.compareTo(new NodeRational(5))).isPositive();
        assertThat(a.compareTo(new NodeRational(12 + 43 + 24 + 7))).isZero();
    }

    @Test
    void equals() {
        assertThat(a.equals(new NodeMatrix(new Node[][]{}))).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        NodeMatrix compare = new NodeMatrix(new Node[][]{
                { new NodeDouble(77), new NodeDouble(56)},
                { new NodeDouble(11), new NodeDouble(65)}});
        assertThat(b).isEqualTo(compare);
        assertThat(b.hashCode()).isEqualTo(compare.hashCode());
    }

    @Test
    void transformer() {
        assertThat(a.getTransformer().toNodeNumber().doubleValue()).isEqualTo(12 + 43d + 24 + 7);
        assertThat(b.getTransformer().toNodeNumber().doubleValue()).isEqualTo(77 + 56 + 11 + 65);
        assertThat(a.getTransformer().toNodeMatrix()).isEqualTo(a);
        assertThat(a.getTransformer().toNodeVector()).isEqualTo(new NodeVector(new Node[] {
            new NodeVector(new Node[] { new NodeDouble(12), new NodeDouble(43)}),
            new NodeVector(new Node[]  { new NodeDouble(24), new NodeDouble(7)})
        }));
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(a);
    }

    @Test
    void toDoubleMatrix() {
        Matrix expected = new Matrix(new double[][] {
                { 77, 56 },
                { 11, 65 }
        });
        assertThat(b.toDoubleMatrix()).isEqualTo(expected);
    }

    @Test
    void applyUniFunc() {
        NodeMatrix res = a.applyUniFunc((n) -> n.add(new NodeDouble(12)));
        assertThat(res.rowCount()).isEqualTo(2);
        assertThat(res.colCount()).isEqualTo(2);
        assertThat(res.getValues()).contains(new Node[] {new NodeDouble(24), new NodeDouble(55)});
        assertThat(res.getValues()).contains(new Node[] {new NodeDouble(24), new NodeDouble(55)});

        NodeMatrix nested = new NodeMatrix(new Node[][] { { new NodeVector(new Node[] {new NodeDouble(25)}) },
                { new NodeDouble(4)} });
        NodeMatrix res2 = nested.applyUniFunc((n) -> n.add(new NodeDouble(12)));
        assertThat(res2.rowCount()).isEqualTo(2);
        assertThat(res2.colCount()).isEqualTo(1);
        assertThat(res2.getValues()).contains(new Node[] { new NodeVector(new Node[] { new NodeDouble(25 + 12)})});
        assertThat(res2.getValues()).contains(new Node[] { new NodeDouble(4 + 12)});
    }

    @Test
    void applyBiFunc() {
        NodeMatrix res = a.applyBiFunc(new NodeDouble(12), NodeNumber::add);
        assertThat(res.rowCount()).isEqualTo(2);
        assertThat(res.colCount()).isEqualTo(2);
        assertThat(res.getValues()).contains(new Node[] {new NodeDouble(24), new NodeDouble(55)});
        assertThat(res.getValues()).contains(new Node[] {new NodeDouble(24), new NodeDouble(55)});

        NodeMatrix empty = new NodeMatrix(new Node[][] { } );
        NodeMatrix res2 = empty.applyBiFunc(new NodeDouble(12), NodeNumber::add);
        assertThat(res2.rowCount()).isOne();
        assertThat(res2.colCount()).isOne();
        assertThat(res2.getValues()).containsOnly(new Node[] { new NodeDouble(12) });
    }

    @Test
    void applyBiFuncFirstLongest() {
        NodeMatrix res = c.applyBiFunc(a, NodeNumber::add);
        assertThat(res.rowCount()).isEqualTo(3);
        assertThat(res.colCount()).isEqualTo(3);
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(12+1), new NodeDouble(43+2), new NodeDouble(3)});
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(24+4), new NodeDouble(7+5), new NodeDouble(6)});
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(7), new NodeDouble(8), new NodeDouble(9)});
    }

    @Test
    void applyBiFuncSecondLongest() {
        NodeMatrix res = a.applyBiFunc(c, NodeNumber::add);
        assertThat(res.rowCount()).isEqualTo(3);
        assertThat(res.colCount()).isEqualTo(3);
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(12+1), new NodeDouble(43+2), new NodeDouble(3)});
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(24+4), new NodeDouble(7+5), new NodeDouble(6)});
        assertThat(res.getValues()).contains(new Node[] { new NodeDouble(7), new NodeDouble(8), new NodeDouble(9)});
    }

    @Test
    void resolve() {
        NodeDouble nd = new NodeDouble(65);
        NodeMatrix res = a.resolve(n -> nd);
        assertThat(res.rowCount()).isEqualTo(2);
        assertThat(res.colCount()).isEqualTo(2);
        assertThat(res.getValues()[0]).containsOnly(nd);
        assertThat(res.getValues()[1]).containsOnly(nd);
    }

    @Test
    void toShortString() {
        assertThat(new NodeMatrix(new Node[][]{}).toShortString()).isEqualTo("[]");
        assertThat(a.toShortString()).isEqualTo("[{ 12.0, 43.0 }, { 24.0, 7.0 }]");
        assertThat(b.toShortString()).isEqualTo("[{ 77.0, 56.0 }, { 11.0, 65.0 }]");
    }

    @Test
    void matrixString() {
        assertThat(new NodeMatrix(new Node[][]{}).toString()).isEqualTo("[]");
        assertThat(a.toString()).isEqualTo("12.0\t43.0\n" +
                "24.0\t7.0");
        assertThat(b.toString()).isEqualTo("77.0\t56.0\n" +
                "11.0\t65.0");
    }
}