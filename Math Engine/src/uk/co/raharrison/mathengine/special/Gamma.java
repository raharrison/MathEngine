package uk.co.raharrison.mathengine.special;

public class Gamma
{
	private static final double[] y = { 0.0021695375159141994, 0.011413521097787704,
			0.027972308950302116, 0.051727015600492421, 0.082502225484340941, 0.12007019910960293,
			0.16415283300752470, 0.21442376986779355, 0.27051082840644336, 0.33199876341447887,
			0.39843234186401943, 0.46931971407375483, 0.54413605556657973, 0.62232745288031077,
			0.70331500465597174, 0.78649910768313447, 0.87126389619061517, 0.95698180152629142 };

	private static final double[] w = { 0.0055657196642445571, 0.012915947284065419,
			0.020181515297735382, 0.027298621498568734, 0.034213810770299537, 0.040875750923643261,
			0.047235083490265582, 0.053244713977759692, 0.058860144245324798, 0.064039797355015485,
			0.068745323835736408, 0.072941885005653087, 0.076598410645870640, 0.079687828912071670,
			0.082187266704339706, 0.084078218979661945, 0.085346685739338721, 0.085983275670394821 };

	private static int ngau = 18;
	private static final int ASWITCH = 100; // When to switch to quadrature
											// method.
	private static final double EPS = 2.22045e-16, FPMIN = Double.MIN_NORMAL / EPS;

	private Gamma()
	{
	}

	public static double gamma(double x)
	{
		return Math.exp(gammaLn(x));
	}

	// Natural logarithm of the gamma function
	public static double gammaLn(double x)
	{
		double[] coef = new double[] { 57.1562356658629235, -59.5979603554754912,
				14.1360979747417471, -0.491913816097620199, 0.339946499848118887E-4,
				0.465236289270485756E-4, -0.983744753048795646E-4, 0.158088703224912494E-3,
				-0.210264441724104883E-3, 0.217439618115212643E-3, -0.164318106536763890E-3,
				0.844182239838527433E-4, -0.261908384015814087E-4, 0.368991826595316234E-5 };
		double denominator = x;
		double series = 0.999999999999997092;
		double temp = x + 5.24218750000000000;
		temp = (x + 0.5) * Math.log(temp) - temp;
		for (int j = 0; j < 14; j++)
			series += coef[j] / ++denominator;
		return temp + Math.log(2.5066282746310005 * series / x);
	}

	// Incomplete gamma by quadrature. Returns P.a;x/ or Q.a; x/, when psig is 1
	// or 0,
	// respectively. User should not call directly.
	private static double gammpapprox(double a, double x, boolean psig)
	{
		int j;
		double xu, t, sum, ans;
		double a1 = a - 1.0, lna1 = Math.log(a1), sqrta1 = Math.sqrt(a1);
		double gln = gammaLn(a);
		// Set how far to integrate into the tail:
		if (x > a1)
			xu = Math.max(a1 + 11.5 * sqrta1, x + 6.0 * sqrta1);
		else
			xu = Math.max(0., Math.min(a1 - 7.5 * sqrta1, x - 5.0 * sqrta1));
		sum = 0;
		for (j = 0; j < ngau; j++)
		{ // Gauss-Legendre.
			t = x + (xu - x) * y[j];
			sum += w[j] * Math.exp(-(t - a1) + a1 * (Math.log(t) - lna1));
		}
		ans = sum * (xu - x) * Math.exp(a1 * (lna1 - 1.) - gln);
		return psig ? ans > 0.0 ? 1.0 - ans : -ans : ans >= 0.0 ? ans : 1.0 + ans;
	}

