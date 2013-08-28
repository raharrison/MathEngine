package uk.co.ryanharrison.mathengine;

public final class RationalComplex
{
	private BigRational re;
	private BigRational im;

	public static final RationalComplex I = new RationalComplex(BigRational.ZERO, BigRational.ONE);
	public static final RationalComplex ZERO = new RationalComplex(BigRational.ZERO,
			BigRational.ZERO);
	public static final RationalComplex ONE = new RationalComplex(BigRational.ONE, BigRational.ZERO);

	public RationalComplex()
	{
		this.re = BigRational.ZERO;
		this.im = BigRational.ZERO;
	}

	public RationalComplex(BigRational realPart)
	{
		this.re = realPart;
		this.im = BigRational.ZERO;
	}

	public RationalComplex(double realPart)
	{
		this.re = new BigRational(realPart, 1E-15, 150);
		this.im = BigRational.ZERO;
	}

	public RationalComplex(BigRational realPart, BigRational imaginary)
	{
		this.re = realPart;
		this.im = imaginary;
	}

	public RationalComplex(double realPart, double imaginary)
	{
		this.re = new BigRational(realPart, 1E-15, 150);
		this.im = new BigRational(imaginary, 1E-15, 150);
	}

	public RationalComplex add(RationalComplex b)
	{
		return new RationalComplex(this.getRe().add(b.getRe()), this.getIm().add(b.getIm()));
	}

	public RationalComplex add(double b)
	{
		return new RationalComplex(this.getRe().add(new BigRational(b, 1E-15, 150)), this.getIm());
	}

	public RationalComplex divide(RationalComplex b)
	{
		return this.multiply(conj(b)).multiply(1 / (abs(b) * abs(b)));
	}

	public boolean equals(RationalComplex b)
	{
		return this.getRe() == b.getRe() && this.getIm() == b.getIm();
	}

	public boolean equals(double b)
	{
		return this.equals(new RationalComplex(b));
	}

	public double getImaginaryDouble()
	{
		return im.doubleValue();
	}

	public double getRealDouble()
	{
		return re.doubleValue();
	}

	public BigRational getIm()
	{
		return im;
	}

	public BigRational getRe()
	{
		return re;
	}

	public boolean isImaginary()
	{
		return this.getRe().equals(BigRational.ZERO);
	}

	public boolean isReal()
	{
		return this.getIm().equals(BigRational.ZERO);
	}

	// Complex / double
	public RationalComplex leftDivide(double b)
	{
		return this.multiply(1 / b);
	}

	public RationalComplex multiply(RationalComplex b)
	{
		return new RationalComplex(this.getRe().multiply(b.getRe()).subtract(this.getIm())
				.multiply(b.getIm()), this.getIm().multiply(b.getRe()).add(this.getRe())
				.multiply(b.getIm()));
	}

	public RationalComplex multiply(double d)
	{
		BigRational dd = new BigRational(d, 1E-15, 150);
		return new RationalComplex(dd.multiply(this.getRe()), dd.multiply(this.getIm()));
	}

	public boolean notEquals(RationalComplex b)
	{
		return !this.equals(b);
	}

	public boolean notEquals(double b)
	{
		return !this.equals(b);
	}

	// double / Complex
	public RationalComplex rightDivide(double b)
	{
		return conj(this).multiply(b).multiply(1 / (abs(this) * abs(this)));
	}

	public void setIm(double im)
	{
		this.im = new BigRational(im, 1E-15, 150);
		;
	}

	public void setRe(double re)
	{
		this.re = new BigRational(re, 1E-15, 150);
	}

	public void setIm(BigRational im)
	{
		this.im = im;
	}

	public void setRe(BigRational re)
	{
		this.re = re;
	}

	public RationalComplex subtract(RationalComplex b)
	{
		return new RationalComplex(this.getRe().subtract(b.getRe()), this.getIm().subtract(
				b.getIm()));
	}

	public RationalComplex subtract(double b)
	{
		return new RationalComplex(this.getRe().subtract(new BigRational(b, 1E-15, 150)),
				this.getIm());
	}

