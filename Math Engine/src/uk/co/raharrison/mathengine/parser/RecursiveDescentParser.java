package uk.co.raharrison.mathengine.parser;

import java.util.HashMap;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.linearalgebra.Vector;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeAddVariable;
import uk.co.raharrison.mathengine.parser.nodes.NodeBoolean;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeVariable;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.BinaryOperator;
import uk.co.raharrison.mathengine.parser.operators.Operator;
import uk.co.raharrison.mathengine.parser.operators.TrigOperator;
import uk.co.raharrison.mathengine.parser.operators.UnaryOperator;

public final class RecursiveDescentParser
{
	private HashMap<String, NodeConstant> constants;
	private AngleUnit angleUnit;

	public RecursiveDescentParser()
	{
		constants = new HashMap<String, NodeConstant>();
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

		constants.put("v", new NodeVector(new Vector(new double[] { 1, 2 })));
		constants.put("m", new NodeMatrix(new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 9 } })));
		constants.put("m2", new NodeMatrix(new Matrix(new double[][] { { -5, 7, 3 }, { -2, 1, 3 },
				{ 9, 4.5, 2 } })));
		constants.put("mm", new NodeMatrix(new Matrix(new double[][] { { 6, 18 }, { 4, -5 } })));
	}

	public AngleUnit getAngleUnit()
	{
		return angleUnit;
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
		else if (tree instanceof NodeVariable)
		{
			String tmp = ((NodeVariable) tree).getVariable();

			if (constants.containsKey(tmp))
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
