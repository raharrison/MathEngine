package uk.co.raharrison.mathengine.integral;

public enum RectanglePosition
{
	Left(0), Midpoint(1), Right(2);

	private int value;

	private RectanglePosition(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return this.value;
	}
}
