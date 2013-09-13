package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.parser.nodes.Node;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.ryanharrison.mathengine.parser.operators.OperatorProvider;

public final class Evaluator
{
	public static Evaluator newEvaluator()
	{
		Evaluator evaluator = newSimpleEvaluator();
		evaluator.parser.fillOperators(OperatorProvider.logicalOperators());
		evaluator.parser.fillOperators(OperatorProvider.vectorOperators());
		evaluator.parser.fillOperators(OperatorProvider.matrixOperators());
		evaluator.parser.fillOperators(OperatorProvider.customOperators());

		return evaluator;
	}

	public static Evaluator newSimpleBinaryEvaluator()
	{
		Evaluator evaluator = new Evaluator();
		evaluator.parser.fillOperators(OperatorProvider.simpleBinaryOperators());

		return evaluator;
	}

	public static Evaluator newSimpleEvaluator()
	{
		Evaluator evaluator = newSimpleBinaryEvaluator();
		evaluator.parser.fillOperators(OperatorProvider.simpleUnaryOperators());
		evaluator.parser.fillOperators(OperatorProvider.trigOperators());

		return evaluator;
	}

	private ExpressionParser parser;

	private RecursiveDescentParser rec;

	private Node cached;

	private Evaluator()
	{
		parser = new ExpressionParser();
		rec = new RecursiveDescentParser();
		cached = null;
	}

	public void addVariable(String variable, NodeConstant value)
	{
		variable = variable.trim();

		if (!parser.isOperator(variable, false))
			rec.addConstant(variable, value);
		else
			throw new IllegalArgumentException("Constant is an operator");
	}

	public void addVariable(String variable, String value)
	{
		value = value.trim();
		NodeConstant r = rec.parse(parser.parse(value));

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

		if (r instanceof NodeNumber)
		{
			return r.getTransformer().toNodeNumber().doubleValue();
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

		if (r instanceof NodeNumber)
		{
			return r.getTransformer().toNodeNumber().doubleValue();
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
		expression = expression.trim();
		parser.setVariables(rec.getVariableList());
		return parser.parse(expression);
	}

	public Node getCachedTree()
	{
		return this.cached;
	}

	public String getCachedTreeToString()
	{
		return this.cached.toString();
	}

	private NodeConstant parseTree(Node tree)
	{
		NodeConstant result = rec.parse(tree);
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

	public void addAcceptedVariable(String variable)
	{
		this.parser.addVariable(variable);
	}
}
