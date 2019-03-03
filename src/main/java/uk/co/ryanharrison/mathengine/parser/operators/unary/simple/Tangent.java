package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.operators.unary.TrigOperator;

public class Tangent extends TrigOperator {

    @Override
    public String[] getAliases() {
        return new String[]{"tan", "tangent"};
    }

    @Override
    public NodeConstant getResult(double num) {
        double result = Math.tan(num);
        return NodeFactory.createNodeNumberFrom(result);
    }

    @Override
    public String toLongString() {
        return "tangent";
    }

    @Override
    public String toString() {
        return "tan";
    }
}
