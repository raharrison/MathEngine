package uk.co.ryanharrison.mathengine.parser.operators.binary.logical;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class Or extends BinaryOperator {
    @Override
    public String[] getAliases() {
        return new String[]{"or", "||"};
    }

    @Override
    public int getPrecedence() {
        return 9;
    }

    @Override
    public String toLongString() {
        return toString();
    }

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        if (arg1 instanceof NodeNumber) {
            if (arg2 instanceof NodeNumber) {
                boolean result = arg1.getTransformer().toNodeNumber().doubleValue() == 1 || arg2.getTransformer().toNodeNumber().doubleValue() == 1;

                return new NodeBoolean(result);
            }
        }

        throw new IllegalArgumentException("Must have two logical arguments to operator 'or'");
    }

    @Override
    public String toString() {
        return "or";
    }
}
