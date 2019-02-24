package uk.co.ryanharrison.mathengine.parser.nodes;

import java.util.function.Function;

public abstract class NodeConstant extends Node implements
        Comparable<NodeConstant> {

    //public abstract NodeConstant applyDeterminable(Determinable deter);

    public abstract NodeConstant applyUniFunc(Function<NodeNumber, NodeConstant> func);

    @Override
    public abstract boolean equals(Object object);

    @Override
    public abstract String toString();

}
