package uk.co.raharrison.mathengine.parser.nodes;

public interface NodeMath
{
	NodeConstant add(NodeConstant arg2);

	NodeConstant divide(NodeConstant arg2);

	NodeConstant multiply(NodeConstant arg2);

	NodeConstant pow(NodeConstant arg2);

	NodeConstant subtract(NodeConstant arg2);
}
