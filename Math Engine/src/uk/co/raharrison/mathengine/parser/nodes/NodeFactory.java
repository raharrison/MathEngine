package uk.co.raharrison.mathengine.parser.nodes;

public final class NodeFactory
{
	private static final int maxInt = Integer.MAX_VALUE;
	private static final int precision = 4;

	private static boolean useRationals = true;

	public static NodeNumber createNodeNumberFrom(double value)
	{
		double absValue = Math.abs(value);

		if (!useRationals)
			return new NodeDouble(value);
		// Greater than max possible number
		if (absValue > maxInt)
			return new NodeDouble(value);
		// Too small
		else if (absValue < 1.0 / maxInt)
			return new NodeDouble(value);
		// Too much precision
		else if (Double.toString(absValue).split("\\.")[1].length() > precision)
			return new NodeDouble(value);
		else
			try
			{
				return new NodeRational(value);
			}
			catch (RuntimeException e)
			{
				return new NodeDouble(value);
			}
	}

	public static NodeNumber createZeroNumber()
	{
		return createNodeNumberFrom(0.0);
	}

	public static boolean isUsingRationals()
	{
		return useRationals;
	}

	public static void setUseRationals(boolean useRationals)
	{
		NodeFactory.useRationals = useRationals;
	}
}
