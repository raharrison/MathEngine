package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.function.BiFunction;

public class Pow extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "^", "pow", "power", "powerof", "powof", "topowerof", "topowof" };
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return "power";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		BiFunction<NodeNumber, NodeNumber, NodeConstant> pow = NodeNumber::pow;

		if(arg2 instanceof NodeNumber) {
			return arg1.applyDeterminable(elem -> pow.apply(elem.getTransformer().toNodeNumber(),
					arg2.getTransformer().toNodeNumber()));
		} else {
			// marshal to vector
			return new NodeVector(arg1.getTransformer().toNodeVector().toVector()
					.appyBiFunc(arg2.getTransformer().toNodeVector().toVector(), pow));
		}
	}

	@Override
	public String toString()
	{
		return "^";
	}
}
