package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.parser.operators.Determinable;

public abstract class NodeNumber extends NodeConstant implements Cloneable
{
	@Override
	public NodeConstant applyDeterminable(Determinable deter)
	{
		return deter.getResult(this);
	}

	@Override
	public abstract NodeNumber clone();
}
