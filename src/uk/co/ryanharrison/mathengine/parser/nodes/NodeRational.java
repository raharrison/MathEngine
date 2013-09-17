package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.BigRational;

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
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).add(arg2.toMatrix()));
	}

	@Override
	public NodeConstant add(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.add(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() + arg2.doubleValue());
	}

	@Override
	public NodeConstant add(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).add(arg2);
	}

	@Override
	public NodeConstant add(NodeVector arg2)
	{
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).add(arg2.toVector()));
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
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).divide(arg2.toMatrix()));
	}

	@Override
	public NodeConstant divide(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.divide(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() / arg2.doubleValue());
	}

	@Override
	public NodeConstant divide(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).divide(arg2);
	}

	@Override
	public NodeConstant divide(NodeVector arg2)
	{
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).divide(arg2.toVector()));
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
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).multiply(arg2.toMatrix()));
	}

	@Override
	public NodeConstant multiply(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.multiply(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() * arg2.doubleValue());
	}

	@Override
	public NodeConstant multiply(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).multiply(arg2);
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
		double exp = arg2.doubleValue();
		if (arg2 instanceof NodeRational && exp % 1.0 == 0)
			return new NodeRational(value.pow((long) exp));
		else
			return new NodeDouble(Math.pow(doubleValue(), exp));

	}

	@Override
	public NodeConstant pow(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).pow(arg2);
	}

	@Override
	public NodeConstant pow(NodeVector arg2)
	{
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).pow(arg2.toVector()));
	}

	@Override
	public NodeConstant subtract(NodeMatrix arg2)
	{
		return new NodeMatrix(new Matrix(getTransformer().toNodeNumber()).subtract(arg2.toMatrix()));
	}

	@Override
	public NodeConstant subtract(NodeNumber arg2)
	{
		if (arg2 instanceof NodeRational)
			return new NodeRational(value.subtract(((NodeRational) arg2).value));
		else
			return new NodeDouble(doubleValue() - arg2.doubleValue());
	}

	@Override
	public NodeConstant subtract(NodePercent arg2)
	{
		return new NodeDouble(doubleValue()).subtract(arg2);
	}

	@Override
	public NodeConstant subtract(NodeVector arg2)
	{
		return new NodeVector(new Vector(getTransformer().toNodeNumber()).subtract(arg2.toVector()));
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
