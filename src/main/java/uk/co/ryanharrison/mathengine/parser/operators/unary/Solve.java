package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.solvers.BrentSolver;
import uk.co.ryanharrison.mathengine.utils.MathUtils;

import java.util.List;

public class Solve extends VectorOperator {
    @Override
    protected NodeConstant calculateResultFromVector(NodeVector arg1) {
        Node[] elements = arg1.getValues();

        if (!(elements[0] instanceof NodeFunction))
            throw new IllegalArgumentException(
                    "First argument must be a function");

        var solver = BrentSolver.builder()
                .targetFunction(((NodeFunction) elements[0]).toFunction())
                .iterations(150)
                .lowerBound(-25)
                .upperBound(25);

        if (arg1.getSize() == 3) {
            solver.lowerBound(elements[1].getTransformer().toNodeNumber().doubleValue());
            solver.upperBound(elements[2].getTransformer().toNodeNumber().doubleValue());
        }

        List<Double> roots = solver.build().solveAll();
        Node[] results = new Node[roots.size()];
        for (int i = 0; i < roots.size(); i++) {
            results[i] = new NodeDouble(MathUtils.round(roots.get(i), 5));
        }
        return new NodeVector(results);
    }

    @Override
    protected String getExpectedArgumentsString() {
        return "function, lowerbound, upperbound";
    }

    @Override
    protected void fillAcceptedArguments() {
        acceptedArgumentLengths.add(1);
        acceptedArgumentLengths.add(3);
    }

    @Override
    public String[] getAliases() {
        return new String[]{"solve", "roots"};
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
        return "solve";
    }
}
