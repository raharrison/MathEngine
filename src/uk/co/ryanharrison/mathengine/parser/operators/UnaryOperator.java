package uk.co.ryanharrison.mathengine.parser.operators;

import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;

public abstract class UnaryOperator extends Operator
{
	public abstract NodeConstant toResult(NodeConstant arg1);
}
