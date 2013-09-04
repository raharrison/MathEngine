package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements NodeArithmetic, NodeTransformer,
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

		throw generateArithmeticException("add", arg2);
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

		throw generateArithmeticException("divide", arg2);
	}

	public abstract double doubleValue();

	@Override
	public abstract boolean equals(Object object);

	private RuntimeException generateArithmeticException(String command, NodeConstant arg2)
	{
		return new RuntimeException(String.format("Cannot %s %s and %s", command, toTypeString(),
				arg2.toTypeString()));
	}

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

		throw generateArithmeticException("multiply", arg2);
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

		throw generateArithmeticException("raise", arg2);
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

		throw generateArithmeticException("subtract", arg2);
	}

	@Override
	public abstract String toString();

	public abstract String toTypeString();
}
