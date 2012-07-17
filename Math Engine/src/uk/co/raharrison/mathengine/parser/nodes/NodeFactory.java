package uk.co.raharrison.mathengine.parser.nodes;

public class NodeFactory
{
	private static final int maxInt = Integer.MAX_VALUE;
	private static final int maxLength = Integer.toString(maxInt).length();
	
	public static NodeNumber createNodeNumberFrom(double value)
	{
		double absValue = Math.abs(value);
		
		if(absValue > maxInt)
			return new NodeDouble(value);
		else if(absValue < 1.0 / maxInt)
			return new NodeDouble(value);
		else if(Double.toString(absValue).split(".")[1].length() > maxLength)
			return new NodeDouble(value);
		else
			return new NodeRational(value);
	}
}
