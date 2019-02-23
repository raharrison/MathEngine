package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.BigRational;

public class Conversion
{
	private UnitGroup unitGroup;
	private SubUnit from;
	private SubUnit to;
	private BigRational value;
	private BigRational result;

	public SubUnit getFrom()
	{
		return this.from;
	}

	public BigRational getResult()
	{
		return result;
	}

	public SubUnit getTo()
	{
		return to;
	}

	public BigRational getValue()
	{
		return value;
	}

	public void setFrom(SubUnit unit)
	{
		this.from = unit;
	}

	void setResult(BigRational result)
	{
		this.result = result;
	}

	public void setTo(SubUnit to)
	{
		this.to = to;
	}

	void setValue(BigRational value)
	{
		this.value = value;
	}

	public UnitGroup getUnitGroup() {
		return unitGroup;
	}

	public void setUnitGroup(UnitGroup unitGroup) {
		this.unitGroup = unitGroup;
	}

	@Override
	public String toString()
	{
		if (result.abs().doubleValue() > 1)
			return result + " " + to.getPlural();
		else
			return result + " " + to.getSingular();
	}

}
