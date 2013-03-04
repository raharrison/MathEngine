package uk.co.raharrison.mathengine;

public final class Complex
{
	private double re;
	private double im;

	public static final Complex I = new Complex(0, 1);
	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);

	public Complex()
	{
		this.re = 0;
		this.im = 0;
	}

	public Complex(double realPart)
	{
		this.re = realPart;
		this.im = 0;
	}

	public Complex(double realPart, double imaginary)
	{
		this.re = realPart;
		this.im = imaginary;
	}

	public Complex add(Complex b)
	{
		return new Complex(this.getRe() + b.getRe(), this.getIm() + b.getIm());
	}

	public Complex add(double b)
	{
		return new Complex(this.getRe() + b, this.getIm());
	}

	public Complex divide(Complex b)
	{
		return this.multiply(conj(b)).multiply(1 / (abs(b) * abs(b)));
	}

	public boolean equals(Complex b)
	{
		return this.getRe() == b.getRe() && this.getIm() == b.getIm();
	}

	public boolean equals(double b)
	{
		return this.equals(new Complex(b));
	}

	public double getIm()
	{
		return im;
	}

	public double getRe()
	{
		return re;
	}

	public boolean isImaginary()
	{
		return this.getRe() == 0;
	}

	public boolean isReal()
	{
		return this.getIm() == 0;
	}

	// Complex / double
	public Complex leftDivide(double b)
	{
		return this.multiply(1 / b);
	}

	public Complex multiply(Complex b)
	{
		return new Complex(this.getRe() * b.getRe() - this.getIm() * b.getIm(), this.getIm()
				* b.getRe() + this.getRe() * b.getIm());
	}

	public Complex multiply(double d)
	{
		return new Complex(d * this.getRe(), d * this.getIm());
	}

	public boolean notEquals(Complex b)
	{
		return !this.equals(b);
	}

	public boolean notEquals(double b)
	{
		return !this.equals(b);
	}

	// double / Complex
	public Complex rightDivide(double b)
	{
		return conj(this).multiply(b).multiply(1 / (abs(this) * abs(this)));
	}

	public void setIm(double im)
	{
		this.im = im;
	}

	public void setRe(double re)
	{
		this.re = re;
	}

	public Complex subtract(Complex b)
	{
		return new Complex(this.getRe() - b.getRe(), this.getIm() - b.getIm());
	}

	public Complex subtract(double b)
	{
		return new Complex(this.getRe() - b, this.getIm());
	}

	@Override
	public String toString()
	{
		if (this == Complex.ZERO)
			return "0";

		String re, im, sign;

		if (this.getIm() < 0)
		{
			if (this.getRe() == 0)
				sign = "-";
			else
				sign = " - ";
		}
		else if (this.getIm() > 0 && this.getRe() != 0)
			sign = " + ";
		else
			sign = "";

		if (this.getRe() == 0)
			re = "";
		else
			re = Double.toString(this.getRe());

		if (this.getIm() == 0)
			im = "";
		else if (this.getIm() == -1 || this.getIm() == 1)
			im = "i";
		else
			im = Double.toString(Math.abs(this.getIm())) + "i";

		return re + sign + im;
	}

	public Complex uminus()
	{
		return new Complex(-this.getRe(), -this.getIm());
	}

	// / <summary>
	// / Calcs the absolute value of a complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static double abs(Complex a)
	{
		return Math.sqrt(a.getIm() * a.getIm() + a.getRe() * a.getRe());
	}

	// / <summary>
	// / Argument of the complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static double arg(Complex a)
	{
		if (a.getRe() < 0)
		{
			if (a.getIm() < 0)
				return Math.atan(a.getIm() / a.getRe()) - Math.PI;
			else
				return Math.PI - Math.atan(-a.getIm() / a.getRe());
		}
		else
			return Math.atan(a.getIm() / a.getRe());

	}

	// / <summary>
	// / Computes the conjugation of a complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static Complex conj(Complex a)
	{
		return new Complex(a.getRe(), -a.getIm());
	}

	// / <summary>
	// / Complex exponential function.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static Complex exp(Complex a)
	{
		return new Complex(Math.exp(a.getRe()) * Math.cos(a.getIm()), Math.exp(a.getRe())
				* Math.sin(a.getIm()));
	}

	// / <summary>
	// / Inverts a.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static Complex inv(Complex a)
	{
		return new Complex(a.getRe() / (a.getRe() * a.getRe() + a.getIm() * a.getIm()), -a.getIm()
				/ (a.getRe() * a.getRe() + a.getIm() * a.getIm()));
	}

	// / <summary>
	// / Main value of the complex logarithm.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static Complex log(Complex a)
	{
		// Log[|w|]+I*(Arg[w]+2*Pi*k)

		return new Complex(Math.log(abs(a)), arg(a));
	}

	public static Complex pow(Complex a, Complex b)
	{
		return exp(b.multiply(log(a)));
	}

	public static Complex pow(Complex a, double b)
	{
		return exp(new Complex(b).multiply(log(a)));
	}

	public static Complex pow(double a, Complex b)
	{
		return exp(b.multiply(Math.log(a)));
	}

	public static Complex round(Complex c, int places)
	{
		return new Complex(MathUtils.round(c.getRe(), places), MathUtils.round(c.getIm(), places));
	}

	// / <summary>
	// / Complex square root.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static Complex sqrt(Complex a)
	{
		return pow(a, .5);
	}

	// / <summary>
	// / Complex square root.
	// / </summary>
	// / <param name="d"></param>
	// / <returns></returns>
	public static Complex sqrt(double d)
	{
		if (d >= 0)
			return new Complex(Math.sqrt(d));
		else
			return new Complex(0, Math.sqrt(-d));
	}
}
