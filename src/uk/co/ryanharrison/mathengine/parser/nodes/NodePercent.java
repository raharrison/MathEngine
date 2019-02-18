package uk.co.ryanharrison.mathengine.parser.nodes;

public final class NodePercent extends NodeDouble {
    public NodePercent(double value) {
        super(value);
    }

    @Override
    public NodeNumber add(NodeNumber arg2) {
        if (arg2 instanceof NodePercent)
            return new NodePercent(doubleValue() * 100.0 + arg2.doubleValue() * 100);
        else if (arg2 instanceof NodeRational)
            return new NodeRational(doubleValue()).add(arg2);
        return super.add(arg2);
    }

    @Override
    public NodeConstant divide(NodePercent arg2) {
        return new NodePercent((doubleValue() * 100.0) / (arg2.doubleValue() * 100));
    }

    @Override
    public double doubleValue() {
        return super.doubleValue() / 100.0;
    }

    @Override
    public NodeConstant multiply(NodePercent arg2) {
        return new NodePercent((doubleValue() * 100.0) * (arg2.doubleValue() * 100));
    }

    @Override
    public NodeConstant pow(NodePercent arg2) {
        return new NodePercent(Math.pow((doubleValue() * 100.0), (arg2.doubleValue() * 100)));
    }

    @Override
    public NodeNumber subtract(NodeNumber arg2) {
        if (arg2 instanceof NodePercent)
            return new NodePercent(doubleValue() * 100.0 - arg2.doubleValue() * 100);
        else if (arg2 instanceof NodeRational)
            return new NodeRational(doubleValue()).subtract(arg2);
        return super.subtract(arg2);
    }

    @Override
    public NodeDouble clone() {
        return new NodePercent(this.value);
    }

    @Override
    public String toString() {
        return super.toString() + "%";
    }

    @Override
    public String toTypeString() {
        return "percentage";
    }
}
