package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.function.BiFunction;

public class Subtract extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "-", "minus", "sub", "subtract" };
	}

	@Override
	public int getPrecedence()
	{
		return 6;
	}

	@Override
	public String toLongString()
	{
		return "subtract";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		BiFunction<NodeNumber, NodeNumber, NodeConstant> subber = NodeNumber::subtract;

		if(arg2 instanceof NodeNumber) {
			return arg1.applyDeterminable(elem -> subber.apply(elem.getTransformer().toNodeNumber(),
					arg2.getTransformer().toNodeNumber()));
		} else {
			// marshal to vector
			return new NodeVector(arg1.getTransformer().toNodeVector().toVector()
					.appyBiFunc(arg2.getTransformer().toNodeVector().toVector(), subber));
		}
	}

	@Override
	public String toString()
	{
		return "-";
	}
}
