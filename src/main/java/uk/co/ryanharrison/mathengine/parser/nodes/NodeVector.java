package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.linearalgebra.Vector;
import uk.co.ryanharrison.mathengine.utils.Utils;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NodeVector extends NodeConstant implements NodeSet {

    private Node[] values;

    public NodeVector(Node[] values) {
        this.values = values;
    }

    public NodeVector(Vector v) {
        values = new NodeDouble[v.getSize()];

        for (int i = 0; i < values.length; i++) {
            values[i] = new NodeDouble(v.get(i));
        }
    }

    @Override
    public int compareTo(NodeConstant cons) {
        double sum = getTransformer().toNodeNumber().doubleValue();

        if (cons instanceof NodeVector) {
            return Double.compare(sum, cons.getTransformer().toNodeNumber().doubleValue());
        } else {
            return new NodeDouble(sum).compareTo(cons);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeVector that = (NodeVector) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    public int getSize() {
        return values.length;
    }

    public Node[] getValues() {
        return values;
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeVectorTransformer();
    }

    @Override
    public NodeVector resolve(Function<Node, NodeConstant> func) {
        return new NodeVector(Arrays.stream(values).map(func).toArray(Node[]::new));
    }

    private class NodeVectorTransformer implements NodeTransformer {

        @Override
        public NodeVector toNodeVector() {
            return NodeVector.this;
        }

        @Override
        public NodeMatrix toNodeMatrix() {
            return new NodeMatrix(new Node[][]{values});
        }

        @Override
        public NodeNumber toNodeNumber() {
            NodeNumber sum = NodeFactory.createZeroNumber();
            for (NodeConstant constant : toNodeConstants()) {
                sum = sum.add(constant.getTransformer().toNodeNumber());
            }
            return sum;
        }
    }

    public NodeVector copy() {
        return new NodeVector(values.clone());
    }

    public NodeConstant[] toNodeConstants() {
        NodeConstant[] results = new NodeConstant[values.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = (NodeConstant) values[i];
        }
        return results;
    }

    private void normalizeVectorSizes(NodeVector b) {
        if (this.values.length == b.values.length)
            return;

        int thisLength = this.values.length;
        int otherLength = b.values.length;

        int longest = Math.max(thisLength, otherLength);

        Node[] results = new Node[longest];

        if (thisLength != longest) {
            if (thisLength == 1) {
                Arrays.fill(results, 0, longest, values[0]);
            } else {
                System.arraycopy(values, 0, results, 0, thisLength);
                Arrays.fill(results, thisLength, longest, NodeFactory.createZeroNumber());
            }
            this.values = results;
        } else {
            if (b.values.length == 1) {
                Arrays.fill(results, 0, longest, b.values[0]);
            } else {
                System.arraycopy(b.values, 0, results, 0, otherLength);
                Arrays.fill(results, otherLength, longest, NodeFactory.createZeroNumber());
            }
            b.values = results;
        }
    }

    @Override
    public NodeVector applyBiFunc(NodeConstant b, BiFunction<NodeNumber, NodeNumber, NodeConstant> func) {
        NodeVector arg2 = b.getTransformer().toNodeVector();
        normalizeVectorSizes(arg2);

        NodeConstant[] results = new NodeConstant[values.length];

        for (int i = 0; i < values.length; i++) {
            results[i] = func.apply(this.values[i].getTransformer().toNodeNumber(), arg2.values[i].getTransformer().toNodeNumber());
        }

        return new NodeVector(results);
    }

    @Override
    public NodeVector applyUniFunc(Function<NodeNumber, NodeConstant> func) {
        NodeConstant[] results = new NodeConstant[values.length];

        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof NodeNumber) {
                results[i] = func.apply(this.values[i].getTransformer().toNodeNumber());
            } else {
                results[i] = ((NodeConstant) values[i]).applyUniFunc(func);
            }
        }

        return new NodeVector(results);
    }

    @Override
    public String toString() {
        return "{ " + Utils.join(values, ", ") + " }";
    }
}
