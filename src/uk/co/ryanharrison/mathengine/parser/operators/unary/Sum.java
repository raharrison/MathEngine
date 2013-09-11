package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;

public final class Sum extends VectorOperator
{
	@Override
	protected NodeConstant calculateResultFromVector(NodeVector arg1)
	{
		return arg1.toVector().sum();
	}
	
	@Override
	protected void fillAcceptedArguments()
	{
		acceptedArgumentLengths.add(INFINITE_ARGUMENT_LENGTH);
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "sum", "sumnation" };
	}
	
	@Override
	protected String getExpectedArgumentsString()
	{
		return INFINITE_ARG_LENGTH_EXPECTED_USAGE;
	}

	@Override
	public int getPrecedence()
	{
		return 2;
	}

	@Override
	public String toLongString()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		return "sum";
	}
}