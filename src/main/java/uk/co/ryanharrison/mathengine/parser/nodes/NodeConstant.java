package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements
        Comparable<NodeConstant>, Appliable {

    @Override
    public abstract boolean equals(Object object);

    @Override
    public abstract String toString();

}
