package uk.co.raharrison.mathengine.parser.nodes;

public class NodeDouble extends NodeNumber implements NodeMath, Cloneable
{
	private double value;

	public NodeDouble(double value)
	{
		this.value = value;
	}

	@Override
	protected NodeDouble clone()
	{
		return new NodeDouble(value);
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		if (cons instanceof NodeBoolean || cons instanceof NodeDouble)
			return Double.compare(this.doubleValue(), cons.doubleValue());

		// negate as switching the comparator
		return -cons.compareTo(this);
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeBoolean || object instanceof NodeDouble)
		{
			return Double.compare(((NodeConstant) object).doubleValue(), this.doubleValue()) == 0;
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return Double.valueOf(value).hashCode();
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return Double.toString(value);
	}
}
