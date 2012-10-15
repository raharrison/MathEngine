package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeAddVariable;
import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.raharrison.mathengine.parser.nodes.NodeFactory;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeNumber;
import uk.co.raharrison.mathengine.parser.nodes.NodeToken;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;
import uk.co.raharrison.mathengine.parser.operators.TrigOperator;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;
import uk.co.raharrison.mathengine.parser.operators.binary.Multiply;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;

public final class RecursiveDescentParser
{
	private HashMap<String, NodeConstant> constants;
	private AngleUnit angleUnit;

	private ConversionEngine conversionEngine;

	public RecursiveDescentParser()
	{
		constants = new HashMap<String, NodeConstant>();
		conversionEngine = new ConversionEngine();
		fillDefaultConstants();
		setAngleUnit(AngleUnit.Radians);
	}

	public void addConstant(String constant, NodeConstant value)
	{
		if (constant != null && !constant.equals(""))
		{
			constants.put(constant, value);
		}
		else
		{
			throw new IllegalArgumentException("Constant is null or empty");
		}
	}

	HashSet<String> getVariableList()
	{
		HashSet<String> res = new HashSet<>();
		for (Entry<String, NodeConstant> e : constants.entrySet())
		{
			res.add(e.getKey());
		}
		return res;
	}

	public void clearConstants()
	{
		constants.clear();
		fillDefaultConstants();
	}

