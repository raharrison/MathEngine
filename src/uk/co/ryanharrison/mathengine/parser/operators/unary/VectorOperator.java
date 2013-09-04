package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVector;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public abstract class VectorOperator extends UnaryOperator
{
	protected abstract NodeConstant calculateResultFromVector(NodeVector arg1);

	@Override
	public final NodeConstant toResult(NodeConstant arg1)
	{
		// first should check valid number of arguments
		NodeConstant result = calculateResultFromVector(arg1.getTransformer().toNodeVector());
		
		// If the result contains one element we may have to convert the result to a number if the argument passed in was a number
		if(result.getTransformer().toNodeVector().getSize() == 1)
		{
			// If the argument was a number return the result as a number
			if(arg1.getTransformer().toNodeNumber() == arg1)
			{
				return result.getTransformer().toNodeNumber();
			}
		}
		return result;
	}
}
