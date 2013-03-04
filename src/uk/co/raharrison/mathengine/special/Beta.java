package uk.co.raharrison.mathengine.special;

public class Beta
{
	private Beta()
	{}

	// Returns the value of the beta function B.z;w/.
	public static double beta(final double z, final double w)
	{
		return Math.exp(Gamma.gammaLn(z) + Gamma.gammaLn(w)
				- Gamma.gammaLn(z + w));
	}

}
