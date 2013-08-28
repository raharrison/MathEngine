package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public class If extends UnaryOperator
{
	@Override
	public NodeConstant toResult(NodeConstant arg1)
	{
		if(arg1 instanceof NodeVector)
		{
			NodeVector args = (NodeVector) arg1;
			
			if(args.getSize() != 3)
				throw new IllegalArgumentException("Expected three arguments to operator if");
			
			if(args.getValues()[0].toNodeNumber().doubleValue() == 1.0)
				return (NodeConstant) args.getValues()[1];
			else
				return (NodeConstant) args.getValues()[2];
		}
		else
			throw new IllegalArgumentException("Expected three arguments to operator if");
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "if" };
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
	public String toString()
	{
		return "if";
	}
}
