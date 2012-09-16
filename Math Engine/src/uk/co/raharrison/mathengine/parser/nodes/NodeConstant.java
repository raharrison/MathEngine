package uk.co.raharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements NodeArithmetic,
		Comparable<NodeConstant>, Appliable
{
	@Override
	public NodeConstant add(NodeConstant arg2)
	{
		if (arg2 instanceof NodeVector)
			return add((NodeVector) arg2);
		if (arg2 instanceof NodeMatrix)
			return add((NodeMatrix) arg2);
		if (arg2 instanceof NodePercent)
			return add((NodePercent) arg2);
		if (arg2 instanceof NodeNumber)
			return add((NodeNumber) arg2);

		throwArithmeticException("add", arg2);
		return null; // unreachable
	}

	@Override
	public NodeConstant divide(NodeConstant arg2)
	{
		if (arg2 instanceof NodeVector)
			return divide((NodeVector) arg2);
		if (arg2 instanceof NodeMatrix)
			return divide((NodeMatrix) arg2);
		if (arg2 instanceof NodePercent)
			return divide((NodePercent) arg2);
		if (arg2 instanceof NodeNumber)
			return divide((NodeNumber) arg2);

		throwArithmeticException("divide", arg2);
		return null; // unreachable
	}

	public abstract double doubleValue();

	@Override
	public abstract boolean equals(Object object);

	@Override
	public NodeConstant multiply(NodeConstant arg2)
	{
		if (arg2 instanceof NodeVector)
			return multiply((NodeVector) arg2);
		if (arg2 instanceof NodeMatrix)
			return multiply((NodeMatrix) arg2);
		if (arg2 instanceof NodePercent)
			return multiply((NodePercent) arg2);
		if (arg2 instanceof NodeNumber)
			return multiply((NodeNumber) arg2);

		throwArithmeticException("multiply", arg2);
		return null; // unreachable
	}

	@Override
	public NodeConstant pow(NodeConstant arg2)
	{
		if (arg2 instanceof NodeVector)
			return pow((NodeVector) arg2);
		if (arg2 instanceof NodeMatrix)
			return pow((NodeMatrix) arg2);
		if (arg2 instanceof NodePercent)
			return pow((NodePercent) arg2);
		if (arg2 instanceof NodeNumber)
			return pow((NodeNumber) arg2);

		throwArithmeticException("raise", arg2);
		return null; // unreachable
	}

	@Override
	public NodeConstant subtract(NodeConstant arg2)
	{
		if (arg2 instanceof NodeVector)
			return subtract((NodeVector) arg2);
		if (arg2 instanceof NodeMatrix)
			return subtract((NodeMatrix) arg2);
		if (arg2 instanceof NodePercent)
			return subtract((NodePercent) arg2);
		if (arg2 instanceof NodeNumber)
			return subtract((NodeNumber) arg2);

		throwArithmeticException("subtract", arg2);
		return null; // unreachable
	}

	private void throwArithmeticException(String command, NodeConstant arg2)
			throws RuntimeException
	{
		throw new RuntimeException(String.format("Cannot %s %s and %s", command, toTypeString(),
				arg2.toTypeString()));
	}

	@Override
	public abstract String toString();

	public abstract String toTypeString();
}
