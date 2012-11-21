package uk.co.raharrison.mathengine.parser;

import uk.co.raharrison.mathengine.linearalgebra.Matrix;
import uk.co.raharrison.mathengine.parser.nodes.Node;
import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.raharrison.mathengine.parser.nodes.NodeDouble;
import uk.co.raharrison.mathengine.parser.nodes.NodeExpression;
import uk.co.raharrison.mathengine.parser.nodes.NodeMatrix;
import uk.co.raharrison.mathengine.parser.nodes.NodeVector;
import uk.co.raharrison.mathengine.parser.operators.binary.Add;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.Cosine;

public final class Tester
{
	public static void main(String[] args)
	{
		RecursiveDescentParser parser = new RecursiveDescentParser();

		NodeVector vec = new NodeVector(new Node[] { new NodeDouble(5),
				new NodeExpression(new Add(), new NodeDouble(4), new NodeDouble(3)),
				new NodeDouble(9) });

		NodeMatrix mat = new NodeMatrix(new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 9 } }));

		NodeExpression expr = new NodeExpression(new Cosine(), mat, vec);

		NodeConstant res = parser.parse(expr);

		System.out.println(res);
	}
}