package uk.co.ryanharrison.mathengine.parser.nodes;

import java.util.Objects;

public class NodeVariable extends Node {

    private String variable;

    public NodeVariable(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeVariable that = (NodeVariable) o;
        return Objects.equals(variable, that.variable);
    }

    @Override
    public NodeVariable copy() {
        return new NodeVariable(variable);
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeVariableTransformer();
    }

    private class NodeVariableTransformer extends DefaultNodeTransformer {

        @Override
        public NodeNumber toNodeNumber() {
            throw new UnsupportedOperationException("Cannot convert variable to a number");
        }
    }

    @Override
    public String toString() {
        return variable;
    }
}