	@Override
	public String toString()
	{
		if (this == RationalComplex.ZERO)
			return "0";

		String re, im, sign;

		if (this.getIm().doubleValue() < 0)
		{
			if (this.getRe().equals(BigRational.ZERO))
				sign = "-";
			else
				sign = " - ";
		}
		else if (this.getIm().doubleValue() > 0 && (!this.getRe().equals(BigRational.ZERO)))
			sign = " + ";
		else
			sign = "";

		if (this.getRe().equals(BigRational.ZERO))
			re = "";
		else
			re = this.getRe().toString();

		if (this.getIm().equals(BigRational.ZERO))
			im = "";
		else if (this.getIm().equals(BigRational.MINUS_ONE) || this.getIm().equals(BigRational.ONE))
			im = "i";
		else
			im = this.getIm().abs() + "i";

		return re + sign + im;
	}

	public RationalComplex uminus()
	{
		return new RationalComplex(this.getRe().negate(), this.getIm().negate());
	}

	// / <summary>
	// / Calcs the absolute value of a complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static double abs(RationalComplex a)
	{
		return Math.sqrt(a.getIm().doubleValue() * a.getIm().doubleValue()
				+ a.getRe().doubleValue() * a.getRe().doubleValue());
	}

	// / <summary>
	// / Argument of the complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static double arg(RationalComplex a)
	{
		if (a.getRe().doubleValue() < 0)
		{
			if (a.getIm().doubleValue() < 0)
				return Math.atan(a.getIm().doubleValue() / a.getRe().doubleValue()) - Math.PI;
			else
				return Math.PI - Math.atan(-a.getIm().doubleValue() / a.getRe().doubleValue());
		}
		else
			return Math.atan(a.getIm().doubleValue() / a.getRe().doubleValue());

	}

	// / <summary>
	// / Computes the conjugation of a complex number.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static RationalComplex conj(RationalComplex a)
	{
		return new RationalComplex(a.getRe(), a.getIm().negate());
	}

	// / <summary>
	// / Complex exponential function.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static RationalComplex exp(RationalComplex a)
	{
		return new RationalComplex(Math.exp(a.getRe().doubleValue())
				* Math.cos(a.getIm().doubleValue()), Math.exp(a.getRe().doubleValue())
				* Math.sin(a.getIm().doubleValue()));
	}

	// / <summary>
	// / Inverts a.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static RationalComplex inv(RationalComplex a)
	{
		return new RationalComplex(a.getRe().divide(
				a.getRe().multiply(a.getRe()).add(a.getIm()).multiply(a.getIm())), a.getIm()
				.negate().divide(a.getRe().multiply(a.getRe()).add(a.getIm().multiply(a.getIm()))));
	}

	// / <summary>
	// / Main value of the complex logarithm.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static RationalComplex log(RationalComplex a)
	{
		// Log[|w|]+I*(Arg[w]+2*Pi*k)

		return new RationalComplex(Math.log(abs(a)), arg(a));
	}

	public static RationalComplex pow(RationalComplex a, RationalComplex b)
	{
		return exp(b.multiply(log(a)));
	}

	public static RationalComplex pow(RationalComplex a, double b)
	{
		return exp(new RationalComplex(b).multiply(log(a)));
	}

	public static RationalComplex pow(double a, RationalComplex b)
	{
		return exp(b.multiply(Math.log(a)));
	}

	// / <summary>
	// / Complex square root.
	// / </summary>
	// / <param name="a"></param>
	// / <returns></returns>
	public static RationalComplex sqrt(RationalComplex a)
	{
		return pow(a, .5);
	}

	// / <summary>
	// / Complex square root.
	// / </summary>
	// / <param name="d"></param>
	// / <returns></returns>
	public static RationalComplex sqrt(double d)
	{
		if (d >= 0)
			return new RationalComplex(Math.sqrt(d));
		else
			return new RationalComplex(0, Math.sqrt(-d));
	}
}
