package uk.co.ryanharrison.mathengine.parser.nodes;

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
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).add(arg2.toMatrix()));
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
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).add(arg2.toVector()));
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
			return Double.compare(this.doubleValue(), cons.getTransformer().toNodeNumber().doubleValue());

		// negate as switching the comparator
		return -cons.compareTo(this);
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).divide(arg2.toMatrix()));
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
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).divide(arg2.toVector()));
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
			return Double.compare(((NodeConstant) object).getTransformer().toNodeNumber().doubleValue(), this.doubleValue()) == 0;
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
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).multiply(arg2.toMatrix()));
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
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).multiply(arg2.toVector()));
	}

	@Override
	public NodeConstant pow(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).pow(arg2.toMatrix()));
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
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).pow(arg2.toVector()));
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).subtract(arg2.toMatrix()));
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
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).subtract(arg2.toVector()));
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
