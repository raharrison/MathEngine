package uk.co.ryanharrison.mathengine.parser.nodes;

public class NodeDouble extends NodeNumber {

    protected double value;

    public NodeDouble(double value) {
        this.value = value;
    }

    @Override
    public NodeNumber add(NodeNumber arg2) {
        if (arg2 instanceof NodePercent) {
            return new NodeDouble(doubleValue() * (1 + arg2.doubleValue()));
        }
        return new NodeDouble(doubleValue() + arg2.doubleValue());
    }

    @Override
    public NodeDouble copy() {
        return new NodeDouble(value);
    }

    @Override
    public int compareTo(NodeConstant cons) {
        if (cons instanceof NodeDouble)
            return Double.compare(this.doubleValue(), cons.getTransformer().toNodeNumber().doubleValue());

        // negate as switching the comparator
        return -cons.compareTo(this);
    }

    @Override
    public NodeNumber divide(NodeNumber arg2) {
        if(arg2 instanceof NodePercent)
            return new NodeDouble(doubleValue() / (arg2.doubleValue()));
        return new NodeDouble(doubleValue() / arg2.doubleValue());
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NodeDouble) {
            return Double.compare(((NodeConstant) object).getTransformer().toNodeNumber().doubleValue(), this.doubleValue()) == 0;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(value).hashCode();
    }

    @Override
    public NodeNumber multiply(NodeNumber arg2) {
        return new NodeDouble(doubleValue() * arg2.doubleValue());
    }

    @Override
    public NodeNumber pow(NodeNumber arg2) {
        return new NodeDouble(Math.pow(doubleValue(), arg2.doubleValue()));
    }

    @Override
    public NodeNumber subtract(NodeNumber arg2) {
        if(arg2 instanceof NodePercent)
            return new NodeDouble(doubleValue() * (1 - arg2.doubleValue()));
        return new NodeDouble(doubleValue() - arg2.doubleValue());
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public String toTypeString() {
        return "number";
    }
}
