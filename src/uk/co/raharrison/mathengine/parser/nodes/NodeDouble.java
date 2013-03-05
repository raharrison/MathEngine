package uk.co.raharrison.mathengine.parser.nodes;

public class NodeDouble extends NodeNumber
{
	private double value;

	public NodeDouble(double value)
	{
		this.value = value;
	}

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).add(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		return new NodeDouble(doubleValue() + arg2.doubleValue());
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		return new NodeDouble(doubleValue() * (1 + arg2.doubleValue()));
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).add(arg2.toNodeVector()));
	}

	@Override
	public NodeDouble clone()
	{
		return new NodeDouble(value);
	}

	@Override
	public int compareTo(NodeConstant cons)
	{
		if (cons instanceof NodeDouble)
			return Double.compare(this.doubleValue(), cons.doubleValue());

		// negate as switching the comparator
		return -cons.compareTo(this);
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).divide(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		return new NodeDouble(doubleValue() / arg2.doubleValue());
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		return new NodeDouble(doubleValue() / (arg2.doubleValue()));
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).divide(arg2.toNodeVector()));
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeDouble)
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

	@Override
	public NodeConstant multiply(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).multiply(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		return new NodeDouble(doubleValue() * arg2.doubleValue());
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		return new NodeDouble(doubleValue() * (arg2.doubleValue()));
	}

	@Override
	public NodeConstant multiply(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).multiply(arg2.toNodeVector()));
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).pow(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant pow(NodeNumber arg2)
	{
		return new NodeDouble(Math.pow(doubleValue(), arg2.doubleValue()));
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		return new NodeDouble(Math.pow(doubleValue(), arg2.doubleValue()));
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).pow(arg2.toNodeVector()));
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).subtract(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		return new NodeDouble(doubleValue() - arg2.doubleValue());
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		return new NodeDouble(doubleValue() * (1 - arg2.doubleValue()));
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).subtract(arg2.toNodeVector()));
	}

	@Override
	public NodeNumber toNodeNumber()
	{
		return this;
	}

	@Override
	public String toString()
	{
		return Double.toString(value);
	}

	@Override
	public String toTypeString()
	{
		return "number";
	}
}
