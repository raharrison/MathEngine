package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeAddVariable;
import uk.co.raharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeToken;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;
import uk.co.raharrison.mathengine.parser.operators.binary.Add;
import uk.co.raharrison.mathengine.parser.operators.binary.Divide;
import uk.co.raharrison.mathengine.parser.operators.binary.Multiply;
import uk.co.raharrison.mathengine.parser.operators.binary.PercentOf;
import uk.co.raharrison.mathengine.parser.operators.binary.Pow;
import uk.co.raharrison.mathengine.parser.operators.binary.Subtract;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.And;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.Equals;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.GreaterThan;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.GreaterThanEqualTo;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.LessThan;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.LessThanEqualTo;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.NotEquals;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.Or;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.Xor;
import uk.co.raharrison.mathengine.parser.operators.unary.Log;
import uk.co.raharrison.mathengine.parser.operators.unary.Percent;
import uk.co.raharrison.mathengine.parser.operators.unary.Sort;
import uk.co.raharrison.mathengine.parser.operators.unary.Sum;
import uk.co.raharrison.mathengine.parser.operators.unary.ToDouble;
import uk.co.raharrison.mathengine.parser.operators.unary.ToRational;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Cosine;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.DoubleFactorial;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Factorial;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Ln;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Sine;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Tangent;

public final class Parser
{
	private HashMap<String, Operator> operators;
	private int maxoplength;

	public Parser()
	{
		operators = new HashMap<String, Operator>();

		addOperator(new Pow());
		addOperator(new Add());
		addOperator(new Subtract());
		addOperator(new Multiply());
		addOperator(new Divide());

		addOperator(new Sine());
		addOperator(new Cosine());
		addOperator(new Tangent());

		addOperator(new Sum());
		addOperator(new Sort());
		
		addOperator(new PercentOf());
		addOperator(new Percent());
		addOperator(new ToDouble());
		addOperator(new ToRational());
		
		addOperator(new Ln());
		addOperator(new Log());
		addOperator(new Factorial());
		addOperator(new DoubleFactorial());

		addOperator(new LessThan());
		addOperator(new LessThanEqualTo());
		addOperator(new GreaterThan());
		addOperator(new GreaterThanEqualTo());
		addOperator(new Equals());
		addOperator(new NotEquals());
		addOperator(new Or());
		addOperator(new And());
		addOperator(new Xor());
		
		maxoplength = findLongestOperator();
	}

	private void addOperator(Operator op)
	{
		for (String alias : op.getAliases())
		{
			operators.put(alias, op);
		}
	}

	private int findLongestOperator()
	{
		int longest = 0;

		for (Entry<String, Operator> entry : operators.entrySet())
		{
			if (entry.getKey().length() > longest)
				longest = entry.getKey().length();
		}

		return longest;
	}

	private String getArguments(String operator, String exp, int index)
	{
		int ma, i, prec = -1;
		int len = exp.length();
		String op = null;
		StringBuilder str = new StringBuilder();

		i = index;
		ma = 0;

		if (operator == null)
		{
			prec = -1;
		}
		else
		{
			prec = operators.get(operator).getPrecedence();
		}

		while (i < len)
		{
			if (exp.charAt(i) == '(')
			{
				ma = matchParenthesis(exp, i);
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if ((op = getOperator(exp, i)) != null)
			{
				if (str.length() != 0 && operators.get(op).getPrecedence() >= prec)
				{
					return str.toString();
				}
				str.append(op);
				i += op.length();
			}
			else
			{
				str.append(exp.charAt(i));
				i++;
			}
		}

		return str.toString();
	}

	private String getOperator(String expression, int index)
	{
		String tmp;
		int i = 0;
		int len = expression.length();

		for (i = 0; i < maxoplength; i++)
		{
			if (index >= 0 && index + maxoplength - i <= len)
			{
				tmp = expression.substring(index, index + maxoplength - i);
				if (isOperator(tmp))
				{
					return tmp;
				}
			}
		}
		return null;
	}

	private boolean isAllowedSym(char s)
	{
		return s == ',' || s == '.' || s == ')' || s == '(' || s == '>' || s == '<' || s == '&'
				|| s == '=' || s == '|' || s == '[' || s == ']' || s == '{' || s == '}';
	}

	protected boolean isOperator(String operator)
	{
		return operators.containsKey(operator);
	}

	private boolean isTwoArgOp(String operator)
	{
		if (operator == null)
			return false;

		Operator op = operators.get(operator);

		if (op == null)
			return false;

		return op instanceof BinaryOperator;
	}

	private boolean isVariable(String expression)
	{
		if (Utils.isNumeric(expression))
			return false;

		for (int i = 0; i < expression.length(); i++)
		{
			if (getOperator(expression, i) != null)
				return false;
			else if (isAllowedSym(expression.charAt(i)))
				return false;
		}

		return true;
	}

	private int matchParenthesis(String expression, int index)
	{
		int len = expression.length();
		int i = index;
		int count = 0;

		while (i < len)
		{
			if (expression.charAt(i) == '(')
			{
				count++;
			}
			else if (expression.charAt(i) == ')')
			{
				count--;
			}

			if (count == 0)
				return i;

			i++;
		}

		return index;
	}

	public Node parse(String expression)
	{
		Node tree = null;

		String farg = "", sarg = "", fop = "";
		int ma = 0, i = 0;

		int len = expression.length();

		if (len == 0)
		{
			throw new IllegalArgumentException("Wrong number of arguments to operator");
		}
		else if (expression.contains(":="))
		{
			int index = expression.indexOf(":=");

			String variable = expression.substring(0, index);

			String t = expression.substring(index + 2, expression.length());

			Node node = parse(t);
			return new NodeAddVariable(variable, node);
		}
		else if (expression.charAt(0) == '(' && (ma = matchParenthesis(expression, 0)) == len - 1)
		{
			return parse(expression.substring(1, ma));
		}
		if (isVariable(expression))
		{
			return new NodeToken(expression);
		}
		else if (Utils.isNumeric(expression))
		{
			return NodeFactory.createNodeNumberFrom(Double.parseDouble(expression));
		}

		while (i < len)
		{
			if ((fop = getOperator(expression, i)) == null)
			{
				farg = getArguments(null, expression, i);
				fop = getOperator(expression, i + farg.length());

				if (fop == null)
					throw new IllegalArgumentException("Missing operator," + " expression is \""
							+ expression + "\"");

				if (isTwoArgOp(fop))
				{
					sarg = getArguments(fop, expression, i + farg.length() + fop.length());
					if (sarg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(fop), parse(farg), parse(sarg));
					i += farg.length() + fop.length() + sarg.length();
				}
				else
				{
					if (farg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(fop), parse(farg));
					i += farg.length() + fop.length();
				}
			}
			else
			{
				if (isTwoArgOp(fop))
				{
					farg = getArguments(fop, expression, i + fop.length());
					if (farg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					if (tree == null)
					{
						if (fop.equals("+") || fop.equals("-"))
						{
							tree = NodeFactory.createNodeNumberFrom(0D);
						}
						else
						{
							throw new IllegalArgumentException(
									"Wrong number of arguments to operator " + fop);
						}
					}
					tree = new NodeExpression(operators.get(fop), tree, parse(farg));
					i += farg.length() + fop.length();
				}
				else
				{
					farg = getArguments(fop, expression, i + fop.length());
					if (farg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(fop), parse(farg));
					i += farg.length() + fop.length();
				}
			}
		}

		return tree;
	}
}
