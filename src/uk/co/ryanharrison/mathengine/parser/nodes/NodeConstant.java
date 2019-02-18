package uk.co.ryanharrison.mathengine.parser.nodes;

public abstract class NodeConstant extends Node implements NodeArithmetic,
		Comparable<NodeConstant>, Appliable
{
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
	public abstract String toString();

	public abstract String toTypeString();
}
