package uk.co.ryanharrison.mathengine.parser.operators.unary;

import java.util.HashSet;
import java.util.Set;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public abstract class VectorOperator extends UnaryOperator
{
	protected static final String INFINITE_ARG_LENGTH_EXPECTED_USAGE = "elements";
	
	protected static final int INFINITE_ARGUMENT_LENGTH = 1;
	
	protected Set<Integer> acceptedArgumentLengths;

	public VectorOperator()
	{
		acceptedArgumentLengths = new HashSet<Integer>();
		fillAcceptedArguments();
	}
	
	protected abstract NodeConstant calculateResultFromVector(NodeVector arg1);

	protected abstract String getExpectedArgumentsString();

	protected abstract void fillAcceptedArguments();

	@Override
	public final NodeConstant toResult(NodeConstant arg1)
	{
		NodeVector vector = arg1.getTransformer().toNodeVector();
		int length = vector.getSize();

		if (!isValidArgumentLength(length))
		{
			String message = String
					.format("Wrong number of arguments '%d' to operator: %s. Expected '%s(%s)'",
							length, toString(), toString(),
							getExpectedArgumentsString());

			throw new IllegalArgumentException(message);
		}

		// first should check valid number of arguments
		NodeConstant result = calculateResultFromVector(vector);

		// If the result contains one element we may have to convert the result
		// to a number if the argument passed in was a number
		if (result.getTransformer().toNodeVector().getSize() == 1)
		{
			// If the argument was a number return the result as a number
			if (arg1.getTransformer().toNodeNumber() == arg1)
			{
				return result.getTransformer().toNodeNumber();
			}
		}
		return result;
	}

	private boolean isValidArgumentLength(int length)
	{
		if(acceptedArgumentLengths.contains(INFINITE_ARGUMENT_LENGTH))
			return true;
		
		return acceptedArgumentLengths.contains(length);
	}
}
