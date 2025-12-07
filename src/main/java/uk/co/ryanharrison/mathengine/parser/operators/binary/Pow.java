package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

import java.util.function.BiFunction;

public class Pow extends SimpleBinaryOperator {
    @Override
    public String[] getAliases() {
        return new String[]{"^", "pow", "power", "powerof", "powof", "topowerof", "topowof"};
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public String toLongString() {
        return "power";
    }

    @Override
    protected BiFunction<NodeNumber, NodeNumber, NodeConstant> getBiFunc() {
        return NodeNumber::pow;
    }

    @Override
    public String toString() {
        return "^";
    }
}
