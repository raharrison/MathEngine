package uk.co.raharrison.mathengine.special;

public class Beta
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

	private static final int SWITCH = 3000; // When to switch to quadrature
											// method.

	private static final double EPS = 2.22045e-16, FPMIN = Double.MIN_NORMAL / EPS;

	// Returns the value of the beta function B.z;w/.
	public static double beta(final double z, final double w)
	{
		return Math.exp(Gamma.gammaLn(z) + Gamma.gammaLn(w) - Gamma.gammaLn(z + w));
	}

	// Evaluates continued fraction for incomplete beta function by modified
	// Lentz’s method
	private static double betacf(final double a, final double b, final double x)
	{
		int m, m2;
		double aa, c, d, del, h, qab, qam, qap;
		qab = a + b; // These q’s will be used in factors that
		qap = a + 1.0; // occur in the coefficients (6.4.6).
		qam = a - 1.0;
		c = 1.0; // First step of Lentz’s method.
		d = 1.0 - qab * x / qap;
		if (Math.abs(d) < FPMIN)
			d = FPMIN;
		d = 1.0 / d;
		h = d;
		for (m = 1; m < 10000; m++)
		{
			m2 = 2 * m;
			aa = m * (b - m) * x / ((qam + m2) * (a + m2));
			d = 1.0 + aa * d; // One step (the even one) of the recurrence
			if (Math.abs(d) < FPMIN)
				d = FPMIN;
			c = 1.0 + aa / c;
			if (Math.abs(c) < FPMIN)
				c = FPMIN;
			d = 1.0 / d;
			h *= d * c;
			aa = -(a + m) * (qab + m) * x / ((a + m2) * (qap + m2));
			d = 1.0 + aa * d;// Next step of the recurrence (the odd one)
			if (Math.abs(d) < FPMIN)
				d = FPMIN;
			c = 1.0 + aa / c;
			if (Math.abs(c) < FPMIN)
				c = FPMIN;
			d = 1.0 / d;
			del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) <= EPS)
				break; // Are we done?
		}
		return h;
	}

	// Incomplete beta by quadrature. Returns Ix.a; b/. User should not call
	// directly.
	private static double betaiapprox(double a, double b, double x)
	{
		int j;
		double xu, t, sum, ans;
		double a1 = a - 1.0, b1 = b - 1.0, mu = a / (a + b);
		double lnmu = Math.log(mu), lnmuc = Math.log(1. - mu);
		t = Math.sqrt(a * b / (Math.pow(a + b, 2) * (a + b + 1.0)));
		if (x > a / (a + b))
		{ // Set how far to integrate into the tail:
			if (x >= 1.0)
				return 1.0;
			xu = Math.min(1., Math.max(mu + 10. * t, x + 5.0 * t));
		}
		else
		{
			if (x <= 0.0)
				return 0.0;
			xu = Math.max(0., Math.min(mu - 10. * t, x - 5.0 * t));
		}
		sum = 0;
		for (j = 0; j < 18; j++)
		{ // Gauss-Legendre.
			t = x + (xu - x) * y[j];
			sum += w[j] * Math.exp(a1 * (Math.log(t) - lnmu) + b1 * (Math.log(1 - t) - lnmuc));
		}
		ans = sum
				* (xu - x)
				* Math.exp(a1 * lnmu - Gamma.gammaLn(a) + b1 * lnmuc - Gamma.gammaLn(b)
						+ Gamma.gammaLn(a + b));
		return ans > 0.0 ? 1.0 - ans : -ans;
	}

	// Returns the incomplete beta function
	public static double incompleteBeta(final double a, final double b, final double x)
	{
		return regIncompleteBeta(a, b, x) * beta(a, b);
	}

	// Inverse of regularized incomplete beta function. Returns x such that
	// Ix.a; b/ D p for argument p
	// between 0 and 1.
	public static double inverseRegIncompleteBeta(double p, double a, double b)
	{
		final double EPS = 1.e-8;
		double pp, t, u, err, x, al, h, w, afac, a1 = a - 1., b1 = b - 1.;
		int j;
		if (p <= 0.)
			return 0.;
		else if (p >= 1.)
			return 1.;
		else if (a >= 1. && b >= 1.)
		{ // Set initial guess. See text.
			pp = p < 0.5 ? p : 1. - p;
			t = Math.sqrt(-2. * Math.log(pp));
			x = (2.30753 + t * 0.27061) / (1. + t * (0.99229 + t * 0.04481)) - t;
			if (p < 0.5)
				x = -x;
			al = (Math.pow(x, 2) - 3.) / 6.;
			h = 2. / (1. / (2. * a - 1.) + 1. / (2. * b - 1.));
			w = x * Math.sqrt(al + h) / h - (1. / (2. * b - 1) - 1. / (2. * a - 1.))
					* (al + 5. / 6. - 2. / (3. * h));
			x = a / (a + b * Math.exp(2. * w));
		}
		else
		{
			double lna = Math.log(a / (a + b)), lnb = Math.log(b / (a + b));
			t = Math.exp(a * lna) / a;
			u = Math.exp(b * lnb) / b;
			w = t + u;
			if (p < t / w)
				x = Math.pow(a * w * p, 1. / a);
			else
				x = 1. - Math.pow(b * w * (1. - p), 1. / b);
		}
		afac = -Gamma.gammaLn(a) - Gamma.gammaLn(b) + Gamma.gammaLn(a + b);
		for (j = 0; j < 10; j++)
		{
			if (x == 0. || x == 1.)
				return x; // a or b too small for accurate calculation
			err = regIncompleteBeta(a, b, x) - p;
			t = Math.exp(a1 * Math.log(x) + b1 * Math.log(1. - x) + afac);
			u = err / t; // Halley:
			x -= t = u / (1. - 0.5 * Math.min(1., u * (a1 / x - b1 / (1. - x))));
			if (x <= 0.)
				x = 0.5 * (x + t); // Bisect if x tries to go neg or > 1.
			if (x >= 1.)
				x = 0.5 * (x + t + 1.);
			if (Math.abs(t) < EPS * x && j > 0)
				break;
		}
		return x;
	}

	// Returns regularized incomplete beta function Ix.a; b/ for positive a and
	// b, and x between 0 and 1.
	public static double regIncompleteBeta(final double a, final double b, final double x)
	{
		double bt;
		if (a <= 0.0 || b <= 0.0)
			throw new IllegalArgumentException("Bad a or b in routine betai");
		if (x < 0.0 || x > 1.0)
			throw new IllegalArgumentException("Bad x in routine betai");
		if (x == 0.0 || x == 1.0)
			return x;
		if (a > SWITCH && b > SWITCH)
			return betaiapprox(a, b, x);

		bt = Math.exp(Gamma.gammaLn(a + b) - Gamma.gammaLn(a) - Gamma.gammaLn(b) + a * Math.log(x)
				+ b * Math.log(1.0 - x));
		if (x < (a + 1.0) / (a + b + 2.0))
			return bt * betacf(a, b, x) / a;
		else
			return 1.0 - bt * betacf(b, a, 1.0 - x) / b;
	}

	private Beta()
	{
	}

}
