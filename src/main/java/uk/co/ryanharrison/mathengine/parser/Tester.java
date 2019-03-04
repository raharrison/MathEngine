package uk.co.ryanharrison.mathengine.parser;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.parser.nodes.*;
import uk.co.ryanharrison.mathengine.parser.operators.binary.Add;
import uk.co.ryanharrison.mathengine.parser.operators.unary.simple.Cosine;

public final class Tester
{
	public static void main(String[] args)
	{
		Evaluator evaluator = Evaluator.newEvaluator();

		NodeVector vec = new NodeVector(new Node[] { new NodeDouble(5),
				new NodeExpression(new Add(), new NodeDouble(4), new NodeDouble(3)),
				new NodeDouble(9) });

		NodeMatrix mat = new NodeMatrix(new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 },
				{ 7, 8, 9 } }));

		NodeExpression expr = new NodeExpression(new Cosine(), mat, vec);

		NodeConstant res = evaluator.parseTree(expr);

		System.out.println(res);
	}
}