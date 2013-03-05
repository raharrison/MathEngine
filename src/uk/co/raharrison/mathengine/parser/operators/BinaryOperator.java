package uk.co.raharrison.mathengine.parser.operators;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;

public abstract class BinaryOperator extends Operator
{
	public abstract NodeConstant toResult(NodeConstant arg1, NodeConstant arg2);
}
