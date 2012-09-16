package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.BigRational;

public final class NodeRational extends NodeNumber
{
	private BigRational value;
	private static final int maxIterations = 150;
	private static final double epsilon = 1E-15;

	public NodeRational(BigRational rational)
	{
		this.value = rational;
	}

	public NodeRational(double value)
	{
		this.value = new BigRational(value, epsilon, maxIterations);
	}

	public NodeRational(int numerator, int denominator)
	{
		this.value = new BigRational(numerator, denominator);
	}

	@Override
	public NodeConstant add(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).add(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.add(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() + value.doubleValue());
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).add(arg2);
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).add(arg2.toNodeVector()));
	}

	@Override
	public NodeNumber clone()
	{
		return new NodeRational((BigRational) value.clone());
	}

	@Override
	public int compareTo(NodeConstant o)
	{
		if (o instanceof NodeRational)
		{
			return value.compareTo(((NodeRational) o).getValue());
		}
		else
		{
			return new NodeDouble(doubleValue()).compareTo(o);
		}
	}

	@Override
	public NodeConstant divide(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).divide(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.divide(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() / value.doubleValue());
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).divide(arg2);
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).divide(arg2.toNodeVector()));
	}

	@Override
	public double doubleValue()
	{
		return value.doubleValue();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeRational)
		{
			return value.equals(((NodeRational) object).getValue());
		}

		return false;
	}

	public BigRational getValue()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	@Override
	public NodeConstant multiply(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).multiply(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.multiply(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() * value.doubleValue());
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).multiply(arg2);
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
		return new NodeDouble(Math.pow(doubleValue(), (arg2).doubleValue()));
	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).pow(arg2);
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		return new NodeVector(new Vector(toNodeNumber()).pow(arg2.toNodeVector()));
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(toNodeNumber()).subtract(arg2.toNodeMatrix()));
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.subtract(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() - value.doubleValue());
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).subtract(arg2);
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
		return value.toString();
	}

	@Override
	public String toTypeString()
	{
		return "rational";
	}
}
