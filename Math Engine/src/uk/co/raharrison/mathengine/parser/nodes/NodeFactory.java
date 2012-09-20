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
		else if (absValue < 1.0 / maxInt && absValue != 0)
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
		if(Utils.isNullOrEmpty(expression))
			return new NodeVector(new Node[0]);
		
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

	public static NodeMatrix createMatrixFrom(String expression, Parser parser)
	{
		if(Utils.isNullOrEmpty(expression))
			return new NodeMatrix(new Node[0][0]);
		
		ArrayList<NodeVector> vals = new ArrayList<>();
		NodeVector v = createVectorFrom(expression, parser);
		int len = 0;

		for (Node n : v.getValues())
		{
			NodeVector vec;
			if (n instanceof NodeVector)
				vec = (NodeVector) n;
			else
				vec = new NodeVector(new Node[] { n });

			if (len == 0)
				len = vec.getSize();
			else if (len != vec.getSize())
				throw new RuntimeException("Invalid matrix dimensions");

			vals.add(vec);
		}

		return createMatrixFrom(vals);
	}

	private static NodeMatrix createMatrixFrom(ArrayList<NodeVector> vals)
	{
		Node[][] results = new Node[vals.size()][vals.get(0).getSize()];

		for (int i = 0; i < results.length; i++)
		{
			for (int j = 0; j < results[0].length; j++)
			{
				results[i][j] = vals.get(i).getValues()[j];
			}
		}
		return new NodeMatrix(results);
	}
}