	private void fillDefaultConstants()
	{
		constants.put("pi", new NodeDouble(Math.PI));
		constants.put("euler", new NodeDouble(Math.E));
		constants.put("infinity", new NodeDouble(Double.POSITIVE_INFINITY));
		constants.put("nan", new NodeDouble(Double.NaN));
		constants.put("goldenratio", new NodeDouble(1.6180339887));
		constants.put("true", new NodeBoolean(true));
		constants.put("false", new NodeBoolean(false));

		constants.put("zero", NodeFactory.createZeroNumber());
		constants.put("one", NodeFactory.createNodeNumberFrom(1.0));
		constants.put("two", NodeFactory.createNodeNumberFrom(2.0));
		constants.put("three", NodeFactory.createNodeNumberFrom(3.0));
		constants.put("four", NodeFactory.createNodeNumberFrom(4.0));
		constants.put("five", NodeFactory.createNodeNumberFrom(5.0));
		constants.put("six", NodeFactory.createNodeNumberFrom(6.0));
		constants.put("seven", NodeFactory.createNodeNumberFrom(7.0));
		constants.put("eight", NodeFactory.createNodeNumberFrom(8.0));
		constants.put("nine", NodeFactory.createNodeNumberFrom(9.0));
		constants.put("ten", NodeFactory.createNodeNumberFrom(10.0));

		constants.put("hundred", NodeFactory.createNodeNumberFrom(100.0));
		constants.put("thousand", NodeFactory.createNodeNumberFrom(1000.0));
		constants.put("million", NodeFactory.createNodeNumberFrom(1000000.0));
		constants.put("billion", NodeFactory.createNodeNumberFrom(1000000000.0));

		// constants.put("d", new NodeDouble(38.6));
		constants.put("t", new NodeDouble(4.6));
		constants
				.put("v", new NodeVector(new Vector(new double[] { 458.6, 1, 2, 8, 3, 7, 21, 4 })));
		constants.put("m", new NodeMatrix(new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 9 } })));
		constants.put("m2", new NodeMatrix(new Matrix(new double[][] { { -5, 7, 3 }, { -2, 1, 3 },
				{ 9, 4.5, 2 } })));
		constants.put("mm", new NodeMatrix(new Matrix(new double[][] { { 6, 18 }, { 4, -5 } })));

		constants.put("c", new NodeVector(new Node[] { constants.get("v"), constants.get("d"),
				constants.get("m"), constants.get("t") }));
	}

	public AngleUnit getAngleUnit()
	{
		return angleUnit;
	}

	private boolean handleCustomOperator(String operator)
	{
		if (operator.equals("clearvars"))
		{
			clearConstants();
			return true;
		}

		return false;
	}

	public void setAngleUnit(AngleUnit angleUnit)
	{
		this.angleUnit = angleUnit;
	}

	private NodeConstant[][] toMatrixValues(NodeMatrix tree)
	{
		int m = tree.getRowCount();
		int n = tree.getColumnCount();

		NodeConstant[][] vals = new NodeConstant[m][n];
		Node[][] nodes = tree.getValues();

		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				vals[i][j] = toValue(nodes[i][j]);
			}
		}

		return vals;
	}

	public NodeConstant toValue(Node tree)
	{
		while(replaceConversions(tree, conversionEngine))
			replaceConversions(tree, conversionEngine);
		
		return getResult(tree);
	}

	private boolean replaceConversions(Node tree, ConversionEngine engine)
	{
		if (tree != null)
		{
			if (tree instanceof NodeExpression)
			{
				NodeExpression exp = (NodeExpression) tree;
				
				if(isConversionNode(exp))
				{
					NodeExpression express = (NodeExpression) exp.getArgOne();
					double val = ((NodeNumber) ((NodeExpression) express.getArgOne()).getArgOne()).doubleValue();
					String from = ((NodeToken) ((NodeExpression) express.getArgOne()).getArgTwo()).getVariable();
					String to = ((NodeToken) express.getArgTwo()).getVariable();

					Node n = exp.getArgOne();
					n = NodeFactory.createNodeNumberFrom(engine.convert(val, from, to));
				}
				if (replaceConversions(exp.getArgOne(), engine))
				{
					NodeExpression express = (NodeExpression) exp.getArgOne();
					double val = ((NodeNumber) ((NodeExpression) express.getArgOne()).getArgOne()).doubleValue();
					String from = ((NodeToken) ((NodeExpression) express.getArgOne()).getArgTwo()).getVariable();
					String to = ((NodeToken) express.getArgTwo()).getVariable();

					Node n = exp.getArgOne();
					n = NodeFactory.createNodeNumberFrom(engine.convert(val, from, to));
				}

				if (isConversionNode(exp))
				{
					return true;
				}

				if (replaceConversions(exp.getArgTwo(), engine))
				{
					NodeExpression express = (NodeExpression) exp.getArgTwo();
					double val = ((NodeNumber) ((NodeExpression) express.getArgOne()).getArgOne()).doubleValue();
					String from = ((NodeToken) ((NodeExpression) express.getArgOne()).getArgTwo()).getVariable();
					String to = ((NodeToken) express.getArgTwo()).getVariable();

					Node n = exp.getArgTwo();
					n = NodeFactory.createNodeNumberFrom(engine.convert(val, from, to));
				}
			}
		}
		return false;
	}

	private static boolean isConversionNode(NodeExpression e)
	{
		if (e.getOperator().toString().equals("in"))
		{
			if (e.getArgTwo() instanceof NodeToken)
			{
				if (e.getArgOne() instanceof NodeExpression)
				{
					NodeExpression ex = ((NodeExpression) e.getArgOne());

					if (ex.getOperator() instanceof Multiply)
					{
						if (ex.getArgTwo() instanceof NodeToken)
							return true;
					}
					return false;
				}
			}
			return false;
		}
		return false;
	}

	private NodeConstant getResult(Node tree)
	{
		if (tree instanceof NodeVector)
		{
			return new NodeVector(toVectorValues((NodeVector) tree));
		}
		else if (tree instanceof NodeMatrix)
		{
			return new NodeMatrix(toMatrixValues((NodeMatrix) tree));
		}
		else if (tree instanceof NodeConstant)
		{
			return (NodeConstant) tree;
		}
		else if (tree instanceof NodeAddVariable)
		{
			NodeAddVariable nab = (NodeAddVariable) tree;

			NodeConstant result = toValue(nab.getNode());
			addConstant(nab.getVariable(), result);

			return result;
		}
		else if (tree instanceof NodeToken)
		{
			String tmp = ((NodeToken) tree).getVariable();

			if (handleCustomOperator(tmp))
			{
				return null;
			}
			else if (constants.containsKey(tmp))
			{
				return constants.get(tmp);
			}
			else
			{
				throw new IllegalArgumentException("No value associated with " + tmp);
			}
		}
		else if (tree instanceof NodeExpression)
		{
			NodeExpression expression = (NodeExpression) tree;
			Operator operator = ((NodeExpression) tree).getOperator();

			if (operator instanceof TrigOperator)
			{
				TrigOperator trigop = (TrigOperator) operator;

				trigop.setAngleUnit(angleUnit);
				return trigop.toResult(toValue(expression.getArgOne()));
			}
			else if (operator instanceof UnaryOperator)
			{
				UnaryOperator unop = (UnaryOperator) operator;

				return unop.toResult(toValue(expression.getArgOne()));
			}
			else
			{
				BinaryOperator binop = (BinaryOperator) operator;

				return binop.toResult(toValue(expression.getArgOne()),
						toValue(expression.getArgTwo()));
			}
		}

		throw new IllegalArgumentException("Unknown Operator");
	}

	public NodeConstant toValueConcurrent(Node tree)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private NodeConstant[] toVectorValues(NodeVector tree)
	{
		NodeConstant[] vals = new NodeConstant[tree.getSize()];
		Node[] nodes = tree.getValues();

		for (int i = 0; i < vals.length; i++)
		{
			vals[i] = toValue(nodes[i]);
		}

		return vals;
	}
}
