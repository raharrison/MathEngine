package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeMatrix;

public class Determinant extends MatrixOperator {

    @Override
    protected NodeConstant calculateResultFromMatrix(NodeMatrix arg1) {
        return NodeFactory.createNodeNumberFrom(arg1.toDoubleMatrix()
                .determinant());
    }

    @Override
    public String[] getAliases() {
        return new String[]{"determinant", "determ"};
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
        return "determinant";
    }

}
