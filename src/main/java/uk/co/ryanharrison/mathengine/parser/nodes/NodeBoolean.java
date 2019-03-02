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
    public boolean equals(Object object) {
        if (object instanceof NodeBoolean)
            return ((NodeBoolean) object).value == value;
        return false;
    }

    @Override
    public NodeDouble copy() {
        return new NodeBoolean(value);
    }

    @Override
    public int hashCode() {
        return Boolean.valueOf(value).hashCode();
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

}