	// Returns the incomplete gamma function Q.a; x/ evaluated by its continued
	// fraction representation.
	private static double gcf(final double a, final double x)
	{
		int i;
		double an, b, c, d, del, h;
		double gln = gammaLn(a);
		b = x + 1.0 - a; // Set up for evaluating continued fraction by modified
							// Lentz’s method (5.2) with b0 D 0.
		c = 1.0 / FPMIN;
		d = 1.0 / b;
		h = d;
		for (i = 1;; i++)
		{ // Iterate to convergence.
			an = -i * (i - a);
			b += 2.0;
			d = an * d + b;
			if (Math.abs(d) < FPMIN)
				d = FPMIN;
			c = b + an / c;
			if (Math.abs(c) < FPMIN)
				c = FPMIN;
			d = 1.0 / d;
			del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) <= EPS)
				break;
		}
		return Math.exp(-x + a * Math.log(x) - gln) * h; // Put factors in
															// front.
	}

	// Returns the incomplete gamma function P.a;x/ evaluated by its series
	// representation.
	private static double gser(final double a, final double x)
	{
		double sum, del, ap;
		double gln = gammaLn(a);
		ap = a;
		del = sum = 1.0 / a;
		for (;;)
		{
			++ap;
			del *= x / ap;
			sum += del;
			if (Math.abs(del) < Math.abs(sum) * EPS)
			{
				return sum * Math.exp(-x + a * Math.log(x) - gln);
			}
		}
	}

	// Returns x such that P.a;x/ D p for an argument p between 0 and 1.
	public static double inverseRegLowerIncompleteGamma(double p, double a)
	{
		int j;
		double x, err, t, u, pp, lna1 = 0, afac = 0, a1 = a - 1;
		final double EPS = 1.e-8; // Accuracy is the square of EPS.
		double gln = gammaLn(a);
		if (a <= 0.)
			throw new IllegalArgumentException("a must be pos in invgammap");
		if (p >= 1.)
			return Math.max(100., a + 100. * Math.sqrt(a));
		if (p <= 0.)
			return 0.0;
		if (a > 1.)
		{ // Initial guess based on reference [1].
			lna1 = Math.log(a1);
			afac = Math.exp(a1 * (lna1 - 1.) - gln);
			pp = p < 0.5 ? p : 1. - p;
			t = Math.sqrt(-2. * Math.log(pp));
			x = (2.30753 + t * 0.27061) / (1. + t * (0.99229 + t * 0.04481)) - t;
			if (p < 0.5)
				x = -x;
			x = Math.max(1.e-3, a * Math.pow(1. - 1. / (9. * a) - x / (3. * Math.sqrt(a)), 3));
		}
		else
		{ // Initial guess based on equations (6.2.8)
			t = 1.0 - a * (0.253 + a * 0.12); // and (6.2.9).
			if (p < t)
				x = Math.pow(p / t, 1. / a);
			else
				x = 1. - Math.log(1. - (p - t) / (1. - t));
		}
		for (j = 0; j < 12; j++)
		{
			if (x <= 0.0)
				return 0.0;// x too small to compute accurately.
			err = regLowerIncompleteGamma(a, x) - p;
			if (a > 1.)
				t = afac * Math.exp(-(x - a1) + a1 * (Math.log(x) - lna1));
			else
				t = Math.exp(-x + a1 * Math.log(x) - gln);
			u = err / t;
			x -= t = u / (1. - 0.5 * Math.min(1., u * ((a - 1.) / x - 1))); // Halley’s
																			// method.
			if (x <= 0.)
				x = 0.5 * (x + t); // Halve old value if x tries to go negative.
			if (Math.abs(t) < EPS * x)
				break;
		}
		return x;
	}

	// Returns lower incomplete gamma function
	public static double lowerIncompleteGamma(final double a, final double x)
	{
		return regLowerIncompleteGamma(a, x) * Math.exp(gammaLn(a));
	}

	// Returns regularized lower incomplete gamma function P.a;x/.
	public static double regLowerIncompleteGamma(final double a, final double x)
	{
		if (x < 0.0 || a <= 0.0)
			throw new IllegalArgumentException("bad args in gammp");
		if (x == 0.0)
			return 0.0;
		else if ((int) a >= ASWITCH)
			return gammpapprox(a, x, true); // Quadrature.
		else if (x < a + 1.0)
			return gser(a, x); // Use the series representation.
		else
			return 1.0 - gcf(a, x); // Use the continued fraction
									// representation.
	}

	// Returns the regularized upper incomplete gamma function Q.a; x/  1 
	// P.a;x/.
	public static double regUpperIncompleteGamma(final double a, final double x)
	{
		if (x < 0.0 || a <= 0.0)
			throw new IllegalArgumentException("bad args in gammq");
		if (x == 0.0)
			return 1.0;
		else if ((int) a >= ASWITCH)
			return gammpapprox(a, x, false); // Quadrature.
		else if (x < a + 1.0)
			return 1.0 - gser(a, x); // Use the series representation.
		else
			return gcf(a, x); // Use the continued fraction representation.
	}

	// Returns upper incomplete gamma function
	public static double upperIncompleteGamma(final double a, final double x)
	{
		return regUpperIncompleteGamma(a, x) * Math.exp(gammaLn(a));
	}
}
