package uk.co.raharrison.mathengine.parser.nodes;

import uk.co.raharrison.mathengine.Rational;

public class NodeRational extends NodeNumber
{
	private Rational value;

	public NodeRational(double value)
	{
		this.value = new Rational(value);
	}

	public NodeRational(int numerator, int denominator)
	{
		this.value = new Rational(numerator, denominator);
	}

	public NodeRational(Rational rational)
	{
		this.value = rational;
	}

	@Override
	protected Object clone()
	{
		return value.clone();
	}

	@Override
	public int compareTo(NodeConstant o)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double doubleValue()
	{
		return value.doubleValue();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof NodeRational)
		{
			return value.equals(((NodeRational) object).getValue());
		}

		return false;
	}

	public Rational getValue()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	@Override
	public String toString()
	{
		return value.toString();
	}
}
