package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.function.BiFunction;

public class Multiply extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "*", "times", "multiply", "mult" };
	}

	@Override
	public int getPrecedence()
	{
		return 4;
	}

	@Override
	public String toLongString()
	{
		return "multiply";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		if (arg1 instanceof NodeMatrix && arg2 instanceof NodeMatrix)
			return ((NodeMatrix) arg1).multiplyMatrix((NodeMatrix) arg2);

		BiFunction<NodeNumber, NodeNumber, NodeConstant> multiplier = NodeNumber::multiply;

		if (arg2 instanceof NodeNumber) {
			return arg1.applyDeterminable(elem -> multiplier.apply(elem.getTransformer().toNodeNumber(),
					arg2.getTransformer().toNodeNumber()));
		} else {
			// marshal to vector
			return new NodeVector(arg1.getTransformer().toNodeVector().toVector()
					.appyBiFunc(arg2.getTransformer().toNodeVector().toVector(), multiplier));
		}
	}

	@Override
	public String toString()
	{
		return "*";
	}
}
