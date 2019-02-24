package uk.co.ryanharrison.mathengine.parser.operators.binary;

import uk.co.ryanharrison.mathengine.parser.nodes.*;

import java.util.function.BiFunction;

public class Multiply extends SimpleBinaryOperator
{
	@Override
	public String[] getAliases()
	{
		return new String[] { "*", "times", "multiply", "mult" };
	}

	@Override
	public int getPrecedence()
	{
		return 4;
	}

	@Override
	public String toLongString()
	{
		return "multiply";
	}

	@Override
	protected BiFunction<NodeNumber, NodeNumber, NodeConstant> getBiFunc() {
		return NodeNumber::multiply;
	}

	@Override
	public NodeConstant toResult(NodeConstant arg1, NodeConstant arg2) {
		if(arg1 instanceof NodeMatrix && arg2 instanceof NodeMatrix) {
			return matrixMultiply((NodeMatrix) arg1, (NodeMatrix) arg2);
		}
		return super.toResult(arg1, arg2);
	}

	@Override
	public String toString()
	{
		return "*";
	}

	private NodeMatrix matrixMultiply(NodeMatrix a, NodeMatrix b) {
		if (a.rowCount() != b.rowCount() || a.colCount() != b.colCount()) {
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}

		int size = a.rowCount();
		Node[][] aValues = a.getValues();
		Node[][] bValues = b.getValues();

		NodeNumber[][] values = new NodeNumber[a.rowCount()][a.colCount()];
		Node[] Bcolj = new Node[size];
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				Bcolj[k] = bValues[k][j];
			}
			for (int i = 0; i < size; i++) {
				Node[] Arowi = aValues[i];
				NodeNumber s = NodeFactory.createZeroNumber();
				for (int k = 0; k < size; k++) {
					s = s.add(Arowi[k].getTransformer().toNodeNumber()
							.multiply(Bcolj[k].getTransformer().toNodeNumber()));
				}

				values[i][j] = s;
			}
		}
		return new NodeMatrix(values);
	}
}
