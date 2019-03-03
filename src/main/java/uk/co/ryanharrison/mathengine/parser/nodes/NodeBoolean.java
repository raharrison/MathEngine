package uk.co.ryanharrison.mathengine.parser.nodes;

public class NodeBoolean extends NodeDouble {

    private boolean value;

    public NodeBoolean(boolean bool) {
        super(bool ? 1 : 0);
        this.value = bool;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value ? 1 : 0;
    }

    @Override
    public NodeDouble copy() {
        return new NodeBoolean(value);
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

}