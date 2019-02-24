package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.operators.Determinable;

public abstract class NodeNumber extends NodeConstant {

    @Override
    public NodeConstant applyDeterminable(Determinable deter) {
        return deter.getResult(this);
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
