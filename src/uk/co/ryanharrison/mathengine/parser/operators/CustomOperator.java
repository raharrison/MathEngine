package uk.co.ryanharrison.mathengine.parser.operators;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeFunction;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;

public class CustomOperator extends UnaryOperator
{
	private NodeFunction function;

	public CustomOperator(NodeFunction function)
	{
		this.function = function;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { toString() };
	}

	public NodeFunction getFunction()
	{
		return this.function;
	}

	@Override
	public int getPrecedence()
	{
		return 3;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		int argNum = function.getArgNum();
		if (arg1 instanceof NodeNumber)
		{
			if (argNum == 1)
			{
				return function.evaluate((NodeNumber) arg1);
			}
		}
		else if (arg1 instanceof NodeVector)
		{
			if (((NodeVector) arg1).getSize() == argNum)
			{
				return function.evaluate((NodeVector) arg1);
			}
		}

		throw new IllegalArgumentException("Expected " + argNum + " arguments");
	}

	@Override
	public String toString()
	{
		return function.getIdentifier();
	}
}
