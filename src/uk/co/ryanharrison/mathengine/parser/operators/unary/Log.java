package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;

public class Log extends VectorOperator
{
	@Override
	protected NodeConstant calculateResultFromVector(NodeVector arg1)
	{
		Node[] values = arg1.getValues();
		NodeConstant[] results = new NodeConstant[arg1.getSize()];
		for (int i = 0; i < arg1.getSize(); i++)
		{
			NodeVector vector = values[i].getTransformer().toNodeVector();
			if(vector.getSize() == 1)
			{
				results[i] = new NodeDouble(Math.log10(vector.getTransformer().toNodeNumber().doubleValue()));
			}
			else if(vector.getSize() == 2)
			{
				double argument = vector.getValues()[0].getTransformer().toNodeNumber().doubleValue();
				double base = vector.getValues()[1].getTransformer().toNodeNumber().doubleValue();
				results[i] = new NodeDouble(Math.log10(base) / Math.log10(argument));
			}
			else
			{
				throw new IllegalArgumentException("Matrix must have two columns {base, value}");
			}
		}
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
		return new String[] { "log", "logarithm" };
	}
	
	@Override
	protected String getExpectedArgumentsString()
	{
		return "base, value";
	}

	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public String toLongString()
	{
		return "logarithm";
	}

	@Override
	public String toString()
	{
		return "log";
	}
}
