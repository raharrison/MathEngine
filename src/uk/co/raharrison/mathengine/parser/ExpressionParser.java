package uk.co.raharrison.mathengine.parser;

import java.util.*;
import java.util.Map.Entry;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.nodes.*;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.CustomOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;

public final class ExpressionParser implements Parser<String, Node>
{
	private Map<String, Operator> operators;
	private int maxoplength;
	private Set<String> variables;

	public ExpressionParser()
	{
		operators = new HashMap<String, Operator>();

		maxoplength = findLongestOperator();
	}

	private void addOperator(Operator op)
	{
		for (String alias : op.getAliases())
		{
			operators.put(alias, op);
		}
	}

	public void addVariable(String var)
	{
		if (variables == null)
			variables = new HashSet<String>();

		this.variables.add(var);
	}

	private String backTrack(String str)
	{
		try
		{
			for (int i = 0; i <= this.maxoplength; i++)
			{
				String op;
				if ((op = findOperator(str, (str.length() - 1 - maxoplength + i))) != null
						&& (str.length() - maxoplength - 1 + i + op.length()) == str.length())
				{
					return op;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void fillOperators(List<Operator> operators)
	{
		for (Operator operator : operators)
		{
			addOperator(operator);
		}

		maxoplength = findLongestOperator();
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

	private String findOperator(String expression, int index)
	{
		String tmp;
		int i = 0;
		int len = expression.length();

		for (i = 0; i < maxoplength; i++)
		{
			if (index >= 0 && index + maxoplength - i <= len)
			{
				tmp = expression.substring(index, index + maxoplength - i);
				if (isOperator(tmp, true))
				{
					return tmp;
				}
			}
		}
		return null;
	}

	private Argument getArgumentsDefault(String operator, String exp, int index)
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
			prec = getOperator(operator).getPrecedence();
		}

		while (i < len)
		{
			if (exp.charAt(i) == '(')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '(', ')');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if (exp.charAt(i) == '{')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '{', '}');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if (exp.charAt(i) == '[')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '[', ']');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if ((op = findOperator(exp, i)) != null)
			{
				if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
						&& getOperator(op).getPrecedence() >= prec)
				{
					return new Argument(str.toString(), null, str.length());
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
		
		return new Argument(str.toString(), null, str.length());
	}

	private Argument getArgumentsRecursive(String operator, String exp, int index)
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
			prec = getOperator(operator).getPrecedence();
		}

		while (i < len)
		{
			if (exp.charAt(i) == '(')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '(', ')');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if (exp.charAt(i) == '{')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '{', '}');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if (exp.charAt(i) == '[')
			{
				ma = Utils.matchingCharacterIndex(exp, i, '[', ']');
				str.append(exp.substring(i, ma + 1));
				i = ma + 1;
			}
			else if ((op = findOperator(exp, i)) != null)
			{
				if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
						&& getOperator(op).getPrecedence() >= prec)
				{
					try
					{
						Node n = parseTree(str.toString(), new DefaultArgumentStrategy());
						return new Argument(str.toString(), n, str.toString().length());
					}
					catch (Exception e)
					{
					}
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

		return new Argument(str.toString(), parseTree(str.toString(), new DefaultArgumentStrategy()), str.toString().length());
	}

	private class Argument
	{
		public Argument(String source, Node node, int length)
		{
			this.source = source;
			this.node = node;
			this.length = length;
		}

		public String source;
		public int length;
		public Node node;
	}

	private Operator getOperator(String operator)
	{
		return operators.get(Utils.standardizeString(operator));
	}

	@SuppressWarnings("unused")
	private boolean isMatrix(String str)
	{
		if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']')
			return isVector(str.substring(1, str.length() - 1));

