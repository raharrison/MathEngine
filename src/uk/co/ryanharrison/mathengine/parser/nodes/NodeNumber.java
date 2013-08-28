package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.operators.Determinable;

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
