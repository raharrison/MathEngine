package uk.co.ryanharrison.mathengine.parser.operators.unary.simple;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.unary.NumberOperator;

import java.util.function.Function;

public class Not extends NumberOperator {

    @Override
    protected Function<NodeNumber, NodeConstant> getFunc() {
        return (num -> {
            boolean result = num.doubleValue() == 1.0;
            return new NodeBoolean(!result);
        });
    }

    @Override
    public String[] getAliases() {
        return new String[]{"not", "invert"};
    }

    @Override
    public String toLongString() {
        return toString();
    }

    @Override
    public String toString() {
        return "not";
    }
}
