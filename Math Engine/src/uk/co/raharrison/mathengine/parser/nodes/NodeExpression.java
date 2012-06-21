package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;

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
}
