package uk.co.raharrison.mathengine.parser.nodes;

public interface NodeArithmetic
{
	NodeConstant add(NodeConstant arg2);

	NodeConstant add(NodeMatrix arg2);

	NodeConstant add(NodeNumber arg2);

	NodeConstant add(NodePercent arg2);

	NodeConstant add(NodeVector arg2);

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

	NodeConstant subtract(NodeConstant arg2);

	NodeConstant subtract(NodeMatrix arg2);

	NodeConstant subtract(NodeNumber arg2);

	NodeConstant subtract(NodePercent arg2);

	NodeConstant subtract(NodeVector arg2);
}
