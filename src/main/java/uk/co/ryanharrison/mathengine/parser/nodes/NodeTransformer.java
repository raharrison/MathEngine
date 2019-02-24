package uk.co.ryanharrison.mathengine.parser.nodes;

public interface NodeTransformer {
    NodeNumber toNodeNumber();

    NodeVector toNodeVector();

    NodeMatrix toNodeMatrix();
}

abstract class DefaultNodeTransformer implements NodeTransformer {

    @Override
    public NodeVector toNodeVector() {
        return new NodeVector(new Node[]{toNodeNumber()});
    }

    @Override
    public NodeMatrix toNodeMatrix() {
        return new NodeMatrix(new Node[][]{{toNodeNumber()}});
    }
}
