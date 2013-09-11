package uk.co.ryanharrison.mathengine.parser.operators.unary;

import java.util.Arrays;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;

public class Sort extends VectorOperator
{
	@Override
	protected NodeConstant calculateResultFromVector(NodeVector arg1)
	{
		Node[] elements = ((NodeVector) arg1).getValues();

		Node[] results = elements.clone();
		Arrays.sort(results);
		return new NodeVector(results);
	}
	
	@Override
	protected void fillAcceptedArguments()
	{
		acceptedArgumentLengths.add(INFINITE_ARGUMENT_LENGTH);
	}
	
	@Override
	public String[] getAliases()
	{
		return new String[] { "sort", "arrange" };
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
		return "sort";
	}
}
