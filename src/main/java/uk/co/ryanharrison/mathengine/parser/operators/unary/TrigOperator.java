package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;

public abstract class TrigOperator extends NumberOperator
{
	protected AngleUnit unit;

	private double degToRad(double radians)
	{
		return Math.PI * radians / 180.0;
	}

	@Override
	public NodeConstant getResult(NodeConstant number)
	{
		return getResult(number, unit);
	}

	public abstract NodeConstant getResult(NodeConstant num, AngleUnit unit);

	private double gradToRad(double radians)
	{
		return radians * (Math.PI / 200);
	}

	protected double radiansTo(double radians, AngleUnit angleUnits)
	{
		switch (angleUnits)
		{
			case Degrees:
				return degToRad(radians);
			case Gradians:
				return gradToRad(radians);
			default:
				return radians;
		}
	}

	public void setAngleUnit(AngleUnit units)
	{
		this.unit = units;
	}
}
