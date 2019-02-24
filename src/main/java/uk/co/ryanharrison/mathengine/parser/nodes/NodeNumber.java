package uk.co.ryanharrison.mathengine.parser.nodes;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class NodeNumber extends NodeConstant {

    @Override
    public NodeConstant applyUniFunc(Function<NodeNumber, NodeConstant> func) {
        return func.apply(this);
    }

    @Override
    public NodeConstant applyBiFunc(NodeConstant b, BiFunction<NodeNumber, NodeNumber, NodeConstant> func) {
        return func.apply(this, b.getTransformer().toNodeNumber());
    }

    public abstract double doubleValue();

    public abstract NodeNumber add(NodeNumber arg2);

    public abstract NodeNumber subtract(NodeNumber arg2);

    public abstract NodeNumber multiply(NodeNumber arg2);

    public abstract NodeNumber divide(NodeNumber arg2);

    public abstract NodeNumber pow(NodeNumber arg2);

    @Override
    public NodeTransformer createTransformer() {
        return new NodeNumberTransformer();
    }

    private class NodeNumberTransformer extends DefaultNodeTransformer {

        @Override
        public NodeNumber toNodeNumber() {
            return NodeNumber.this;
        }
    }
}
