package uk.co.raharrison.mathengine.parser.nodes;

import java.util.ArrayList;

import uk.co.raharrison.mathengine.Utils;
import uk.co.raharrison.mathengine.parser.Parser;

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

	public static NodeVector createVectorFrom(String expression, Parser parser)
	{
		ArrayList<Node> vals = new ArrayList<>();
		int i = 0;
		StringBuilder b = new StringBuilder();
		while (i < expression.length())
		{
			if (expression.charAt(i) == '{')
			{
				int ma = 0;
				vals.add(parser.parse(expression.substring(i,
						1 + (ma = Utils.matchingCharacterIndex(expression, i, '{', '}')))));
				i = ++ma;
			}
			else
			{
				int j = i;
				while (j < expression.length() && expression.charAt(j) != ',')
				{
					b.append(expression.charAt(j));
					j++;
				}
				if (b.length() != 0)
					vals.add(parser.parse(b.toString()));
				i += b.length() + 1;
				b.setLength(0);
			}
		}

		return new NodeVector(vals.toArray(new Node[vals.size()]));
	}
}
