package uk.co.raharrison.mathengine.unitconversion.units;

public class ConversionParams
{
	private SubUnit from;
	private SubUnit to;
	private double result;

	public SubUnit getFrom()
	{
		return this.from;
	}

	public double getResult()
	{
		return result;
	}

	public SubUnit getTo()
	{
		return to;
	}

	public void setFrom(SubUnit unit)
	{
		this.from = unit;
	}

	void setResult(double result)
	{
		this.result = result;
	}

	public void setTo(SubUnit to)
	{
		this.to = to;
	}
}
