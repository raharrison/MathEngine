package uk.co.raharrison.mathengine.parser.nodes;

public final class NodePercent extends NodeDouble
{
	public NodePercent(double value)
	{
		super(value);
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		return new NodePercent(doubleValue() * 100.0 + arg2.doubleValue() * 100);
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		return new NodePercent((doubleValue() * 100.0) / (arg2.doubleValue() * 100));
	}
	
	@Override
	public double doubleValue()
	{
		return super.doubleValue() / 100.0;
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		return new NodePercent((doubleValue() * 100.0) * (arg2.doubleValue() * 100));
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		return new NodePercent(Math.pow((doubleValue() * 100.0), (arg2.doubleValue() * 100)));
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		return new NodePercent((doubleValue() * 100.0) - (arg2.doubleValue() * 100));
	}

	@Override
	public String toString()
	{
		return super.toString() + "%";
	}

	@Override
	public String toTypeString()
	{
		return "percentage";
	}
}
