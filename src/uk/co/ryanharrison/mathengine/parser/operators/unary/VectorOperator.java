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
		if (arg1 instanceof NodeVector)
			return calculateResultFromVector((NodeVector) arg1);
		throw new UnsupportedOperationException("Not implemented");
	}
}
