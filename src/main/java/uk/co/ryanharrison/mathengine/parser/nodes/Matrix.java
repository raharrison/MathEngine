package uk.co.ryanharrison.mathengine.parser.nodes;

public final class Matrix
{
	private NodeNumber[][] elements;

	private int rows, columns;

	public Matrix multiply(Matrix B) {
		if (B.rows != columns) {
			throw new IllegalArgumentException("Matrix inner dimensions must agree.");
		}
		Matrix X = null;// = new Matrix(rows, B.columns);
		NodeNumber[] Bcolj = new NodeNumber[columns];
		for (int j = 0; j < B.columns; j++) {
			for (int k = 0; k < columns; k++) {
				Bcolj[k] = B.elements[k][j];
			}
			for (int i = 0; i < rows; i++) {
				NodeNumber[] Arowi = elements[i];
				NodeNumber s = NodeFactory.createZeroNumber();
				for (int k = 0; k < columns; k++) {
					s = s.add(Arowi[k].multiply(Bcolj[k]).getTransformer().toNodeNumber()).getTransformer().toNodeNumber();
				}

				X.elements[i][j] = s;
			}
		}
		return X;
	}

}
