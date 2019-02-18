package uk.co.ryanharrison.mathengine.parser.nodes;

public interface NodeArithmetic
{
	NodeConstant divide(NodeConstant arg2);

	NodeConstant divide(NodeMatrix arg2);

	NodeConstant divide(NodeNumber arg2);

	NodeConstant divide(NodePercent arg2);

	NodeConstant divide(NodeVector arg2);

	NodeConstant multiply(NodeConstant arg2);

	NodeConstant multiply(NodeMatrix arg2);

	NodeConstant multiply(NodeNumber arg2);

	NodeConstant multiply(NodePercent arg2);

	NodeConstant multiply(NodeVector arg2);

	NodeConstant pow(NodeConstant arg2);

	NodeConstant pow(NodeMatrix arg2);

	NodeConstant pow(NodeNumber arg2);

	NodeConstant pow(NodePercent arg2);

	NodeConstant pow(NodeVector arg2);

}
