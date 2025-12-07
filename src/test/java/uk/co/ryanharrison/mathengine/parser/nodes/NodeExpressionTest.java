package uk.co.ryanharrison.mathengine.parser.nodes;

import org.junit.jupiter.api.Test;
import uk.co.ryanharrison.mathengine.parser.operators.binary.Add;
import uk.co.ryanharrison.mathengine.parser.operators.unary.simple.Sine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeExpressionTest {

    private NodeExpression a = new NodeExpression(new Add(), new NodeDouble(12.5), new NodeDouble(1.3));
    private NodeExpression b = new NodeExpression(new Sine(), new NodeDouble(12.5));

    @Test
    void create() {
        assertThat(a.getArgOne()).isEqualTo(new NodeDouble(12.5));
        assertThat(a.getArgTwo()).isEqualTo(new NodeDouble(1.3));
        assertThat(a.getOperator()).isInstanceOf(Add.class);
    }

    @Test
    void createInvalidArgsForOperator() {
        assertThrows(IllegalArgumentException.class, () -> new NodeExpression(new Add(), new NodeDouble(12.5)));
    }

    @Test
    void equalsAndHashcode() {
        assertThat(a.equals(b)).isFalse();
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());

        assertThat(a).isEqualTo(new NodeExpression(new Add(), new NodeDouble(12.5), new NodeDouble(1.3)));
        assertThat(a.hashCode()).isEqualTo(new NodeExpression(new Add(), new NodeDouble(12.5), new NodeDouble(1.3)).hashCode());
    }

    @Test
    void copy() {
        assertThat(a.copy()).isEqualTo(new NodeExpression(new Add(), new NodeDouble(12.5), new NodeDouble(1.3)));
    }

    @Test
    void transformer() {
        assertThrows(UnsupportedOperationException.class, () -> a.getTransformer().toNodeNumber());
    }

    @Test
    void expressionToString() {
        NodeDouble d = new NodeDouble(12);
        NodeExpression m1 = new NodeExpression(new Add(), new NodeMatrix(new Node[][]{{d}}), (new NodeMatrix(new Node[][]{{a}})));
        NodeExpression m2 = new NodeExpression(new Sine(), (new NodeMatrix(new Node[][]{{a}})));
        assertThat(m1.toString()).isEqualTo("([{ 12.0 }] + [{ (12.5 + 1.3) }])");
        assertThat(m2.toString()).isEqualTo("sin([{ (12.5 + 1.3) }])");
    }
}