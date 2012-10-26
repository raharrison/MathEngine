package uk.co.raharrison.mathengine.parser.nodes;

public class NodeUnit extends NodeDouble
{
	private Node temp;
	
	private String unit;

	public NodeUnit(String unit)
	{
		this(unit, 0.0);
	}

	public NodeUnit(String unit, double value)
	{
		super(value);
		this.unit = unit;
	}

	public NodeUnit(String unit, Node val)
	{
		this(unit);
		this.temp = val;
	}

	public String getUnit()
	{
		return unit;
	}
	
	public Node getTemp()
	{
		return temp;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeUnit)
		{
			NodeUnit node = ((NodeUnit) object);
			return unit.equals(node.getUnit()) && doubleValue() == node.doubleValue();
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		return this;
	}

	@Override
	public String toString()
	{
		if(doubleValue() == 0)
			return getUnit();
		
		return Double.toString(doubleValue()) + " " + getUnit();
	}

	@Override
	public NodeDouble clone()
	{
		return new NodeUnit(unit, doubleValue());
	}

	@Override
	public String toTypeString()
	{
		return "unit";
	}
}
