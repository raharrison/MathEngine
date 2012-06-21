package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.MathUtils;

public class Factorial extends SimpleUnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 1;
	}

	@Override
	public double getResult(double x)
	{
		return MathUtils.factorial(x);
	}
}
