package uk.co.raharrison.mathengine.parser.operators;

import java.util.ArrayList;
import java.util.List;

import uk.co.raharrison.mathengine.parser.operators.binary.*;
import uk.co.raharrison.mathengine.parser.operators.binary.logical.*;
import uk.co.raharrison.mathengine.parser.operators.unary.*;
import uk.co.raharrison.mathengine.parser.operators.unary.simple.*;

public class OperatorProvider
{
	public static List<Operator> customOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new Solve());
		operators.add(new NSolve());
		operators.add(new If());
		operators.add(new Select());
		operators.add(new Where());
		operators.add(new Diff());
		// operators.add(new Convert());

		return operators;
	}

	public static List<Operator> logicalOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new And());
		operators.add(new Equals());
		operators.add(new Not());
		operators.add(new GreaterThan());
		operators.add(new GreaterThanEqualTo());
		operators.add(new LessThan());
		operators.add(new LessThanEqualTo());
		operators.add(new NotEquals());
		operators.add(new Or());
		operators.add(new Xor());

		return operators;
	}

	public static List<Operator> matrixOperators()
	{
		List<Operator> operators = new ArrayList<>();

		return operators;
	}

	public static List<Operator> simpleBinaryOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new Add());
		operators.add(new Divide());
		operators.add(new Multiply());
		operators.add(new PercentOf());
		operators.add(new AsPercentOf());
		operators.add(new Pow());
		operators.add(new Subtract());

		return operators;
	}

	public static List<Operator> simpleUnaryOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new Factorial());
		operators.add(new Ln());
		operators.add(new Log());
		operators.add(new Percent());
		operators.add(new Abs());
		operators.add(new DoubleFactorial());
		operators.add(new ToDouble());
		operators.add(new ToRational());

		return operators;
	}

	public static List<Operator> trigOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new Sine());
		operators.add(new Cosine());
		operators.add(new Tangent());

		return operators;
	}

	public static List<Operator> vectorOperators()
	{
		List<Operator> operators = new ArrayList<>();

		operators.add(new Sort());
		operators.add(new Sum());

		return operators;
	}

	private OperatorProvider()
	{
	}
}