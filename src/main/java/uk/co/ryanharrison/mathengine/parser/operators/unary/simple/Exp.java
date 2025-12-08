package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

import java.util.function.Function;

public class Exp extends NumberOperator {

    @Override
    public String[] getAliases() {
        return new String[]{"exp", "exponential"};
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    protected Function<NodeNumber, NodeConstant> getFunc() {
        return (num -> NodeFactory.createNodeNumberFrom(Math.exp(num.doubleValue())));
    }

    @Override
    public String toLongString() {
        return "exponent";
    }

    @Override
    public String toString() {
        return "exp";
    }
}
