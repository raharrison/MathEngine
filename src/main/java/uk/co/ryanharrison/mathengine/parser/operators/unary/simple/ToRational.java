package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeRational;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

import java.util.function.Function;

public class ToRational extends NumberOperator {
    @Override
    public String[] getAliases() {
        return new String[]{"torational", "rat", "rational", "frac"};
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    protected Function<NodeNumber, NodeConstant> getFunc() {
        return (num -> new NodeRational(num.doubleValue()));
    }

    @Override
    public String toLongString() {
        return toString();
    }

    @Override
    public String toString() {
        return "rational";
    }
}
