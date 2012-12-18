package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;

public class NodeUnit extends NodeDouble
{
	private Node value;
	private SubUnit unit;
	private boolean hasValue;

	public NodeUnit(SubUnit unit)
	{
		this(unit, NodeFactory.createNodeNumberFrom(1.0), false);
	}

	public NodeUnit(SubUnit unit, Node val)
	{
		this(unit, val, true);
	}

	public NodeUnit(SubUnit unit, Node val, boolean hasValue)
	{
		super(1.0);
		this.unit = unit;
		this.value = val;
		this.hasValue = hasValue;
	}

	@Override
	public NodeDouble clone()
	{
		return new NodeUnit(unit, value);
	}

	@Override
	public double doubleValue()
	{
		return value.toNodeNumber().doubleValue();
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

	public SubUnit getUnit()
	{
		return unit;
	}

	public Node getValue()
	{
		return value;
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
		return value.toNodeNumber();
	}

	@Override
	public String toString()
	{
		if (hasValue)
		{
			if (doubleValue() == 1)
				return getUnit().getBaseAliasSingular();

			if (hasValue)
				return Double.toString(doubleValue()) + " " + getUnit().getBaseAliasPlural();
		}

		return getUnit().getBaseAliasPlural();
	}

	@Override
	public String toTypeString()
	{
		return "unit";
	}
}
