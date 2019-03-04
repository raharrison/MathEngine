package uk.co.ryanharrison.mathengine.parser.operators;

import uk.co.ryanharrison.mathengine.parser.EvaluationContext;
import uk.co.ryanharrison.mathengine.parser.nodes.*;

import java.util.HashMap;
import java.util.Map;

public class CustomOperator extends UnaryOperator {

    private NodeFunction function;

    public CustomOperator(NodeFunction function) {
        this.function = function;
    }

    @Override
    public String[] getAliases() {
        return new String[]{toString()};
    }

    public NodeFunction getFunction() {
        return this.function;
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
    public NodeConstant toResult(NodeConstant arg1) {
        int argNum = function.getArgNum();
        EvaluationContext context = getEvaluationContext();
        if (arg1 instanceof NodeNumber) {
            if (argNum == 1) {
                Map<String, NodeConstant> args = Map.of(function.getVariables()[0], arg1);
                return context.evaluateFunc(function.getNode(), args);
            }
        } else if (arg1 instanceof NodeVector) {
            NodeVector vector = (NodeVector) arg1;
            if (vector.getSize() == argNum) {

                Node[] nodes = vector.getValues();
                String[] vars = function.getVariables();
                Map<String, NodeConstant> args = new HashMap<>();
                for (int i = 0; i < vars.length; i++) {
                    args.put(vars[i], (NodeConstant) nodes[i]);
                }

                return context.evaluateFunc(function.getNode(), args);
            }
        }

        throw new IllegalArgumentException("Expected " + argNum + " arguments");
    }

    @Override
    public String toString() {
        return function.getIdentifier();
    }
}
