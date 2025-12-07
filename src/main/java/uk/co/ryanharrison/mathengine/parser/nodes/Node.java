package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class Node {
    private NodeTransformer transformer;

    protected abstract NodeTransformer createTransformer();

    public NodeTransformer getTransformer() {
        if (transformer == null)
            transformer = createTransformer();
        return transformer;
    }

    public abstract Node copy();

    @Override
    public abstract String toString();
}
