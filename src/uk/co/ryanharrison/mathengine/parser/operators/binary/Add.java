package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;

import java.util.function.BiFunction;

public class Add extends BinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "+", "add", "plus", "and" };
	}

	@Override
	public int getPrecedence()
	{
		return 6;
	}

	@Override
	public String toLongString()
	{
		return "add";
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2)
	{
		BiFunction<NodeNumber, NodeNumber, NodeConstant> adder = NodeNumber::add;

		if(arg2 instanceof NodeNumber) {
			return arg1.applyDeterminable(elem -> adder.apply(elem.getTransformer().toNodeNumber(),
					arg2.getTransformer().toNodeNumber()));
		} else {
			// marshal to vector
			return new NodeVector(arg1.getTransformer().toNodeVector().toVector()
					.appyBiFunc(arg2.getTransformer().toNodeVector().toVector(), adder));
		}

		// get rid of Vector
		// apply function to each element of arg1 memoized with arg2
		// function takes in two NodeNumbers and spits out add result
		// NodeNumber has to be able to do stuff

		// if arg2 is a NodeNumber do above
		// else convert arg1 to NodeMultiValue
		// replace node variables with NodeConstant

	}

	@Override
	public String toString()
	{
		return "+";
	}
}
