package uk.co.ryanharrison.mathengine.parser.operators.binary.logical;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class LessThanEqualTo extends BinaryOperator {
    @Override
    public String[] getAliases() {
        return new String[]{"<=", "lessthanorequalto", "lessthanequal", "lessthanequalto"};
    }

    @Override
    public int getPrecedence() {
        return 7;
    }

    @Override
    public String toLongString() {
        return "less than or equal to";
    }

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        int c = arg1.compareTo(arg2);

        return new NodeBoolean(c <= 0);
    }

    @Override
    public String toString() {
        return "<=";
    }
}
