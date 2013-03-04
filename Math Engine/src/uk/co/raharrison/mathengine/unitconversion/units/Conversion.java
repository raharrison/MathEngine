package uk.co.raharrison.mathengine.unitconversion.units;

public class Conversion
{
	private SubUnit from;
	private SubUnit to;
	private double value;
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

	public double getValue()
	{
		return value;
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

	void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		if (Math.abs(result) > 1)
			return result + " " + to.getPlural();
		else
			return result + " " + to.getSingular();
	}
}
