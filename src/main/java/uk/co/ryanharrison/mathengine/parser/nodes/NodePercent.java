package uk.co.ryanharrison.mathengine.parser.nodes;

public final class NodePercent extends NodeDouble {

    public NodePercent(double value) {
        super(value);
    }

    @Override
    public double doubleValue() {
        return super.doubleValue() / 100.0;
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
    public NodeNumber divide(NodeNumber arg2) {
        if (arg2 instanceof NodePercent)
            return new NodePercent((doubleValue() * 100.0) / (arg2.doubleValue() * 100));
        else if (arg2 instanceof NodeRational)
            return new NodeRational(doubleValue()).divide(arg2);
        return super.divide(arg2);
    }

    @Override
    public NodeNumber multiply(NodeNumber arg2) {
        if (arg2 instanceof NodePercent)
            return new NodePercent((doubleValue() * arg2.doubleValue()) * 100);
        else if (arg2 instanceof NodeRational)
            return new NodeRational(doubleValue()).multiply(arg2);
        return super.multiply(arg2);
    }

    @Override
    public NodeNumber pow(NodeNumber arg2) {
        if (arg2 instanceof NodePercent)
            return new NodePercent(Math.pow((doubleValue()), (arg2.doubleValue())) * 100);
        else if (arg2 instanceof NodeRational)
            return new NodeRational(doubleValue()).pow(arg2);
        return super.pow(arg2);
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
    public NodeDouble copy() {
        return new NodePercent(this.value);
    }

    @Override
    public String toString() {
        return super.toString() + "%";
    }

}
