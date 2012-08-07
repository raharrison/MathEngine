package uk.co.raharrison.mathengine.parser.nodes;

public class NodePercent extends NodeDouble
{
	public NodePercent(double value)
	{
		super(value);
	}
	
	@Override
	public double doubleValue()
	{
		return super.doubleValue() / 100.0;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "%";
	}
}
