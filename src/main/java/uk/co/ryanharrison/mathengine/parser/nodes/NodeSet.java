package uk.co.ryanharrison.mathengine.parser.nodes;

import java.util.function.Function;

public interface NodeSet {

    NodeConstant resolve(Function<Node, NodeConstant> func);

}
