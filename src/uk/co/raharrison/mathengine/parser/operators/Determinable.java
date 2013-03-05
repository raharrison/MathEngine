package uk.co.raharrison.mathengine.parser.operators;

import uk.co.raharrison.mathengine.parser.nodes.NodeConstant;

public interface Determinable
{
	NodeConstant getResult(NodeConstant number);
}
