package uk.co.raharrison.mathengine.parser.nodes;

public class NodeFactory
{
	private static final int maxInt = Integer.MAX_VALUE;
	private static final int precision = 4;

	public static NodeNumber createNodeNumberFrom(double value)
	{
		double absValue = Math.abs(value);

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
			return new NodeRational(value);
	}
}