		return false;
	}

	protected boolean isOperator(String operator, boolean ignoreSpaces)
	{
		if (ignoreSpaces)
			operator = Utils.removeSpaces(operator);

		return operators.containsKey(Utils.standardizeString(operator));
	}

	private boolean isTwoArgOp(String operator)
	{
		if (operator == null)
			return false;

		Operator op = getOperator(operator);

		if (op == null)
			return false;

		return op instanceof BinaryOperator;
	}

	private boolean isVariable(String expression)
	{
		if (Utils.isNumeric(expression))
			return false;

		if (variables != null && variables.contains(expression))
			return true;

		for (int i = 0; i < expression.length(); i++)
		{
			if (findOperator(expression, i) != null)
				return false;
			else if (!isAllowedSym(expression.charAt(i)))
				return false;
		}

		// Used to be false
		return true;
	}

	private boolean isAllowedSym(char s)
	{
		return !(s == ')' || s == '(' || s == '}' || s == ']' || s == '[' || s == '{' || s == '.'
				|| s == '>' || s == '<' || s == '&' || s == '=' || s == '|');
	}

	private boolean isVector(String str)
	{
		for (Entry<String, Operator> entry : operators.entrySet())
		{
			if (str.startsWith(entry.getKey()))
				return false;
		}

		return str.matches("([^,(]+,)+([^,)]+)");
	}

	@Override
	public Node parse(String expression)
	{
		int index = expression.indexOf(":=");

		if (index != -1)
		{
			String variable = expression.substring(0, index);
			if (isOperator(variable, false))
				throw new IllegalArgumentException("Variable is an operator");

			String t = expression.substring(index + 2, expression.length()).trim();

			NodeFunction func = NodeFactory
					.createNodeFunctionFrom(variable.trim(), t, parseTree(t, new RecursiveArgumentStrategy()));

			if (func.getArgNum() > 0)
			{
				addOperator(new CustomOperator(func));
				maxoplength = findLongestOperator();
			}
			else
				return new NodeAddVariable(func.getIdentifier(), parseTree(t.trim(), new RecursiveArgumentStrategy()));
			return func;
		}

		ArgumentStrategy strategy = new RecursiveArgumentStrategy();
		return parseTree(expression, strategy);
	}

	private Node parseTree(String expression, ArgumentStrategy strategy)
	{
		Node tree = null;

		Argument farg, sarg;
		String fop = "", cleanfop = "";
		int ma = 0, i = 0;

		int len = expression.length();

		if (len == 0)
		{
			throw new IllegalArgumentException("Wrong number of arguments to operator");
		}
		else if (Utils.isNumeric(expression))
		{
			return NodeFactory.createNodeNumberFrom(Double.parseDouble(Utils
					.removeSpaces(expression)));
		}
		else if (expression.charAt(0) == '('
				&& (ma = Utils.matchingCharacterIndex(expression, 0, '(', ')')) == len - 1)
		{
			return parseTree(expression.substring(1, ma), strategy);
		}
		else if (expression.charAt(0) == '{'
				&& (ma = Utils.matchingCharacterIndex(expression, 0, '{', '}')) == len - 1)
		{
			return NodeFactory.createVectorFrom(expression.substring(1, ma), this);
		}
		// else if(isVector(Utils.removeSpaces(expression)))
		// {
		// return NodeFactory.createVectorFrom(expression, this);
		// }
		else if (expression.charAt(0) == '['
				&& (ma = Utils.matchingCharacterIndex(expression, 0, '[', ']')) == len - 1)
		{
			return NodeFactory.createMatrixFrom(expression.substring(1, ma), this);
		}
		else if (isVariable(expression))
		{
			return new NodeVariable(expression);
		}

		while (i < len)
		{
			if ((fop = findOperator(expression, i)) == null)
			{
				farg = strategy.getArgumentsFrom(null, expression, i);
				fop = findOperator(expression, i + farg.length);

				if (fop == null)
					throw new IllegalArgumentException("Missing operator," + " expression is \""
							+ expression + "\"");

				cleanfop = Utils.standardizeString(fop);
				if (isTwoArgOp(fop))
				{
					sarg = strategy.getArgumentsFrom(fop, expression, i + farg.length + fop.length());
					if (sarg.source.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					
					if(farg.node == null)
						farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
					if(sarg.node == null)
						sarg.node = parseTree(sarg.source, new DefaultArgumentStrategy());
					
					tree = new NodeExpression(operators.get(cleanfop), farg.node, sarg.node);
					i += farg.length + fop.length() + sarg.length;
				}
				else
				{
					// Never using this check
					if (farg.source.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					
					if(farg.node == null)
						farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
					tree = new NodeExpression(operators.get(cleanfop), farg.node);
					i += farg.length + fop.length();
				}
			}
			else
			{
				cleanfop = Utils.standardizeString(fop);
				if (isTwoArgOp(fop))
				{
					farg = strategy.getArgumentsFrom(fop, expression, i + fop.length());
					if (farg.source.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					if (tree == null)
					{
						if (cleanfop.equals("+") || cleanfop.equals("-"))
						{
							tree = NodeFactory.createNodeNumberFrom(0D);
						}
						else
						{
							throw new IllegalArgumentException(
									"Wrong number of arguments to operator " + fop);
						}
					}
					
					if(farg.node == null)
						farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
					tree = new NodeExpression(operators.get(cleanfop), tree, farg.node);
					i += farg.length + fop.length();
				}
				else
				{
					farg = strategy.getArgumentsFrom(fop, expression, i + fop.length());
					if (farg.source.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					if(farg.node == null)
						farg.node = parseTree(farg.source, new DefaultArgumentStrategy());
					tree = new NodeExpression(operators.get(cleanfop), farg.node);
					i += farg.length + fop.length();
				}
			}
		}

		return tree;
	}

	private interface ArgumentStrategy
	{
		Argument getArgumentsFrom(String operator, String exp, int index);
	}

	private class DefaultArgumentStrategy implements ArgumentStrategy
	{
		@Override
		public Argument getArgumentsFrom(String operator, String exp, int index)
		{
			return getArgumentsDefault(operator, exp, index);
		}
	}

	private class RecursiveArgumentStrategy implements ArgumentStrategy
	{
		@Override
		public Argument getArgumentsFrom(String operator, String exp, int index)
		{
			return getArgumentsRecursive(operator, exp, index);
		}
	}

	void setVariables(Set<String> set)
	{
		this.variables = set;
	}
}
