package uk.co.raharrison.mathengine.parser;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;

public final class Evaluator
{
	private Parser parser;
	private RecursiveDescentParser rec;

	private Node cached;

	public Evaluator()
	{
		parser = new Parser();
		rec = new RecursiveDescentParser();
		cached = null;
	}

	public void addVariable(String variable, NodeConstant value)
	{
		variable = Utils.standardizeString(variable);

		if (!parser.isOperator(variable))
			rec.addConstant(variable, value);
		else
			throw new IllegalArgumentException("Constant is an operator");
	}

	public void addVariable(String variable, String value)
	{
		value = Utils.standardizeString(value);
		NodeConstant r = rec.toValue(parser.parse(value));

		addVariable(variable, r);
	}

	public void compileTree(String expression)
	{
		this.cached = generateTree(expression);
	}

	public NodeConstant evaluateCachedTreeConstant()
	{
		return parseTree(cached);
	}

	public double evaluateCachedTreeDouble()
	{
		NodeConstant r = parseTree(cached);

		if (r instanceof NodeDouble)
		{
			return ((NodeDouble) r).asDouble();
		}
		else
		{
			throw new UnsupportedOperationException("Expression does not return a double value");
		}
	}

	public String evaluateCachedTreeString()
	{
		return parseTree(cached).toString();
	}

	public NodeConstant evaluateConstant(String expression)
	{
		Node tree = generateTree(expression);
		return parseTree(tree);
	}

	public double evaluateDouble(String expression)
	{
		Node tree = generateTree(expression);
		NodeConstant r = parseTree(tree);

		if (r instanceof NodeDouble)
		{
			return ((NodeDouble) r).asDouble();
		}
		else
		{
			throw new UnsupportedOperationException("Expression does not return a double value");
		}
	}

	public String evaluateString(String expression)
	{
		Node tree = generateTree(expression);

		return parseTree(tree).toString();
	}

	private Node generateTree(String expression)
	{
		expression = Utils.standardizeString(expression);

		// TODO: Add Node CustomOperator
		if (expression.equals("clearvars"))
		{
			rec.clearConstants();
			return null;
		}

		return parser.parse(expression);
	}

	private NodeConstant parseTree(Node tree)
	{
		NodeConstant result = rec.toValue(tree);
		addVariable("ans", result);
		return result;
	}

	public void resetConstants()
	{
		rec.clearConstants();
	}

	public void setAngleUnit(AngleUnit angleUnit)
	{
		this.rec.setAngleUnit(angleUnit);
	}
}