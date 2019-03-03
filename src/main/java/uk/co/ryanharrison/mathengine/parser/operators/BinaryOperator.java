package uk.co.ryanharrison.mathengine.parser.operators;

import uk.co.ryanharrison.mathengine.parser.EvaluationContext;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;

public abstract class BinaryOperator extends Operator {

    protected abstract NodeConstant toResult(NodeConstant arg1, NodeConstant arg2);

    public NodeConstant toResult(EvaluationContext context, NodeConstant arg1, NodeConstant arg2) {
        this.evaluationContext = context;
        return toResult(arg1, arg2);
    }
}
