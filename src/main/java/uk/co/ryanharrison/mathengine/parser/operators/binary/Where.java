package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.EvaluationContext;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.ArrayList;
import java.util.List;

public class Where extends BinaryOperator {

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        if (!(arg1 instanceof NodeVector))
            throw new IllegalArgumentException("First argument must be a vector");

        if (!(arg2 instanceof NodeFunction))
            throw new IllegalArgumentException("Second argument must be a predicate function with one argument");

        NodeVector vector = (NodeVector) arg1;
        NodeFunction predicate = (NodeFunction) arg2;

        if (predicate.getVariables().length != 1)
            throw new IllegalArgumentException("Predicate function must have one argument");

        List<NodeConstant> results = new ArrayList<>();
        NodeConstant[] vecVals = vector.toNodeConstants();
        EvaluationContext context = getEvaluationContext();

        for (NodeConstant vecVal : vecVals) {
            NodeConstant res = context.evaluateFunc(predicate.getNode(), predicate.createArgsFrom(vecVal));

            if (res.getTransformer().toNodeNumber().doubleValue() == 1.0) {
                results.add(vecVal);
            }
        }

        return new NodeVector(results.toArray(new Node[0]));
    }

    @Override
    public String[] getAliases() {
        return new String[]{"where"};
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
        return "where";
    }
}
