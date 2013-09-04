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
		return calculateResultFromVector(arg1.getTransformer().toNodeVector());
	}
}
