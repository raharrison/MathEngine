package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.EvaluationContext;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

public class Select extends BinaryOperator {

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        if (!(arg1 instanceof NodeVector))
            throw new IllegalArgumentException("First argument must be a vector");

        if (!(arg2 instanceof NodeFunction))
            throw new IllegalArgumentException("Second argument must be a selector function with one argument");

        NodeVector vector = (NodeVector) arg1;
        NodeFunction selector = (NodeFunction) arg2;

        if (selector.getVariables().length != 1)
            throw new IllegalArgumentException("Selector function must have one argument");

        EvaluationContext context = getEvaluationContext();
        NodeConstant[] results = new NodeConstant[vector.getSize()];
        NodeConstant[] vecVals = vector.toNodeConstants();

        for (int i = 0; i < results.length; i++) {
            results[i] = context.evaluateFunc(selector.getNode(), selector.createArgsFrom(vecVals[i]));
        }

        return new NodeVector(results);
    }

    @Override
    public String[] getAliases() {
        return new String[]{"select"};
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public String toLongString() {
        return toString();
    }

    @Override
    public String toString() {
        return "select";
    }

}
