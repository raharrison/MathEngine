package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

import java.util.function.BiFunction;

public class Subtract extends SimpleBinaryOperator {
    @Override
    public String[] getAliases() {
        return new String[]{"-", "minus", "sub", "subtract"};
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public String toLongString() {
        return "subtract";
    }

    @Override
    protected BiFunction<NodeNumber, NodeNumber, NodeConstant> getBiFunc() {
        return NodeNumber::subtract;
    }

    @Override
    public String toString() {
        return "-";
    }
}
