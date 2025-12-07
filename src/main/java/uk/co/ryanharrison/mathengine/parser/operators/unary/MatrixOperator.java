package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public abstract class MatrixOperator extends UnaryOperator {
    protected abstract NodeConstant calculateResultFromMatrix(NodeMatrix arg1);

    @Override
    public NodeConstant toResult(NodeConstant arg1) {
        if (arg1 instanceof NodeMatrix)
            return calculateResultFromMatrix((NodeMatrix) arg1);

        throw new IllegalArgumentException("Argument to operator: " + toString() + " must be a matrix");
    }
}
