package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeUnit;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionEngine;
import uk.co.ryanharrison.mathengine.unitconversion.ConversionResult;

public class Convert extends BinaryOperator {
    private static final ConversionEngine ENGINE = ConversionEngine.loadDefaults();

    @Override
    public String[] getAliases() {
        return new String[]{"in", "to", "as", "convert"};
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public String toLongString() {
        return "conversion";
    }

    @Override
    public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
        if (arg1 instanceof NodeUnit unit1 && arg2 instanceof NodeUnit unit2) {
            ConversionResult result = ENGINE.convert(
                    unit1.doubleValue(),
                    unit1.getUnit().getSingular(),
                    unit2.getUnit().getSingular()
            );

            NodeNumber value = NodeFactory.createNodeNumberFrom(result.result().doubleValue());
            return new NodeUnit(result.toUnit(), value);
        }

        throw new IllegalArgumentException("Incorrect conversion parameters");
    }

    @Override
    public String toString() {
        return "in";
    }
}
