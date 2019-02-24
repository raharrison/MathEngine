package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

import java.util.function.Function;

public abstract class NumberOperator extends UnaryOperator {

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public NodeConstant toResult(NodeConstant arg1) {
        return arg1.applyUniFunc(getFunc());
    }

    protected abstract Function<NodeNumber, NodeConstant> getFunc();
}
