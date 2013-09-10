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
	
	public abstract double doubleValue();

	@Override
	public NodeTransformer getTransformer()
	{
		if (this.transformer == null)
			this.transformer = new NodeNumberTransformer();

		return this.transformer;
	}

	private class NodeNumberTransformer implements NodeTransformer
	{

		@Override
		public NodeVector toNodeVector()
		{
			return new NodeVector(new Node[] { toNodeNumber() });
		}

		@Override
		public NodeNumber toNodeNumber()
		{
			return NodeNumber.this;
		}
	}
}
