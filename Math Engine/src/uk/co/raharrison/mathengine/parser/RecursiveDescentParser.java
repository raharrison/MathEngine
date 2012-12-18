package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;
import uk.co.raharrison.mathengine.parser.nodes.*;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;
import uk.co.raharrison.mathengine.parser.operators.binary.ConversionOperator;
import uk.co.raharrison.mathengine.parser.operators.unary.TrigOperator;
import uk.co.raharrison.mathengine.unitconversion.ConversionEngine;

public final class RecursiveDescentParser implements Parser<Node, NodeConstant>
{
	private Map<String, NodeConstant> constants;
	private AngleUnit angleUnit;
	private ConversionEngine engine;

	public RecursiveDescentParser()
	{
		constants = new HashMap<String, NodeConstant>();
		engine = new ConversionEngine();
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
		constants.put("clearvars", new NodeDouble(Double.NaN));

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

	public ConversionEngine getConversionEngine()
	{
		return this.engine;
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
		else if (tree instanceof NodeFunction)
		{
			NodeFunction func = (NodeFunction) tree;
			func.setParser(this);

			addConstant(func.getIdentifier(), func);
			return (NodeFunction) tree;
		}
		else if (tree instanceof NodeConstant)
		{
			return (NodeConstant) tree;
		}
		else if (tree instanceof NodeAddVariable)
		{
			NodeAddVariable nab = (NodeAddVariable) tree;

			NodeConstant result = parse(nab.getNode());
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
				return trigop.toResult(parse(expression.getArgOne()));
			}
			else if (operator instanceof ConversionOperator)
			{
				ConversionOperator convop = (ConversionOperator) operator;

				convop.setConversionEngine(engine);

				return convop
						.toResult(parse(expression.getArgOne()), parse(expression.getArgTwo()));
			}
			else if (operator instanceof UnaryOperator)
			{
				UnaryOperator unop = (UnaryOperator) operator;

				return unop.toResult(parse(expression.getArgOne()));
			}
			else
			{
				BinaryOperator binop = (BinaryOperator) operator;

				return binop.toResult(parse(expression.getArgOne()), parse(expression.getArgTwo()));
			}
		}

		throw new IllegalArgumentException("Unknown Operator");
	}

	Set<String> getVariableList()
	{
		Set<String> res = new HashSet<>();
		for (Entry<String, NodeConstant> e : constants.entrySet())
		{
			res.add(e.getKey());
		}
		return res;
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

	@Override
	public NodeConstant parse(Node tree)
	{
		return getResult(tree);
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
				vals[i][j] = parse(nodes[i][j]);
			}
		}

		return vals;
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
			vals[i] = parse(nodes[i]);
		}

		return vals;
	}
}
