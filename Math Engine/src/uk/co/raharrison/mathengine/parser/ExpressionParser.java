package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeAddVariable;
import uk.co.raharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeToken;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;

public final class ExpressionParser implements Parser<String, Node>
{
	private HashMap<String, Operator> operators;
	private int maxoplength;
	private HashSet<String> variables;

	public ExpressionParser()
	{
		operators = new HashMap<String, Operator>();

		maxoplength = 0;
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

	void fillOperators(List<Operator> operators)
	{
		for (Operator operator : operators)
		{
			addOperator(operator);
		}

		maxoplength = findLongestOperator();
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
				if (str.length() != 0 && !isTwoArgOp(backTrack(str.toString()))
						&& getOperator(op).getPrecedence() >= prec)
				{
					// TODO : HAVE GETARGS PARSE THE ARGS INSTEAD OF PARSETREE METHOD (RETURN A NODE)
					String s = str.toString();
					if(isVariable(s) || Utils.isNumeric(s) ||isVector(s))
						return s;
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

//		for (int i = 0; i < expression.length(); i++)
//		{
//			if (findOperator(expression, i) != null)
//				return false;
//			else if (!isAllowedSym(expression.charAt(i)))
//				return false;
//		}

		//Used to be true
		return false;
	}
	
	private boolean isVector(String str)
	{
		for (Entry<String, Operator> entry : operators.entrySet())
		{
			if(str.startsWith(entry.getKey()))
				return false;
		}
		
		return str.matches("([^,(]+,)+([^,)]+)");
	}
	
	@SuppressWarnings("unused")
	private boolean isMatrix(String str)
	{
		if(str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']')
			return isVector(str.substring(1, str.length() - 1));
		
		return false;
	}

	public Node parse(String expression)
	{
		int index = expression.indexOf("=");
		
		if(index != -1)
		{
			String variable = expression.substring(0, index);
			if (isOperator(variable, false))
				throw new IllegalArgumentException("Variable is an operator");

			String t = expression.substring(index + 2, expression.length());

			Node node = NodeFactory.createNodeFunctionFrom(variable, parseTree(t.trim()));
			return new NodeAddVariable(variable.trim(), node);
		}
		
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

		if(isVariable(expression))
			return expression;
		
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
						&& Utils.isAlphabetic(expression.charAt(i - 1))
						&& backTrack(expression.substring(0, i)) == null)
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

	public void setVariables(HashSet<String> vars)
	{
		this.variables = vars;
	}
}
