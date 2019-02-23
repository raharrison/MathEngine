package uk.co.ryanharrison.mathengine.special;

/**
 * Representation of the Beta function
 * 
 * @author Ryan Harrison
 * 
 */
public final class Beta
{
	/**
	 * Not permitted to create an instance of this class
	 */
	private Beta()
	{
	}

	/**
	 * Returns the result of the function Beta(Z, W)
	 * 
	 * @param z
	 *            The parameter Z
	 * @param w
	 *            The parameter W
	 * @return The result of the calculation of B(z, w)
	 */
	public static double beta(final double z, final double w)
	{
		return Math.exp(Gamma.gammaLn(z) + Gamma.gammaLn(w) - Gamma.gammaLn(z + w));
	}

}
