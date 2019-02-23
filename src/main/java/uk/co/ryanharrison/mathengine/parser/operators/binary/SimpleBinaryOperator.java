package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.function.BiFunction;

public abstract class SimpleBinaryOperator extends BinaryOperator {

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        BiFunction<NodeNumber, NodeNumber, NodeConstant> biFunc = getBiFunc();

        if (arg2 instanceof NodeNumber) {
            return arg1.applyDeterminable(elem -> biFunc.apply(elem.getTransformer().toNodeNumber(),
                    arg2.getTransformer().toNodeNumber()));
        } else if (arg2 instanceof NodeMatrix) {
            return arg1.getTransformer().toNodeMatrix()
                    .appyBiFunc(arg2.getTransformer().toNodeMatrix(), biFunc);
        } else {
            // marshal to vector
            return arg1.getTransformer().toNodeVector()
                    .appyBiFunc(arg2.getTransformer().toNodeVector(), biFunc);
        }
    }

    protected abstract BiFunction<NodeNumber, NodeNumber, NodeConstant> getBiFunc();
}
