package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.MathUtils;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

import java.util.function.Function;

public class Factorial extends NumberOperator {

    @Override
    public String[] getAliases() {
        return new String[]{"!", "factorial", "fac"};
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    protected Function<NodeNumber, NodeConstant> getFunc() {
        return (num -> NodeFactory.createNodeNumberFrom(MathUtils.factorial(num.doubleValue())));
    }

    @Override
    public String toLongString() {
        return "factorial";
    }

    @Override
    public String toString() {
        return "!";
    }
}
