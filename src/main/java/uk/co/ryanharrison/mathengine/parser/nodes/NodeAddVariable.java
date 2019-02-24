package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Utils;

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
            return node.getTransformer().toNodeNumber()
                    .add(NodeFactory.createNodeNumberFrom(Utils.stringToNum(variable)));
        }
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, node.toString());
    }
}
