package uk.co.ryanharrison.mathengine.parser.nodes;

import java.util.Objects;

public final class NodeAddVariable extends Node {

    private String variable;
    private Node node;

    public NodeAddVariable(String variable, Node node) {
        this.variable = variable;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public Node copy() {
        return new NodeAddVariable(variable, node.copy());
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeAddVariableTransformer();
    }

    private class NodeAddVariableTransformer extends DefaultNodeTransformer {

        @Override
        public NodeNumber toNodeNumber() {
            return node.getTransformer().toNodeNumber();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeAddVariable that = (NodeAddVariable) o;
        return Objects.equals(variable, that.variable) &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, node);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, node.toString());
    }
}
