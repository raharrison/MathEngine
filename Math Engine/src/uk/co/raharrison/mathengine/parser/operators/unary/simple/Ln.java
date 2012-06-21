package uk.co.raharrison.mathengine.parser.operators.unary.simple;

public class Ln extends SimpleUnaryOperator
{
	@Override
	public int getPrecedence()
	{
		return 5;
	}

	@Override
	public double getResult(double x)
	{
		return Math.log(x);
	}
}
