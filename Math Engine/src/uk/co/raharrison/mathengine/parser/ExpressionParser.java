package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;
import java.util.HashSet;
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
import uk.co.raharrison.mathengine.parser.operators.binary.Convert;
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

public final class ExpressionParser implements Parser<String, Node>
{
	private HashMap<String, Operator> operators;
	private int maxoplength;
	private HashSet<String> variables;

	public ExpressionParser()
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

		addOperator(new Convert());

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
				if (str.length() != 0 && getOperator(op).getPrecedence() >= prec)
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

	private Operator getOperator(String operator)
	{
		return operators.get(Utils.standardizeString(operator));
	}

	private boolean isAllowedSym(char s)
	{
		return !(s == ',' || s == '.' || s == ':' || s == ')' || s == '(' || s == '>' || s == '<'
				|| s == '&' || s == '=' || s == '|' || s == '[' || s == ']' || s == '{' || s == '}');
	}

	// private NodeUnit isNodeUnit(String expression)
	// {
	// if (Utils.isNumeric(expression) || engine == null)
	// return null;
	//
	// for (String s : operators.get("in").getAliases())
	// {
	// if (expression.contains(s))
	// return null;
	// }
	//
	// for (int i = 0; i < expression.length(); i++)
	// {
	// if (engine.isUnit(expression.substring(i, expression.length())))
	// {
	// String unit = expression.substring(i, expression.length());
	// SubUnit sub = engine.getUnitFrom(unit);
	// if (i == 0) // TODO : mph == milliphots
	// // ////////////////////////////////
	// return new NodeUnit(sub, NodeFactory.createNodeNumberFrom(1.0), false);
	// else
	// {
	// Node val = parse(expression.substring(0, i));
	// return new NodeUnit(sub, val);
	// }
	// }
	// }
	//
	// return null;
	// }

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

		return true;
	}

	public Node parse(String expression)
	{
		// expression = insertMult(expression);
		return parseTree(expression);
	}

	private Node parseTree(String expression)
	{
		Node tree = null;

		String farg = "", sarg = "", fop = "", cleanfop = "";
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
		// else if (isNodeUnit(expression) != null)
		// {
		// return isNodeUnit(expression);
		// }
		else if (expression.contains(":="))
		{
			int index = expression.indexOf(":=");

			String variable = expression.substring(0, index);
			if (isOperator(variable, false))
				throw new IllegalArgumentException("Variable is an operator");

			String t = expression.substring(index + 2, expression.length());

			Node node = parseTree(t.trim());
			return new NodeAddVariable(variable.trim(), node);
		}
		else if (expression.charAt(0) == '('
				&& (ma = Utils.matchingCharacterIndex(expression, 0, '(', ')')) == len - 1)
		{
			return parseTree(expression.substring(1, ma));
		}
		else if (expression.charAt(0) == '{'
				&& (ma = Utils.matchingCharacterIndex(expression, 0, '{', '}')) == len - 1)
		{
			return NodeFactory.createVectorFrom(expression.substring(1, ma), this);
		}
		// else if(Utils.removeSpaces(expression).matches("([^,(]+,)+([^,)]+)"))
		// // TODO : Use isVector() method like this
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
			return new NodeToken(expression);
		}

		while (i < len)
		{
			if ((fop = findOperator(expression, i)) == null)
			{
				farg = getArguments(null, expression, i);
				fop = findOperator(expression, i + farg.length());

				if (fop == null)
					throw new IllegalArgumentException("Missing operator," + " expression is \""
							+ expression + "\"");

				cleanfop = Utils.standardizeString(fop);
				if (isTwoArgOp(fop))
				{
					sarg = getArguments(fop, expression, i + farg.length() + fop.length());
					if (sarg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(cleanfop), parseTree(farg),
							parseTree(sarg));
					i += farg.length() + fop.length() + sarg.length();
				}
				else
				{
					if (farg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(cleanfop), parseTree(farg));
					i += farg.length() + fop.length();
				}
			}
			else
			{
				cleanfop = Utils.standardizeString(fop);
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
					tree = new NodeExpression(operators.get(cleanfop), tree, parseTree(farg));
					i += farg.length() + fop.length();
				}
				else
				{
					farg = getArguments(fop, expression, i + fop.length());
					if (farg.equals(""))
						throw new IllegalArgumentException("Wrong number of arguments to operator "
								+ fop);
					tree = new NodeExpression(operators.get(cleanfop), parseTree(farg));
					i += farg.length() + fop.length();
				}
			}
		}

		return tree;
	}

	// TODO : Ignore spaces
	@SuppressWarnings("unused")
	private String insertMult(String expression)
	{
		int i = 0, p = 0;
		String tmp = null;
		StringBuilder str = new StringBuilder(expression);

		int l = expression.length();

		while (i < l)
		{
			try
			{
				if ((tmp = findOperator(expression, i)) != null && !isTwoArgOp(tmp)
						&& Utils.isAlphabetic(expression.charAt(i - 1)))
				{
					// case: variable jp one-arg-op , xcos(x)
					str.insert(i + p, "*");
					p++;
				}
				else if (Utils.isAlphabetic(expression.charAt(i))
						&& Utils.isNumeric(expression.charAt(i - 1)) && (tmp == null)) // tmp
																						// was
																						// set
																						// by
																						// previous
																						// test
				{
					// case: const jp variable or one-arg-op, 2x, 2tan(x)
					str.insert(i + p, "*");
					p++;
				}
				else if (expression.charAt(i) == '(' && Utils.isNumeric(expression.charAt(i - 1)))
				{
					// case: "const jp ( expr )" , 2(3+x)
					str.insert(i + p, "*");
					p++;
				}
				else if (Utils.isAlphabetic(expression.charAt(i))
						&& expression.charAt(i - 1) == ')' && (tmp == null)) // tmp
																				// was
																				// set
																				// by
																				// previous
																				// test
				{
					// case: ( expr ) jp variable or one-arg-op , (2-x)x ,
					// (2-x)sin(x)
					str.insert(i + p, "*");
					p++;
				}
				else if (expression.charAt(i) == '(' && expression.charAt(i - 1) == ')')
				{
					// case: ( expr ) jp ( expr ) , (2-x)(x+1) , sin(x)(2-x)
					str.insert(i + p, "*");
					p++;
				}
				else if (expression.charAt(i) == '('
						&& Utils.isAlphabetic(expression.charAt(i - 1)))
				{
					// case: var jp ( expr ) , x(x+1) , x(1-sin(x))
					str.insert(i + p, "*");
					p++;
				}
			}
			catch (IndexOutOfBoundsException e)
			{
			}

			if (tmp != null)
			{
				i += tmp.length();
			}
			else
			{
				i++;
			}

			tmp = null;
		}

		return str.toString();
	}

	public void setVariables(HashSet<String> vars)
	{
		this.variables = vars;
	}
}
