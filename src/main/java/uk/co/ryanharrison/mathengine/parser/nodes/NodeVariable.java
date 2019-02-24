package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.Utils;

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
    public NodeVariable copy() {
        return new NodeVariable(variable);
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeVariableTransformer();
    }

    private class NodeVariableTransformer implements NodeTransformer {

        @Override
        public NodeVector toNodeVector() {
            return new NodeVector(new Node[]{toNodeNumber()});
        }

        @Override
        public NodeMatrix toNodeMatrix() {
            return new NodeMatrix(new Node[][]{{toNodeNumber()}});
        }

        @Override
        public NodeNumber toNodeNumber() {
            return NodeFactory
                    .createNodeNumberFrom(Utils.stringToNum(variable));
        }
    }

    @Override
    public String toString() {
        return variable;
    }
}
