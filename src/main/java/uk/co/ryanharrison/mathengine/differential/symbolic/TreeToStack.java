package uk.co.ryanharrison.mathengine.differential.symbolic;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeVariable;
import uk.co.ryanharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.ryanharrison.mathengine.parser.operators.UnaryOperator;

import java.util.ArrayDeque;
import java.util.Deque;

class TreeToStack
{
	public static Deque<ExpressionItem> treeToStack(Node tree)
	{
		Deque<ExpressionItem> stack = new ArrayDeque<ExpressionItem>();
		convert(tree, stack);
		return stack;
	}

	private static void convert(Node tree, Deque<ExpressionItem> stack)
	{
		if (tree instanceof NodeNumber)
			stack.add(new ExpressionItem(tree.toString()));
		else if (tree instanceof NodeVariable)
			stack.add(new ExpressionItem(tree.toString()));
		else if (tree instanceof NodeExpression)
		{
			NodeExpression expression = (NodeExpression) tree;
			ExpressionItem item = new ExpressionItem(tree.toString());
			if (expression.getOperator() instanceof BinaryOperator)
			{
				item.operator = expression.getOperator().toString().charAt(0);
				stack.add(item);
				if (expression.getArgOne() != null)
					convert(expression.getArgOne(), stack);
				if (expression.getArgTwo() != null)
					convert(expression.getArgTwo(), stack);
			}
			else if (expression.getOperator() instanceof UnaryOperator)
			{
				item.function = expression.getOperator().toString();
				stack.add(item);
			}
		}
		else
		{
			throw new UnsupportedOperationException("Unsupported node "
					+ tree.getClass().getCanonicalName());
		}
	}
}
