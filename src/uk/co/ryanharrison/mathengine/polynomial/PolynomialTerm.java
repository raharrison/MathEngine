package uk.co.ryanharrison.mathengine.polynomial;

class PolynomialTerm
{
	private int _Power;
	private double _coefficient;

	public PolynomialTerm(int power, double coefficient)
	{
		this.setPower(power);
		this.setCoefficient(coefficient);
	}

	public PolynomialTerm(String TermExpression)
	{
		if (TermExpression.length() > 0)
		{
			if (TermExpression.indexOf("x^") > -1)
			{
				String CoefficientString = TermExpression.substring(0,
						0 + TermExpression.indexOf("x^"));
				int IndexofX = TermExpression.indexOf("x^");
				String PowerString = TermExpression.substring(IndexofX + 2, IndexofX + 2
						+ TermExpression.length() - 1 - (IndexofX + 1));
				if (CoefficientString.equals("-"))
					this.setCoefficient(-1);
				else if (CoefficientString.equals("+") | CoefficientString.equals(""))
					this.setCoefficient(1);
				else
					this.setCoefficient(Double.parseDouble(CoefficientString));

				this.setPower(Integer.parseInt(PowerString));
			}
			else if (TermExpression.indexOf("x") > -1)
			{
				this.setPower(1);
				String CoefficientString = TermExpression.substring(0,
						0 + TermExpression.indexOf("x"));
				if (CoefficientString.equals("-"))
					this.setCoefficient(-1);
				else if (CoefficientString.equals("+") | CoefficientString.equals(""))
					this.setCoefficient(1);
				else
					this.setCoefficient(Double.parseDouble(CoefficientString));
			}
			else
			{
				this.setPower(0);
				this.setCoefficient(Double.parseDouble(TermExpression));
			}
		}
		else
		{
			this.setPower(0);
			this.setCoefficient(0);
		}
	}

	public double getCoefficient()
	{
		return _coefficient;
	}

	public int getPower()
	{
		return _Power;
	}

	public void setCoefficient(double _coefficient)
	{
		this._coefficient = _coefficient;
	}

	public void setPower(int _Power)
	{
		this._Power = _Power;
	}
}
