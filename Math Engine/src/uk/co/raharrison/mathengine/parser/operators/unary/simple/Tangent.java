package uk.co.raharrison.mathengine.parser.operators.unary.simple;

import uk.co.raharrison.mathengine.parser.AngleUnit;
import uk.co.raharrison.mathengine.parser.operators.TrigOperator;

public class Tangent extends TrigOperator
{
	@Override
	public double getResult(double x, AngleUnit unit)
	{
		return Math.tan(super.radiansTo(x, unit));
	}
}
