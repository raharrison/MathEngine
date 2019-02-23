package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.Operator;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

public final class NodeExpression extends Node
{
	private Operator operator;

	private Node argOne;
	private Node argTwo;

	public NodeExpression(Operator operator, Node arg1)
	{
		this(operator, arg1, null);
	}

	public NodeExpression(Operator operator, Node arg1, Node arg2)
	{
		this.operator = operator;

		if (operator instanceof BinaryOperator && arg2 == null)
		{
			throw new IllegalArgumentException("Wrong number of arguments to operator "
					+ operator.toString());
		}
		else
		{
			this.argOne = arg1;
			this.argTwo = arg2;
		}
	}

	public Node getArgOne()
	{
		return argOne;
	}

	public Node getArgTwo()
	{
		return argTwo;
	}

	public Operator getOperator()
	{
		return operator;
	}

	@Override
	public int hashCode()
	{
		return argOne.hashCode() + argTwo.hashCode() + operator.hashCode();
	}

	@Override
	public String toString()
	{
		if (operator == null)
			return "";

		
		if (operator instanceof UnaryOperator)
		{
			String argOneOut = argOne instanceof NodeMatrix ? ((NodeMatrix) argOne).toShortString()
					: argOne.toString();
			return String.format("%s(%s)", operator.toString(), argOneOut);
		}
		else if (operator instanceof BinaryOperator)
		{
			String argOneOut = argOne instanceof NodeMatrix ? ((NodeMatrix) argOne).toShortString()
					: argOne.toString();
			String argTwoOut = argTwo instanceof NodeMatrix ? ((NodeMatrix) argTwo).toShortString()
					: argTwo.toString();
			return String.format("(%s %s %s)", argOneOut, operator.toString(), argTwoOut);
		}

		return "";
	}
	
	@Override
	public NodeTransformer getTransformer()
	{
		if (this.transformer == null)
			this.transformer = new NodeNumberTransformer();

		return this.transformer;
	}

	@Override
	public Node copy() {
		return new NodeExpression(operator, argOne.copy(), argTwo.copy());
	}

	private class NodeNumberTransformer implements NodeTransformer
	{

		@Override
		public NodeVector toNodeVector()
		{
			return new NodeVector(new Node[] { toNodeNumber() });
		}

		@Override
		public NodeMatrix toNodeMatrix() {
			return new NodeMatrix(new Node[][]{{toNodeNumber()}});
		}

		@Override
		public NodeNumber toNodeNumber()
		{
			return argOne.getTransformer().toNodeNumber().add(argTwo.getTransformer().toNodeNumber()).getTransformer().toNodeNumber();
		}
	}
}
